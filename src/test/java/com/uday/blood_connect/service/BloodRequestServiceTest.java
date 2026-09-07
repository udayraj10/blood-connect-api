package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.request.BloodRequestDTO;
import com.uday.blood_connect.dto.response.BloodRequestResponseDTO;
import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import com.uday.blood_connect.exception.RequestAlreadyFulFilledException;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.repository.BloodRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BloodRequestServiceTest {

    @Mock
    private BloodRequestRepository bloodRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private MatchingService matchingService;

    @InjectMocks
    private BloodRequestService bloodRequestService;

    @Test
    void createBloodRequest_WhenUserFound_ReturnBloodRequestResponse() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequestDTO bloodRequestDTO = createValidBloodRequestDTO();

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(bloodRequestRepository.save(any(BloodRequest.class))).thenAnswer(invocation -> {
            BloodRequest savedRequest = invocation.getArgument(0);
            savedRequest.setId(1L);
            return savedRequest;
        });

        BloodRequestResponseDTO response = bloodRequestService.createBloodRequest(bloodRequestDTO, user.getEmail());

        assertNotNull(response);
        assertEquals(bloodRequestDTO.bloodGroup(), response.bloodGroup());
        assertEquals(RequestStatus.OPEN, response.status());

        verify(bloodRequestRepository, times(1)).save(any(BloodRequest.class));
        verify(matchingService, times(1)).findMatchingUsers(any(BloodRequest.class), eq(user));
    }

    @Test
    void cancelRequest_WhenRequestExistsAndIsOpen_UpdateStatusToCancelled() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest bloodRequest = createValidBloodRequest(user);
        bloodRequest.setId(1L);
        DonationOffer offer = createValidDonationOffer(user, bloodRequest);
        offer.setId(1L);
        bloodRequest.setDonationOffers(List.of(offer));

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(bloodRequestRepository.findById(bloodRequest.getId())).thenReturn(Optional.of(bloodRequest));
        when(bloodRequestRepository.save(any(BloodRequest.class))).thenReturn(bloodRequest);

        BloodRequestResponseDTO response = bloodRequestService.cancelRequest(bloodRequest.getId(), user.getEmail());

        assertNotNull(response);
        assertEquals(RequestStatus.CANCELLED, response.status());
        assertEquals(OfferStatus.CANCELLED, bloodRequest.getDonationOffers().get(0).getStatus());

        verify(bloodRequestRepository, times(1)).save(any(BloodRequest.class));
    }

    @Test
    void cancelRequest_WhenRequestDoesNotExist_ThrowsException() {
        User user = createValidUser();
        user.setId(1L);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(bloodRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bloodRequestService.cancelRequest(1L, user.getEmail());
        });
    }

    @Test
    void cancelRequest_WhenRequestIsNotOpen_ThrowsException() {
        User user = createValidUser();
        user.setId(1L);
        BloodRequest bloodRequest = createValidBloodRequest(user);
        bloodRequest.setId(1L);
        bloodRequest.setStatus(RequestStatus.FULFILLED);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(bloodRequestRepository.findById(bloodRequest.getId())).thenReturn(Optional.of(bloodRequest));

        assertThrows(RequestAlreadyFulFilledException.class, () -> {
            bloodRequestService.cancelRequest(bloodRequest.getId(), user.getEmail());
        });
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
                .offeredAt(LocalDateTime.now())
                .build();
    }

    private BloodRequestDTO createValidBloodRequestDTO() {
        return new BloodRequestDTO(
                BloodGroup.O_POS,
                "Bengaluru",
                UrgencyLevel.NORMAL,
                "Urgent requirement for surgery"
        );
    }
}
