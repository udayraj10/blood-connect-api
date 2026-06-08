package com.uday.blood_connect.controller;

import com.uday.blood_connect.dto.response.ApiResponse;
import com.uday.blood_connect.dto.response.UserResponseDTO;
import com.uday.blood_connect.dto.response.UserStatsDTO;
import com.uday.blood_connect.dto.request.AvailabilityDTO;
import com.uday.blood_connect.dto.request.PasswordDTO;
import com.uday.blood_connect.dto.request.UpdateUserDTO;
import com.uday.blood_connect.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User Controller", description = "Endpoints for managing user profiles and settings")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO.Summary>> getUserDetails(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully",
                userService.getUserDetails(userDetails.getUsername())));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO.Summary>> updateUserDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(ApiResponse.success("User details updated successfully",
                userService.updateUserDetails(userDetails.getUsername(), updateUserDTO)));
    }

    @PatchMapping("/me/availability")
    public ResponseEntity<ApiResponse<UserResponseDTO.Summary>> updateUserAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AvailabilityDTO availabilityDTO) {
        return ResponseEntity.ok(ApiResponse.success("User availability updated successfully",
                userService.updateUserAvailability(userDetails.getUsername(), availabilityDTO)));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PasswordDTO passwordDTO) {

        userService.changePassword(userDetails.getUsername(), passwordDTO);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @PatchMapping("/me/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@AuthenticationPrincipal UserDetails userDetails) {

        userService.deactivateUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/me/stats")
    public ResponseEntity<ApiResponse<UserStatsDTO>> getUserStats(@AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.success("User stats retrieved successfully",
                userService.userStats(userDetails.getUsername())));
    }
}
