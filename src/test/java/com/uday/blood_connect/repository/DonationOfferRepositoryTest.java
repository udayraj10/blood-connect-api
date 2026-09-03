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
    void findByDonor_WhenDonorExists_ThenReturnsMatchingOffers() {
        User donor = createValidUser();
        entityManager.persistAndFlush(donor);

        Pageable pageable = PageRequest.of(0, 10);

        List<DonationOffer> offers = donationOfferRepository.findByDonor(donor, pageable).getContent();

        assertEquals(1, offers.size());
        assertEquals(donor.getId(), offers.get(0).getDonor().getId());
        assertEquals(donor.getBloodGroup(), offers.get(0).getBloodRequest().getBloodGroup());
    }

    @Test
    void findByDonor_WhenDonationOffersDoNotExist_ThenReturnsEmptyList() {
        User donor = createValidUser();
        donor.setDonationOffers(new ArrayList<>());
        entityManager.persistAndFlush(donor);

        Pageable pageable = PageRequest.of(0, 10);

        List<DonationOffer> offers = donationOfferRepository.findByDonor(donor, pageable).getContent();

        assertEquals(0, offers.size());
    }

    @Test
    void findByDonor_WhenMultipleDonorsExist_ThenReturnsMatchingOffers() {
        User donor1 = createValidUser();
        entityManager.persistAndFlush(donor1);

        User donor2 = createValidUser();
        donor2.setEmail("raju@gmail.com");
        donor2.setFullName("Raju");
        entityManager.persistAndFlush(donor2);
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);

        List<DonationOffer> offers1 = donationOfferRepository.findByDonor(donor1, pageable).getContent();
        List<DonationOffer> offers2 = donationOfferRepository.findByDonor(donor2, pageable).getContent();

        assertEquals(1, offers1.size());
        assertEquals(donor1.getId(), offers1.get(0).getDonor().getId());
        assertEquals(1, offers2.size());
        assertEquals(donor2.getId(), offers2.get(0).getDonor().getId());
    }

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
    void findByBloodRequestId_WhenBloodRequestExists_ThenReturnsMatchingOffers() {
        User donor = createValidUser();
        entityManager.persistAndFlush(donor);

        BloodRequest bloodRequest = donor.getBloodRequests().get(0);
        Pageable pageable = PageRequest.of(0, 10);

        List<DonationOffer> offers = donationOfferRepository.findByBloodRequestId(bloodRequest.getId(), pageable).getContent();

        assertEquals(1, offers.size());
        assertEquals(bloodRequest.getId(), offers.get(0).getBloodRequest().getId());
    }

    @Test
    void findByBloodRequestId_WhenBloodRequestDoesNotExist_ThenReturnsEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);

        List<DonationOffer> offers = donationOfferRepository.findByBloodRequestId(999L, pageable).getContent();

        assertEquals(0, offers.size());
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
