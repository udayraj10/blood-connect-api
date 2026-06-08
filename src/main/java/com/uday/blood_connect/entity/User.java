package com.uday.blood_connect.entity;

import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false, length = 255)
    private String password;

    @NotNull(message = "Age is required")
    @Column(nullable = false)
    @Positive(message = "Age must be a positive number")
    private Integer age;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be between 10 and 20 digits")
    @Column(nullable = false, length = 20)
    private String phone;

    @NotNull(message = "Blood group is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String address;

    @NotNull(message = "Account type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(nullable = true)
    private LocalDate lastDonationDate;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloodRequest> bloodRequests = new ArrayList<>();

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonationOffer> donationOffers = new ArrayList<>();

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
