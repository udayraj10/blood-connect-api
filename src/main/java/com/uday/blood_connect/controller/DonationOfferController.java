package com.uday.blood_connect.controller;

import com.uday.blood_connect.dto.response.ApiResponse;
import com.uday.blood_connect.dto.response.DonationOfferResponseDTO;
import com.uday.blood_connect.service.DonationOfferService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Donation Offer Controller", description = "Endpoints for managing donation offers")
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationOfferController {

    private final DonationOfferService donationOfferService;

    @GetMapping("/offers")
    public ResponseEntity<ApiResponse<Page<DonationOfferResponseDTO>>> getRequestOffers(
            @AuthenticationPrincipal UserDetails user,
            Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.success("Blood requests retrieved successfully",
                donationOfferService.getRequestOffers(user.getUsername(), pageable)));
    }

    @PatchMapping("/offers/{offerId}/accept")
    public ResponseEntity<ApiResponse<DonationOfferResponseDTO>> acceptOffer(
            @Parameter(description = "The unique ID of the system user", example = "16")
            @PathVariable("offerId") Long offerId,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(ApiResponse.success("Blood request accepted successfully",
                donationOfferService.acceptOffer(offerId, user.getUsername())));
    }

    @PatchMapping("/offers/{offerId}/decline")
    public ResponseEntity<ApiResponse<DonationOfferResponseDTO>> declineOffer(
            @Parameter(description = "The unique ID of the system user", example = "34")
            @PathVariable("offerId") Long offerId,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(ApiResponse.success("Blood request declined successfully",
                donationOfferService.declineOffer(offerId, user.getUsername())));
    }

    @PatchMapping("/offers/{offerId}/complete")
    public ResponseEntity<ApiResponse<DonationOfferResponseDTO>> completeOffer(
            @Parameter(description = "The unique ID of the system user", example = "16")
            @PathVariable("offerId") Long offerId,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(ApiResponse.success("Blood request completed successfully",
                donationOfferService.completeOffer(offerId, user.getUsername())));
    }
}
