package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.StatsResponseDTO;
import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import com.uday.blood_connect.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DonationOfferRepository donationOfferRepository;

    @Mock
    private BloodRequestRepository bloodRequestRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getStats_ReturnsAggregatedMetricsCorrectly() {
        AgeCount age25 = mock(AgeCount.class);
        when(age25.getAge()).thenReturn(25);
        when(age25.getCount()).thenReturn(10L);

        BloodGroupCount oPos = mock(BloodGroupCount.class);
        when(oPos.getBloodGroup()).thenReturn(BloodGroup.O_POS);
        when(oPos.getCount()).thenReturn(5L);

        when(userRepository.countByAge()).thenReturn(List.of(age25));
        when(bloodRequestRepository.countByBloodGroup()).thenReturn(List.of(oPos));

        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countByIsActive(true)).thenReturn(80L);
        when(userRepository.countByIsActive(false)).thenReturn(20L);
        when(userRepository.countByIsAvailable(true)).thenReturn(50L);

        when(bloodRequestRepository.count()).thenReturn(20L);
        when(donationOfferRepository.count()).thenReturn(30L);

        when(bloodRequestRepository.countByStatus(RequestStatus.OPEN)).thenReturn(15L);
        when(bloodRequestRepository.countByStatus(RequestStatus.FULFILLED)).thenReturn(20L);
        when(bloodRequestRepository.countByStatus(RequestStatus.CANCELLED)).thenReturn(5L);

        when(bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.NORMAL)).thenReturn(25L);
        when(bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.URGENT)).thenReturn(10L);
        when(bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.CRITICAL)).thenReturn(5L);

        StatsResponseDTO result = adminService.getStats();

        assertNotNull(result);
        assertEquals(100L, result.totalUsers());
        assertEquals(80L, result.activeUsers());
        assertEquals(20L, result.inActiveUsers());
        assertEquals(50L, result.availableUsers());
        assertEquals(20L, result.totalBloodRequests());
        assertEquals(30L, result.totalDonationOffers());
        assertEquals(15L, result.openRequests());
        assertEquals(20L, result.fulfilledRequests());
        assertEquals(5L, result.cancelledRequests());
        assertEquals(25L, result.normalRequests());
        assertEquals(10L, result.urgentRequests());
        assertEquals(5L, result.criticalRequests());
    }
}
