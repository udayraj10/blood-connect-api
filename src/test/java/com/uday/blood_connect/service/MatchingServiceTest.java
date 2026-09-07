package com.uday.blood_connect.service;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.repository.DonationOfferRepository;
import com.uday.blood_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DonationOfferRepository donationOfferRepository;

    @InjectMocks
    private MatchingService matchingService;

    @Captor
    private ArgumentCaptor<List<DonationOffer>> offersCaptor;

    @Test
    void findMatchingUsers_WhenMatchesExist_ShouldSaveOffers() {
        User requester = createValidUser();
        requester.setId(1L);
        User matchUser = createValidUser();
        matchUser.setId(2L);
        matchUser.setEmail("raju@gmail.com");
        matchUser.setFullName("Raju");

        BloodRequest request = createValidBloodRequest(requester);
        request.setId(1L);

        when(userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(request.getBloodGroup(), request.getCity(), true))
                .thenReturn(List.of(requester, matchUser));

        matchingService.findMatchingUsers(request, requester);

        verify(donationOfferRepository, times(1)).saveAll(offersCaptor.capture());

        List<DonationOffer> savedOffers = offersCaptor.getValue();
        assertEquals(1, savedOffers.size());

        DonationOffer savedOffer = savedOffers.get(0);
        assertEquals(matchUser, savedOffer.getDonor());
        assertEquals(request, savedOffer.getBloodRequest());
        assertEquals(OfferStatus.PENDING, savedOffer.getStatus());
    }

    @Test
    void findMatchingUsers_WhenNoMatchesExist_ShouldThrowExceptionAndNotSaveOffers() {
        User requester = createValidUser();
        requester.setId(1L);

        BloodRequest request = createValidBloodRequest(requester);
        request.setId(1L);

        when(userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(request.getBloodGroup(), request.getCity(), true))
                .thenReturn(List.of(requester));

        assertThrows(ResourceNotFoundException.class, () -> matchingService.findMatchingUsers(request, requester));

        verify(donationOfferRepository, never()).saveAll(any());
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
}
