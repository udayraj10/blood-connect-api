package com.uday.blood_connect.repository;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    Page<BloodRequest> findByRequester(User requester, Pageable pageable);

    Optional<BloodRequest> findByIdAndRequesterId(Long id, Long requesterId);

    long countByRequesterIdAndStatus(Long requesterId, RequestStatus status);

    long countByRequesterId(Long requesterId);

    long countByStatus(RequestStatus status);

    long countByUrgencyLevel(UrgencyLevel level);

    @Query("SELECT b.bloodGroup AS bloodGroup, COUNT(b.bloodGroup) AS count " +
            "FROM BloodRequest b GROUP BY b.bloodGroup")
    List<BloodGroupCount> countByBloodGroup();

}
