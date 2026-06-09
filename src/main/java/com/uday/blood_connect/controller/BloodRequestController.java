package com.uday.blood_connect.controller;

import com.uday.blood_connect.dto.response.ApiResponse;
import com.uday.blood_connect.dto.response.BloodRequestResponseDTO;
import com.uday.blood_connect.dto.response.MatchResultsDTO;
import com.uday.blood_connect.dto.request.BloodRequestDTO;
import com.uday.blood_connect.service.BloodRequestService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Blood Request Controller", description = "Endpoints for managing blood requests")
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO.Summary>> createBloodRequest(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody BloodRequestDTO bloodRequestDTO) {

        return ResponseEntity.ok(ApiResponse.success("Blood request created successfully",
                bloodRequestService.createBloodRequest(bloodRequestDTO, user.getUsername())));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO.Summary>> getBloodRequest(
            @Parameter(description = "The unique ID of the system user", example = "18")
            @AuthenticationPrincipal UserDetails user,
            @PathVariable("requestId") Long requestId) {

        return ResponseEntity.ok(ApiResponse.success("Blood request retrieved successfully",
                bloodRequestService.getBloodRequest(requestId, user.getUsername())));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<BloodRequestResponseDTO.Summary>>> getBloodRequests(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.success("Blood requests retrieved successfully",
                bloodRequestService.getBloodRequests(user.getUsername(), page, size)));
    }

    @GetMapping("/{requestId}/donors")
    public ResponseEntity<ApiResponse<Page<MatchResultsDTO>>> getDonorsForRequest(
            @Parameter(description = "The unique ID of the system user", example = "18")
            @PathVariable("requestId") Long requestId,
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.success("Matched donors retrieved successfully",
                bloodRequestService.getDonorsForRequest(requestId, user.getUsername(), page, size)));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO.Summary>> updateBloodRequestStatus(
            @Parameter(description = "The unique ID of the system user", example = "18")
            @PathVariable("requestId") Long requestId,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(ApiResponse.success("Blood request cancelled successfully",
                bloodRequestService.cancelRequest(requestId, user.getUsername())));
    }

}
