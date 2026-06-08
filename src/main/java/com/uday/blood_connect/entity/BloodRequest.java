package com.uday.blood_connect.entity;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.OfferStatus;
import com.uday.blood_connect.enums.UrgencyLevel;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.exception.RequestAlreadyFulFilledException;
import com.uday.blood_connect.exception.UnauthorizedActionExcepition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blood_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Requester ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @NotNull(message = "Blood group is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String city;

    @NotNull(message = "Urgency level is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgencyLevel;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Size(max = 500, message = "Message must not exceed 500 characters")
    @Column(length = 500)
    private String message;

    @OneToMany(mappedBy = "bloodRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonationOffer> donationOffers = new ArrayList<>();

    @NotNull(message = "Created at timestamp is required")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void verifyOwner(User user) {
        if (!this.getRequester().getId().equals(user.getId())) {
            throw new UnauthorizedActionExcepition(
                    "You are not authorized to perform this action");
        }
    }

    public void cancelRequest() {
        ensureOpenStatus();

        this.setStatus(RequestStatus.CANCELLED);
        this.donationOffers.forEach(o -> o.setStatus(OfferStatus.CANCELLED));
    }

    public void ensureOpenStatus() {
        if (this.getStatus() != RequestStatus.OPEN) {
            throw new RequestAlreadyFulFilledException(
                    "Cannot perform this action. Only open requests can be modified."
            );
        }
    }

    public void acceptRequest(DonationOffer donationOffer) {
        ensureOpenStatus();

        if (donationOffer.getStatus() != OfferStatus.PENDING) {
            throw new RequestAlreadyFulFilledException(
                    "Only pending offers can be accepted."
            );
        }

        this.donationOffers.stream()
                .filter(offer -> offer.getStatus() == OfferStatus.ACCEPTED)
                .findFirst()
                .ifPresent(offer -> {
                    throw new RequestAlreadyFulFilledException(
                            "Blood request already accepted by another donor."
                    );
                });

        donationOffer.accept();
    }
}