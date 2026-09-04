package com.uday.blood_connect.repository;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void countByIsActive_WhenActiveUsers_ThenReturnsActiveUserCount() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();

        long count = userRepository.countByIsActive(true);

        assertEquals(1, count);
    }

    @Test
    void countByIsActive_WhenInactiveUsers_ThenReturnsInactiveUserCount() {
        User user = createValidUser();
        user.setIsActive(false);
        entityManager.persistAndFlush(user);
        entityManager.clear();

        long count = userRepository.countByIsActive(false);

        assertEquals(1, count);
    }

    @Test
    void countByIsAvailable_WhenAvailableUsers_ThenReturnsAvailableUserCount() {
        User user = createValidUser();
        user.setIsAvailable(true);
        entityManager.persistAndFlush(user);
        entityManager.clear();

        long count = userRepository.countByIsAvailable(true);

        assertEquals(1, count);
    }

    @Test
    void countByIsAvailable_WhenUnavailableUsers_ThenReturnsUnavailableUserCount() {
        User user = createValidUser();
        user.setIsAvailable(false);
        entityManager.persistAndFlush(user);
        entityManager.clear();

        long count = userRepository.countByIsAvailable(false);

        assertEquals(1, count);
    }

    @Test
    void findByBloodGroupAndCityAndIsAvailable_WhenMatchingCriteria_ThenReturnsUsers() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();

        List<User> users = userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(BloodGroup.O_POS, "Bengaluru", true);

        assertEquals(1, users.size());
    }

    @Test
    void findByBloodGroupAndCityAndIsAvailable_WhenDifferentBloodGroup_ThenReturnsEmpty() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();

        List<User> users = userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(BloodGroup.A_POS, "Bengaluru", true);

        assertEquals(0, users.size());
    }

    @Test
    void findByBloodGroupAndCityAndIsAvailable_WhenUnavailableUser_ThenReturnsEmpty() {
        User user = createValidUser();
        user.setIsAvailable(false);
        entityManager.persistAndFlush(user);
        entityManager.clear();

        List<User> users = userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(BloodGroup.O_POS, "Bengaluru", true);

        assertEquals(0, users.size());
    }

    @Test
    void findByBloodGroupAndCityAndIsAvailable_WhenDifferentCity_ThenReturnsEmpty() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();

        List<User> users = userRepository.findByBloodGroupAndCityIgnoreCaseAndIsAvailable(BloodGroup.O_POS, "Chennai", true);

        assertEquals(0, users.size());
    }

    @Test
    void searchByFullNameOrCity_WhenMatchingFullName_ThenReturnsUsers() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("Uday Kumar", 99L, pageable);

        assertEquals(1, users.getContent().size());
        assertEquals("Uday Kumar", users.getContent().get(0).getFullName());
    }

    @Test
    void searchByFullNameOrCity_WhenMatchingCity_ThenReturnsUsers() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("Bengaluru", 99L, pageable);

        assertEquals(1, users.getContent().size());
        assertEquals("Bengaluru", users.getContent().get(0).getCity());
    }

    @Test
    void searchByFullNameOrCity_WhenDifferentCaseQuery_ThenReturnsUsers() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("bengaluru", 99L, pageable);

        assertEquals(1, users.getContent().size());
        assertEquals("Bengaluru", users.getContent().get(0).getCity());
    }

    @Test
    void searchByFullNameOrCity_WhenNoMatchingFullNameOrCity_ThenReturnsEmptyPage() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("Chennai", 99L, pageable);

        assertEquals(0, users.getContent().size());
    }

    @Test
    void searchByFullNameOrCity_WhenCurrentUserId_ThenExcludesCurrentUser() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("Bengaluru", user.getId(), pageable);

        assertEquals(0, users.getContent().size());
    }

    @Test
    void searchByFullNameOrCity_WhenQueryMatchingStartOfName_ThenReturnsUser() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("Uday", 99L, pageable);

        assertEquals(1, users.getContent().size());
        assertEquals("Uday Kumar", users.getContent().get(0).getFullName());
    }

    @Test
    void searchByFullNameOrCity_WhenQueryMatchingOnlyMiddleOfName_ThenReturnsEmptyPage() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByFullNameOrCity("day", 99L, pageable);

        assertEquals(0, users.getContent().size());
    }

    @Test
    void searchByBloodGroup_WhenMatchingBloodGroup_ThenReturnsUsers() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByBloodGroup(List.of(BloodGroup.O_POS), 999L, pageable);

        assertEquals(1, users.getContent().size());
        assertEquals(user.getId(), users.getContent().get(0).getId());
        assertEquals(BloodGroup.O_POS, users.getContent().get(0).getBloodGroup());
    }

    @Test
    void searchByBloodGroup_WhenMultipleBloodGroups_ThenReturnsMatchingUsers() {
        User user1 = createValidUser();
        entityManager.persistAndFlush(user1);

        User user2 = createValidUser();
        user2.setFullName("Raju");
        user2.setEmail("raju@gmail.com");
        user2.setBloodGroup(BloodGroup.A_POS);
        entityManager.persistAndFlush(user2);
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByBloodGroup(List.of(BloodGroup.O_POS, BloodGroup.A_POS), 999L, pageable);

        assertEquals(2, users.getContent().size());
        assertEquals(List.of(BloodGroup.O_POS, BloodGroup.A_POS), users.getContent().stream().map(User::getBloodGroup).toList());
    }

    @Test
    void searchByBloodGroup_WhenDifferentBloodGroup_ThenReturnsEmptyPage() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByBloodGroup(List.of(BloodGroup.B_POS), 999L, pageable);

        assertEquals(0, users.getContent().size());
    }

    @Test
    void searchByBloodGroup_WhenCurrentUser_ThenExcludesCurrentUser() {
        User user1 = createValidUser();
        entityManager.persistAndFlush(user1);

        User user2 = createValidUser();
        user2.setFullName("Raju");
        user2.setEmail("raju@gmail.com");
        entityManager.persistAndFlush(user2);
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = userRepository.searchByBloodGroup(List.of(BloodGroup.O_POS), user1.getId(), pageable);

        assertEquals(1, users.getContent().size());
        assertEquals("Raju", users.getContent().get(0).getFullName());
        assertEquals(BloodGroup.O_POS, users.getContent().get(0).getBloodGroup());
    }

    @Test
    void countByAge_WhenUsersWithDifferentAges_ThenReturnsCountForEachAge() {
        User user1 = createValidUser();
        entityManager.persistAndFlush(user1);

        User user2 = createValidUser();
        user2.setFullName("Raju");
        user2.setEmail("raju@gmail.com");
        user2.setAge(30);
        entityManager.persistAndFlush(user2);
        entityManager.clear();

        List<AgeCount> ageCounts = userRepository.countByAge();

        assertEquals(2, ageCounts.size());
        assertEquals(1, ageCounts.get(0).getCount());
        assertEquals(1, ageCounts.get(1).getCount());
    }

    @Test
    void countByAge_WhenMultipleUsersExists_ThenReturnsCorrectCounts() {
        User user1 = createValidUser();
        entityManager.persistAndFlush(user1);

        User user2 = createValidUser();
        user2.setFullName("Raju");
        user2.setEmail("raju@gmail.com");
        entityManager.persistAndFlush(user2);
        entityManager.clear();

        List<AgeCount> ageCounts = userRepository.countByAge();

        assertEquals(1, ageCounts.size());
        assertEquals(2, ageCounts.get(0).getCount());
    }

    @Test
    void countByAge_WhenNoUsersExist_ThenReturnsEmptyList() {
        List<AgeCount> ageCounts = userRepository.countByAge();

        assertTrue(ageCounts.isEmpty());
    }

    @Test
    void delete_WhenUserExists_CascadesToRelatedEntities() {
        User user = createValidUser();
        User savedUser = entityManager.persistAndFlush(user);
        Long userId = savedUser.getId();
        Long bloodRequestId = savedUser.getBloodRequests().get(0).getId();
        Long donationOfferId = savedUser.getDonationOffers().get(0).getId();
        entityManager.clear();

        userRepository.deleteById(userId);
        userRepository.flush();

        assertTrue(userRepository.findById(userId).isEmpty());
        assertNull(entityManager.find(BloodRequest.class, bloodRequestId));
        assertNull(entityManager.find(DonationOffer.class, donationOfferId));
    }

    @Test
    void deactivate_WhenUserExists_ThenSetIsActiveToFalse() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();

        user.deactivate();
        User updatedUser = userRepository.saveAndFlush(user);

        assertFalse(updatedUser.getIsActive());
    }

    @Test
    void activate_WhenUserExists_ThenSetIsActiveToTrue() {
        User user = createValidUser();
        entityManager.persistAndFlush(user);
        entityManager.clear();

        user.activate();
        User updatedUser = userRepository.saveAndFlush(user);

        assertTrue(updatedUser.getIsActive());
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
                .build();
    }
}