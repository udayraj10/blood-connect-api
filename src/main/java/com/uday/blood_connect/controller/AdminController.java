package com.uday.blood_connect.controller;

import com.uday.blood_connect.dto.response.*;
import com.uday.blood_connect.service.AdminService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Admin Controller", description = "Endpoints for admin operations")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO.Details>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully",
                adminService.getAllUsers(page, size)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO.Details>> getUserById(
            @Parameter(description = "The unique ID of the system user", example = "33")
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully",
                adminService.getUserById(userId)));
    }

    @PatchMapping("/users/{userId}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(
            @Parameter(description = "The unique ID of the system user", example = "33")
            @PathVariable("userId") Long userId) {
        adminService.deactivateUser(userId);

        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }

    @PatchMapping("/users/{userId}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(
            @Parameter(description = "The unique ID of the system user", example = "33")
            @PathVariable("userId") Long userId) {

        adminService.activeUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "The unique ID of the system user", example = "33")
            @PathVariable("userId") Long userId) {

        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/blood-requests")
    public ResponseEntity<ApiResponse<Page<BloodRequestResponseDTO.Details>>> getAllBloodRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(ApiResponse.success("Blood requests retrieved successfully",
                adminService.getAllBloodRequests(page, size)));
    }

    @GetMapping("/blood-requests/{requestId}")
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO.Details>> getBloodRequestById(
            @Parameter(description = "The unique ID of the system user", example = "18")
            @PathVariable("requestId") Long requestId) {

        return ResponseEntity.ok(ApiResponse.success("Blood request retrieved successfully",
                adminService.getBloodRequestById(requestId)));
    }

    @PatchMapping("/blood-requests/{requestId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelBloodRequest(
            @Parameter(description = "The unique ID of the system user", example = "18")
            @PathVariable("requestId") Long requestId) {

        adminService.cancelBloodRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success("Cancelled blood request successfully", null));
    }

    @GetMapping("/offers")
    public ResponseEntity<ApiResponse<Page<DonationOfferResponseDTO>>> getAllOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(ApiResponse.success("Donation offers retrieved successfully",
                adminService.getAllOffers(page, size)));
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<ApiResponse<DonationOfferResponseDTO>> getOfferById(
            @Parameter(description = "The unique ID of the system user", example = "1")
            @PathVariable("offerId") Long offerId) {

        return ResponseEntity.ok(ApiResponse.success("Donation offer retrieved successfully",
                adminService.getOfferById(offerId)));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<StatsResponseDTO>> getStats() {

        return ResponseEntity.ok(ApiResponse.success("Stats retrieved successfully",
                adminService.getStats()));
    }
}
