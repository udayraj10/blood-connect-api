package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.DonationOfferResponseDTO;
import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import com.uday.blood_connect.exception.RequestAlreadyFulFilledException;
import com.uday.blood_connect.repository.BloodRequestRepository;
import com.uday.blood_connect.repository.DonationOfferRepository;
import com.uday.blood_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonationOfferServiceTest {

    @Mock
    private DonationOfferRepository donationOfferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BloodRequestRepository bloodRequestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private DonationOfferService donationOfferService;

    @Test
    void acceptOffer_WhenOfferExists_ReturnOfferResponse() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest request = createValidBloodRequest(user);
        DonationOffer offer = createValidDonationOffer(user, request);
        offer.setId(1L);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(donationOfferRepository.save(any(DonationOffer.class))).thenReturn(offer);

        DonationOfferResponseDTO response = donationOfferService.acceptOffer(offer.getId(), user.getEmail());

        assertNotNull(response);
        assertEquals(OfferStatus.ACCEPTED, response.status());

        verify(donationOfferRepository, times(1)).save(any(DonationOffer.class));
    }

    @Test
    void acceptOffer_WhenUserDoesNotMatch_ThrowException() {
        User user1 = createValidUser();
        user1.setId(1L);
        BloodRequest request = createValidBloodRequest(user1);
        DonationOffer offer = createValidDonationOffer(user1, request);
        offer.setId(1L);
        User user2 = createValidUser();
        user2.setId(2L);
        user2.setEmail("raj@gmail.com");

        when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(AccessDeniedException.class, () -> donationOfferService.acceptOffer(offer.getId(),
                user2.getEmail()));
    }

    @Test
    void acceptOffer_WhenOfferAccepted_ThrowException(){
        User user = createValidUser();
        user.setId(1L);
        BloodRequest request = createValidBloodRequest(user);
        DonationOffer offer = createValidDonationOffer(user, request);
        offer.setId(1L);
        offer.setStatus(OfferStatus.ACCEPTED);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(RequestAlreadyFulFilledException.class, () -> donationOfferService.acceptOffer(offer.getId(),
                user.getEmail()));
    }

    @Test
    void declineOffer_WhenOfferExists_ReturnOfferResponse() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest request = createValidBloodRequest(user);
        DonationOffer offer = createValidDonationOffer(user, request);
        offer.setId(1L);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(donationOfferRepository.save(any(DonationOffer.class))).thenReturn(offer);

        DonationOfferResponseDTO response = donationOfferService.declineOffer(offer.getId(), user.getEmail());

        assertNotNull(response);
        assertEquals(OfferStatus.DECLINED, response.status());

        verify(donationOfferRepository, times(1)).save(any(DonationOffer.class));
    }

    @Test
    void declineOffer_WhenUserDoesNotMatch_ThrowException() {
        User user1 = createValidUser();
        user1.setId(1L);
        BloodRequest request = createValidBloodRequest(user1);
        DonationOffer offer = createValidDonationOffer(user1, request);
        offer.setId(1L);
        User user2 = createValidUser();
        user2.setId(2L);
        user2.setEmail("raj@gmail.com");

        when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(AccessDeniedException.class, () -> donationOfferService.declineOffer(offer.getId(),
                user2.getEmail()));
    }

    @Test
    void declineOffer_WhenOfferAccepted_ThrowException() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest request = createValidBloodRequest(user);
        DonationOffer offer = createValidDonationOffer(user, request);
        offer.setId(1L);
        offer.setStatus(OfferStatus.ACCEPTED);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(RequestAlreadyFulFilledException.class, () -> donationOfferService.declineOffer(offer.getId(),
                user.getEmail()));
    }

    @Test
    void completeOffer_WhenOfferExists_ReturnOfferResponse() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest request = createValidBloodRequest(user);
        DonationOffer offer = createValidDonationOffer(user, request);
        offer.setId(1L);
        offer.setStatus(OfferStatus.ACCEPTED);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(donationOfferRepository.saveAll(anyList())).thenReturn(List.of(offer));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(bloodRequestRepository.save(any(BloodRequest.class))).thenReturn(request);

        DonationOfferResponseDTO response = donationOfferService.completeOffer(offer.getId(), user.getEmail());

        assertNotNull(response);
        assertEquals(OfferStatus.COMPLETED, response.status());

        verify(donationOfferRepository, times(1)).saveAll(anyList());
        verify(userRepository, times(1)).save(any(User.class));
        verify(bloodRequestRepository, times(1)).save(any(BloodRequest.class));
    }

    @Test
    void completeOffer_WhenOfferIsCompleted_ThrowException() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest request = createValidBloodRequest(user);
        DonationOffer offer = createValidDonationOffer(user, request);
        offer.setId(1L);
        offer.setStatus(OfferStatus.COMPLETED);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(donationOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(RequestAlreadyFulFilledException.class, () -> donationOfferService.completeOffer(offer.getId(),
                user.getEmail()));
    }

    private User createValidUser() {
        return User.builder()
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
    }

    private BloodRequest createValidBloodRequest(User requester) {
        return BloodRequest.builder()
                .requester(requester)
                .bloodGroup(BloodGroup.A_POS)
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
                .offeredAt(LocalDateTime.now())
                .build();
    }
}
