package com.uday.blood_connect.service;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.OfferStatus;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.repository.DonationOfferRepository;
import com.uday.blood_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final DonationOfferRepository donationOfferRepository;
    private final UserRepository userRepository;

    public void findMatchingUsers(BloodRequest bloodRequest, User requester) {

        List<User> matchedUsers = userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(
                        bloodRequest.getBloodGroup(),
                        bloodRequest.getCity(),
                        true
                ).stream()
                .filter(user -> !user.getId().equals(requester.getId())) // Exclude requester
                .toList();

        if (!matchedUsers.isEmpty()) {
            createOffersForMatches(bloodRequest, matchedUsers);
        } else {
            throw new ResourceNotFoundException("No matching donors found for the blood request.");
        }
    }

    private void createOffersForMatches(BloodRequest bloodRequest, List<User> matchedUsers) {

        List<DonationOffer> offers = matchedUsers.stream()
                .map(user -> DonationOffer.builder()
                        .donor(user)
                        .bloodRequest(bloodRequest)
                        .status(OfferStatus.PENDING)
                        .build())
                .toList();

        donationOfferRepository.saveAll(offers);
    }
}
