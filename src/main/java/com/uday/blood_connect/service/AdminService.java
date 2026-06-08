package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.BloodRequestResponseDTO;
import com.uday.blood_connect.dto.response.DonationOfferResponseDTO;
import com.uday.blood_connect.dto.response.StatsResponseDTO;
import com.uday.blood_connect.dto.response.UserResponseDTO;
import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.OfferStatus;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import com.uday.blood_connect.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final DonationOfferRepository donationOfferRepository;
    private final UserService userService;
    private final BloodRequestService bloodRequestService;
    private final DonationOfferService donationOfferService;

    public Page<UserResponseDTO.Details> getAllUsers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<User> users = userRepository.findAll(pageable);

        return users.map(this::mapToDTO);
    }

    public UserResponseDTO.Details getUserById(Long userId) {
        User user = userService.getUserById(userId);

        return mapToDTO(user);
    }

    public void deactivateUser(Long userId) {
        User user = userService.getUserById(userId);

        user.deactivate();

        userRepository.save(user);
    }

    public void activeUser(Long userId) {
        User user = userService.getUserById(userId);

        user.activate();

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userService.getUserById(userId);

        userRepository.delete(user);
    }

    public Page<BloodRequestResponseDTO.Details> getAllBloodRequests(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<BloodRequest> bloodRequests = bloodRequestRepository.findAll(pageable);

        return bloodRequests.map(this::mapToDTO);
    }

    public BloodRequestResponseDTO.Details getBloodRequestById(Long requestId) {
        BloodRequest bloodRequest = bloodRequestService.getBloodRequestById(requestId);

        return mapToDTO(bloodRequest);
    }

    public void cancelBloodRequest(Long requestId) {
        BloodRequest bloodRequest = bloodRequestService.getBloodRequestById(requestId);

        bloodRequest.cancelRequest();

        bloodRequestRepository.save(bloodRequest);
    }

    public Page<DonationOfferResponseDTO> getAllOffers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<DonationOffer> offers = donationOfferRepository.findAll(pageable);

        return offers.map(donationOfferService::mapToDTO);
    }

    public DonationOfferResponseDTO getOfferById(Long offerId) {
        DonationOffer offer = donationOfferService.getOfferById(offerId);

        return donationOfferService.mapToDTO(offer);
    }

    public StatsResponseDTO getStats() {
        List<AgeCount> ageList = userRepository.countByAge();
        List<BloodGroupCount> bloodGroupList = bloodRequestRepository.countByBloodGroup();

        Map<Integer, Long> ageCount = ageList.stream()
                .collect(Collectors.toMap(AgeCount::getAge, AgeCount::getCount));

        Map<BloodGroup, Long> bloodGroupCount = bloodGroupList.stream()
                .collect(Collectors.toMap(BloodGroupCount::getBloodGroup, BloodGroupCount::getCount));

        return new StatsResponseDTO(
                userRepository.count(),
                bloodRequestRepository.count(),
                donationOfferRepository.count(),
                bloodRequestRepository.countByStatus(RequestStatus.OPEN),
                donationOfferRepository.countByStatus(OfferStatus.ACCEPTED),
                bloodRequestRepository.countByStatus(RequestStatus.FULFILLED),
                donationOfferRepository.countByStatus(OfferStatus.DECLINED),
                bloodRequestRepository.countByStatus(RequestStatus.CANCELLED),
                bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.NORMAL),
                bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.URGENT),
                bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.CRITICAL),
                ageCount,
                bloodGroupCount
        );
    }

    private UserResponseDTO.Details mapToDTO(User user) {
        return new UserResponseDTO.Details(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAge(),
                user.getBloodGroup(),
                user.getCity(),
                user.getAddress(),
                user.getAccountType(),
                user.getIsAvailable(),
                user.getLastDonationDate(),
                user.getCreatedAt()
        );
    }

    private BloodRequestResponseDTO.Details mapToDTO(BloodRequest bloodRequest) {
        return new BloodRequestResponseDTO.Details(
                bloodRequest.getId(),
                bloodRequest.getBloodGroup(),
                bloodRequest.getCity(),
                bloodRequest.getUrgencyLevel(),
                bloodRequest.getStatus(),
                bloodRequest.getMessage(),
                bloodRequest.getCreatedAt()
        );
    }

}
