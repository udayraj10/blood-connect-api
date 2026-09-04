package com.uday.blood_connect.repository;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BloodRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Test
    void findByIdAndRequesterId_WhenBloodRequestExists_ReturnBloodRequest() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        BloodRequest bloodRequest = createValidBloodRequest(requester);
        entityManager.persistAndFlush(bloodRequest);

        Optional<BloodRequest> result = bloodRequestRepository.findByIdAndRequesterId(bloodRequest.getId(),
                requester.getId());

        assertTrue(result.isPresent());
        assertEquals(bloodRequest.getId(), result.get().getId());
    }

    @Test
    void findByIdAndRequesterId_WhenBloodRequestDoesNotExist_ReturnsEmptyOptional() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        Optional<BloodRequest> result = bloodRequestRepository.findByIdAndRequesterId(1L, requester.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void countByRequesterIdAndStatus_WhenBloodRequestsExist_ReturnCount() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        BloodRequest bloodRequest1 = createValidBloodRequest(requester);
        BloodRequest bloodRequest2 = createValidBloodRequest(requester);
        entityManager.persistAndFlush(bloodRequest1);
        entityManager.persistAndFlush(bloodRequest2);

        long count = bloodRequestRepository.countByRequesterIdAndStatus(requester.getId(), RequestStatus.OPEN);

        assertEquals(2, count);
    }

    @Test
    void countByRequesterIdAndStatus_WhenBloodRequestsDoNotExist_ReturnZero() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        long count = bloodRequestRepository.countByRequesterIdAndStatus(requester.getId(), RequestStatus.OPEN);

        assertEquals(0, count);
    }

    @Test
    void countByRequesterId_WhenBloodRequestsExist_ReturnCount() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        BloodRequest bloodRequest1 = createValidBloodRequest(requester);
        BloodRequest bloodRequest2 = createValidBloodRequest(requester);
        entityManager.persistAndFlush(bloodRequest1);
        entityManager.persistAndFlush(bloodRequest2);

        long count = bloodRequestRepository.countByRequesterId(requester.getId());

        assertEquals(2, count);
    }

    @Test
    void countByRequesterId_WhenBloodRequestsDoNotExist_ReturnZero() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        long count = bloodRequestRepository.countByRequesterId(requester.getId());

        assertEquals(0, count);
    }

    @Test
    void countByStatus_WhenBloodRequestsExist_ReturnCount() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        BloodRequest bloodRequest1 = createValidBloodRequest(requester);
        BloodRequest bloodRequest2 = createValidBloodRequest(requester);
        entityManager.persistAndFlush(bloodRequest1);
        entityManager.persistAndFlush(bloodRequest2);

        long count = bloodRequestRepository.countByStatus(RequestStatus.OPEN);

        assertEquals(2, count);
    }

    @Test
    void countByStatus_WhenBloodRequestsDoNotExist_ReturnZero() {
        long count = bloodRequestRepository.countByStatus(RequestStatus.OPEN);

        assertEquals(0, count);
    }

    @Test
    void countByUrgencyLevel_WhenBloodRequestsExist_ReturnCount() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        BloodRequest bloodRequest1 = createValidBloodRequest(requester);
        BloodRequest bloodRequest2 = createValidBloodRequest(requester);
        entityManager.persistAndFlush(bloodRequest1);
        entityManager.persistAndFlush(bloodRequest2);

        long count = bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.NORMAL);

        assertEquals(2, count);
    }

    @Test
    void countByUrgencyLevel_WhenBloodRequestsDoNotExist_ReturnZero() {
        long count = bloodRequestRepository.countByUrgencyLevel(UrgencyLevel.NORMAL);

        assertEquals(0, count);
    }

    @Test
    void countByBloodGroup_WhenBloodRequestsExist_ReturnCount() {
        User requester = createValidUser();
        entityManager.persistAndFlush(requester);

        BloodRequest bloodRequest1 = createValidBloodRequest(requester);
        BloodRequest bloodRequest2 = createValidBloodRequest(requester);
        entityManager.persistAndFlush(bloodRequest1);
        entityManager.persistAndFlush(bloodRequest2);

        List<BloodGroupCount> result = bloodRequestRepository.countByBloodGroup();

        assertEquals(1, result.size());
        assertEquals(BloodGroup.A_POS, result.get(0).getBloodGroup());
        assertEquals(2, result.get(0).getCount());
    }

    @Test
    void countByBloodGroup_WhenBloodRequestsDoNotExist_ReturnEmptyList() {
        List<BloodGroupCount> result = bloodRequestRepository.countByBloodGroup();

        assertEquals(0, result.size());
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
