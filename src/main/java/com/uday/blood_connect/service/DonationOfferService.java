package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.DonationOfferResponseDTO;
import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.exception.ResourceEmptyException;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.repository.BloodRequestRepository;
import com.uday.blood_connect.repository.DonationOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationOfferService {

    private final DonationOfferRepository donationOfferRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final UserService userService;

    public Page<DonationOfferResponseDTO> getRequestOffers(String username, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("offeredAt").descending());

        User user = userService.getUserByEmail(username);

        Page<DonationOffer> offers = donationOfferRepository.findByDonor(user, pageable);

        if (offers.isEmpty()) {
            throw new ResourceEmptyException("No offers are available");
        }

        return offers.map(this::mapToDTO);
    }

    public DonationOfferResponseDTO getOfferById(String username, Long offerId) {

        DonationOffer offer = getOfferById(offerId);

        offer.verifyDonor(userService.getUserByEmail(username));

        return mapToDTO(offer);
    }

    @Transactional
    public DonationOfferResponseDTO acceptOffer(Long offerId, String username) {

        DonationOffer offer = getOfferById(offerId);

        offer.verifyDonor(userService.getUserByEmail(username));

        offer.getBloodRequest().acceptRequest(offer);

        donationOfferRepository.save(offer);

        return mapToDTO(offer);
    }

    public DonationOfferResponseDTO declineOffer(Long offerId, String username) {

        DonationOffer offer = getOfferById(offerId);

        offer.verifyDonor(userService.getUserByEmail(username));

        offer.decline();

        donationOfferRepository.save(offer);

        return mapToDTO(offer);
    }

    @Transactional
    public DonationOfferResponseDTO completeOffer(Long offerId, String username) {

        DonationOffer offer = getOfferById(offerId);
        BloodRequest bloodRequest = offer.getBloodRequest();

        offer.verifyDonor(userService.getUserByEmail(username));

        offer.complete();

        bloodRequest.fulfillRequest();

        List<DonationOffer> otherOffers =
                donationOfferRepository.findByBloodRequestIdAndIdNot(offer.getBloodRequest().getId(), offer.getId());

        otherOffers.forEach(DonationOffer::close);

        List<DonationOffer> allOffers = new ArrayList<>(otherOffers);
        allOffers.add(offer);

        donationOfferRepository.saveAll(allOffers);
        bloodRequestRepository.save(bloodRequest);

        return mapToDTO(offer);
    }

    public DonationOffer getOfferById(Long offerId) {
        return donationOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
    }

    protected DonationOfferResponseDTO mapToDTO(DonationOffer offer) {
        return new DonationOfferResponseDTO(
                offer.getId(),
                offer.getBloodRequest().getRequester().getFullName(),
                offer.getBloodRequest().getBloodGroup(),
                offer.getBloodRequest().getCity(),
                offer.getBloodRequest().getUrgencyLevel(),
                offer.getBloodRequest().getMessage(),
                offer.getStatus(),
                offer.getOfferedAt().toString(),
                offer.getRespondedAt() != null ? offer.getRespondedAt().toString() : null
        );
    }
}