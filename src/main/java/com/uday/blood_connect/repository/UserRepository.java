package com.uday.blood_connect.repository;

import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.BloodGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')) " +
            "AND u.role = 'USER' " +
            "AND u.id != :currentUserId")
    Page<User> searchByUserFullName(
            @Param("fullName") String fullName,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );

    List<User> findByBloodGroup(BloodGroup bloodGroup);

    List<User> findByBloodGroupAndCityAndIsAvailable(BloodGroup bloodGroup, String city, boolean isAvailable);

    List<User> findByIsAvailableTrue();

    List<User> findByBloodGroupAndIsAvailableTrue(BloodGroup bloodGroup);

    boolean existsByEmail(String email);

    @Query("SELECT u.age AS age, COUNT(u.age) AS count FROM User u GROUP BY u.age")
    List<AgeCount> countByAge();
}
