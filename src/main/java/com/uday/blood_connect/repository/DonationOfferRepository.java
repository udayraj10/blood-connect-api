package com.uday.blood_connect.repository;

import com.uday.blood_connect.entity.BloodRequest;
import com.uday.blood_connect.entity.DonationOffer;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationOfferRepository extends JpaRepository<DonationOffer, Long> {

    Page<DonationOffer> findByDonor(User donor, Pageable pageable);

    Page<DonationOffer> findByBloodRequestId(Long id, Pageable pageable);

    List<DonationOffer> findByBloodRequestIdAndIdNot(Long requestId, Long offerId);

    long countByDonorIdAndStatus(Long donorId, OfferStatus status);

    long countByStatus(OfferStatus status);

    Optional<DonationOffer> findByDonorAndBloodRequest(User donor, BloodRequest bloodRequest);

}
