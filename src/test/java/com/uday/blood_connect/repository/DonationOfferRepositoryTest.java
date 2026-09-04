package com.uday.blood_connect.repository;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DonationOfferRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DonationOfferRepository donationOfferRepository;

    @Test
    void countByDonorId_WhenDonationOffersExists_ThenReturnsCorrectCount() {
        User donor = createValidUser();
        entityManager.persistAndFlush(donor);
        entityManager.clear();

        long count = donationOfferRepository.countByDonorId(donor.getId());

        assertEquals(1, count);
    }

    @Test
    void countByDonorId_WhenDonationOffersDoNotExist_ThenReturnsZero() {
        User donor = createValidUser();
        donor.setDonationOffers(new ArrayList<>());
        entityManager.persistAndFlush(donor);
        entityManager.clear();

        long count = donationOfferRepository.countByDonorId(donor.getId());

        assertEquals(0, count);
    }

    @Test
    void countByDonorIdAndStatus_WhenDonationOffersExists_ThenReturnsCorrectCount() {
        User donor = createValidUser();
        entityManager.persistAndFlush(donor);
        entityManager.clear();

        long count = donationOfferRepository.countByDonorIdAndStatus(donor.getId(), OfferStatus.PENDING);

        assertEquals(1, count);
    }

    @Test
    void countByDonorIdAndStatus_WhenDonationOffersDoNotExist_ThenReturnsZero() {
        User donor = createValidUser();
        donor.setDonationOffers(new ArrayList<>());
        entityManager.persistAndFlush(donor);
        entityManager.clear();

        long count = donationOfferRepository.countByDonorIdAndStatus(donor.getId(), OfferStatus.PENDING);

        assertEquals(0, count);
    }

    @Test
    void findByBloodRequestIdAndIdNot_WhenMatchingOffersExist_ThenReturnsMatchingOffers() {
        User donor = createValidUser();
        entityManager.persistAndFlush(donor);

        DonationOffer offer2 = createValidDonationOffer(donor, donor.getBloodRequests().get(0));
        entityManager.persistAndFlush(offer2);

        BloodRequest bloodRequest = donor.getBloodRequests().get(0);
        DonationOffer offer = donor.getDonationOffers().get(0);

        List<DonationOffer> offers = donationOfferRepository.findByBloodRequestIdAndIdNot(bloodRequest.getId(), offer.getId());

        assertEquals(1, offers.size());
        assertEquals(offer2.getId(), offers.get(0).getId());
    }

    @Test
    void findByBloodRequestIdAndIdNot_WhenNoMatchingOffersExist_ThenReturnsEmptyList() {
        User donor = createValidUser();
        entityManager.persistAndFlush(donor);

        BloodRequest bloodRequest = donor.getBloodRequests().get(0);
        DonationOffer offer = donor.getDonationOffers().get(0);

        List<DonationOffer> offers = donationOfferRepository.findByBloodRequestIdAndIdNot(bloodRequest.getId(), offer.getId());

        assertEquals(0, offers.size());
    }

    private User createValidUser() {
        User user = User.builder()
                .fullName("Uday Kumar")
                .email("uday@gmail.com")
                .password("000000")
                .age(25)
                .phone("9876543210")
                .bloodGroup(BloodGroup.O_POS)
                .city("Bengaluru")
                .address("Maruti Nagar, Madivala")
                .accountType(AccountType.INDIVIDUAL)
                .role(Role.USER)
                .isAvailable(true)
                .isActive(true)
                .build();

        user.setBloodRequests(new ArrayList<>(List.of(createValidBloodRequest(user))));
        user.setDonationOffers(new ArrayList<>(List.of(createValidDonationOffer(user, user.getBloodRequests().get(0)))));

        return user;
    }

    private BloodRequest createValidBloodRequest(User requester) {
        return BloodRequest.builder()
                .requester(requester)
                .bloodGroup(BloodGroup.O_POS)
                .city("Bengaluru")
                .urgencyLevel(UrgencyLevel.NORMAL)
                .status(RequestStatus.OPEN)
                .message("Urgent requirement for surgery")
                .build();
    }

    private DonationOffer createValidDonationOffer(User donor, BloodRequest bloodRequest) {
        return DonationOffer.builder()
                .donor(donor)
                .bloodRequest(bloodRequest)
                .status(OfferStatus.PENDING)
                .build();
    }
}
