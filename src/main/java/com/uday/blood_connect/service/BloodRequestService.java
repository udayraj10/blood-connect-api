package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.BloodRequestResponseDTO;
import com.uday.blood_connect.dto.response.MatchResultsDTO;
import com.uday.blood_connect.dto.request.BloodRequestDTO;
import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.repository.BloodRequestRepository;
import com.uday.blood_connect.repository.DonationOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;
    private final DonationOfferRepository donationOfferRepository;
    private final UserService userService;
    private final MatchingService matchingService;

    @Transactional
    public BloodRequestResponseDTO.Summary createBloodRequest(BloodRequestDTO bloodRequestDTO, String username) {

        User requester = userService.getUserByEmail(username);

        BloodRequest bloodRequest = BloodRequest.builder()
                .requester(requester)
                .bloodGroup(bloodRequestDTO.bloodGroup())
                .city(bloodRequestDTO.city())
                .urgencyLevel(bloodRequestDTO.urgencyLevel())
                .status(RequestStatus.OPEN) // Default status
                .message(bloodRequestDTO.message())
                .build();

        BloodRequest savedBloodRequest = bloodRequestRepository.save(bloodRequest);
        matchingService.findMatchingUsers(savedBloodRequest, requester);

        return mapToDTO(savedBloodRequest);
    }

    public BloodRequestResponseDTO.Summary getBloodRequest(Long requestId, String username) {

        User user = userService.getUserByEmail(username);

        BloodRequest bloodRequest = bloodRequestRepository.findByIdAndRequesterId(requestId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));

        return mapToDTO(bloodRequest);
    }

    public Page<MatchResultsDTO> getDonorsForRequest(Long requestId, String username, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        User user = userService.getUserByEmail(username);

        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));

        Page<DonationOffer> offerPage = donationOfferRepository.findByBloodRequestId(requestId, pageable);

        bloodRequest.verifyOwner(user);

        if (offerPage.isEmpty()) {
            throw new ResourceNotFoundException("No donors found for this request");
        }

        return offerPage.map(offer -> new MatchResultsDTO(
                offer.getId(),
                offer.getDonor().getFullName(),
                offer.getDonor().getCity(),
                offer.getDonor().getBloodGroup(),
                offer.getStatus(),
                offer.getOfferedAt()
        ));
    }

    public Page<BloodRequestResponseDTO.Summary> getBloodRequests(String username, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        User user = userService.getUserByEmail(username);

        Page<BloodRequest> requests = bloodRequestRepository.findByRequester(user, pageable);

        return requests.map(this::mapToDTO);
    }

    @Transactional
    public BloodRequestResponseDTO.Summary cancelRequest(Long requestId, String username) {

        BloodRequest bloodRequest = getBloodRequestById(requestId);

        User user = userService.getUserByEmail(username);

        bloodRequest.verifyOwner(user);

        bloodRequest.cancelRequest();

        BloodRequest savedRequest = bloodRequestRepository.save(bloodRequest);

        return mapToDTO(savedRequest);
    }

    public BloodRequest getBloodRequestById(Long requestId) {
        return bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));
    }

    private BloodRequestResponseDTO.Summary mapToDTO(BloodRequest bloodRequest) {
        return new BloodRequestResponseDTO.Summary(
                bloodRequest.getId(),
                bloodRequest.getBloodGroup(),
                bloodRequest.getCity(),
                bloodRequest.getUrgencyLevel(),
                bloodRequest.getStatus(),
                bloodRequest.getMessage()
        );
    }
}
