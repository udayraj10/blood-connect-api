package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.UserResponseDTO;
import com.uday.blood_connect.dto.response.UserStatsDTO;;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import com.uday.blood_connect.exception.ResourceEmptyException;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.repository.BloodRequestRepository;
import com.uday.blood_connect.repository.DonationOfferRepository;
import com.uday.blood_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DonationOfferRepository donationOfferRepository;

    @Mock
    private BloodRequestRepository bloodRequestRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserStats_WhenUserExists_ReturnAggregatedCounts() {
        User user = createValidUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        when(donationOfferRepository.countByDonorId(user.getId())).thenReturn(10L);
        when(donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.PENDING)).thenReturn(2L);
        when(donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.ACCEPTED)).thenReturn(3L);
        when(donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.COMPLETED)).thenReturn(4L);
        when(donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.DECLINED)).thenReturn(1L);

        when(bloodRequestRepository.countByRequesterId(user.getId())).thenReturn(5L);
        when(bloodRequestRepository.countByRequesterIdAndStatus(user.getId(), RequestStatus.OPEN)).thenReturn(1L);
        when(bloodRequestRepository.countByRequesterIdAndStatus(user.getId(), RequestStatus.FULFILLED)).thenReturn(3L);
        when(bloodRequestRepository.countByRequesterIdAndStatus(user.getId(), RequestStatus.CANCELLED)).thenReturn(1L);

        UserStatsDTO stats = userService.userStats(user.getEmail());

        assertNotNull(stats);
        assertEquals(10L, stats.totalDonations());
        assertEquals(5L, stats.totalRequestsMade());
    }

    @Test
    void getUserStats_WhenUserDoesNotExist_ThenThrowException() {
        when(userRepository.findByEmail("uday@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.userStats("uday@gmail.com"));
    }

    @Test
    void getUsersBySearch_WhenQueryIsFullName_ReturnUsers() {
        User user1 = createValidUser();
        User user2 = createValidUser();
        user2.setEmail("raju@gmail.com");
        user2.setFullName("Raju");

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.searchByFullNameOrCity("Raju", user1.getId(), PageRequest.of(0, 5, Sort.by("id").ascending())))
                .thenReturn(new PageImpl<>(List.of(user2)));

        List<UserResponseDTO> users = userService.getUsersBySearch(user1.getEmail(), "Raju", 0, 5).getContent();

        assertEquals(1, users.size());
        assertEquals("Raju", users.get(0).fullName());

        verify(userRepository, times(1)).searchByFullNameOrCity("Raju", user1.getId(), PageRequest.of(0, 5, Sort.by("id").ascending()));
    }

    @Test
    void getUsersBySearch_WhenQueryIsCity_ReturnUsers() {
        User user1 = createValidUser();
        User user2 = createValidUser();
        user2.setEmail("raju@gmail.com");
        user2.setFullName("Raju");

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.searchByFullNameOrCity("Bengaluru", user1.getId(), PageRequest.of(0, 5, Sort.by("id").ascending())))
                .thenReturn(new PageImpl<>(List.of(user2)));

        List<UserResponseDTO> users = userService.getUsersBySearch(user1.getEmail(), "Bengaluru", 0, 5).getContent();

        assertEquals(1, users.size());
        assertEquals("Bengaluru", users.get(0).city());

        verify(userRepository, times(1)).searchByFullNameOrCity("Bengaluru", user1.getId(), PageRequest.of(0, 5, Sort.by("id").ascending()));
    }

    @Test
    void getUsersBySearch_WhenQueryIsBloodGroup_ReturnUsers() {
        User user1 = createValidUser();
        User user2 = createValidUser();
        user2.setEmail("raju@gmail.com");
        user2.setFullName("Raju");

        List<BloodGroup> targetGroups = BloodGroup.resolveMatchingGroups("O");

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.searchByBloodGroup(targetGroups, user1.getId(), PageRequest.of(0, 5,
                Sort.by("id").ascending())))
                .thenReturn(new PageImpl<>(List.of(user2)));

        List<UserResponseDTO> users = userService.getUsersBySearch(user1.getEmail(), "O", 0, 5).getContent();

        assertEquals(1, users.size());
        assertEquals(BloodGroup.O_POS, users.get(0).bloodGroup());

        verify(userRepository, times(1)).searchByBloodGroup(targetGroups, user1.getId(), PageRequest.of(0, 5,
                Sort.by("id").ascending()));
    }

    @Test
    void getUsersBySearch_WhenUserNotFound_ThenThrowException() {
        User user = createValidUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.searchByFullNameOrCity("Raju", user.getId(), PageRequest.of(0, 5, Sort.by("id").ascending())))
                .thenReturn(Page.empty());

        assertThrows(ResourceEmptyException.class, () -> userService.getUsersBySearch(user.getEmail(), "Raju", 0, 5));

        verify(userRepository, times(1)).searchByFullNameOrCity("Raju", user.getId(), PageRequest.of(0, 5, Sort.by("id").ascending()));
    }

    @Test
    void getUsersBySearch_WhenQueryIsEmpty_ReturnEmptyPage() {
        List<UserResponseDTO> users = userService.getUsersBySearch("uday@gmail.com", "", 0, 5).getContent();

        assertTrue(users.isEmpty());
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
}
