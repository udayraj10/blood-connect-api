package com.uday.blood_connect.entity;

import com.uday.blood_connect.enums.OfferStatus;
import com.uday.blood_connect.exception.RequestAlreadyFulFilledException;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Donor ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @NotNull(message = "Blood request ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_request_id", nullable = false)
    private BloodRequest bloodRequest;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    @NotNull(message = "Offered at timestamp is required")
    @Column(nullable = false, updatable = false)
    private LocalDateTime offeredAt;

    @Column(nullable = true)
    private LocalDateTime respondedAt;

    @PrePersist
    protected void onCreate() {
        offeredAt = LocalDateTime.now();
    }

    public void verifyDonor(User user) {
        if (!this.donor.getId().equals(user.getId())) {
            throw new AccessDeniedException("You are forbidden from performing this action");
        }
    }

    public void accept() {
        this.status = OfferStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void decline() {
        if (this.status != OfferStatus.PENDING) {
            throw new RequestAlreadyFulFilledException("Only pending offers can be declined");
        }
        this.status = OfferStatus.DECLINED;
        this.respondedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != OfferStatus.ACCEPTED) {
            throw new RequestAlreadyFulFilledException("Only accepted offers can be marked as completed");
        }
        this.status = OfferStatus.COMPLETED;
        this.respondedAt = LocalDateTime.now();
    }

    public void close() {
        this.status = OfferStatus.CLOSED;
    }
}
