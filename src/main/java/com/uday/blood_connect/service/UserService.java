package com.uday.blood_connect.service;

import com.uday.blood_connect.dto.response.UserResponseDTO;
import com.uday.blood_connect.dto.response.UserStatsDTO;
import com.uday.blood_connect.dto.request.PasswordDTO;
import com.uday.blood_connect.dto.request.UpdateUserDTO;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.OfferStatus;
import com.uday.blood_connect.enums.RequestStatus;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.exception.UserAlreadyExistsException;
import com.uday.blood_connect.repository.BloodRequestRepository;
import com.uday.blood_connect.repository.DonationOfferRepository;
import com.uday.blood_connect.repository.UserRepository;
import com.uday.blood_connect.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final DonationOfferRepository donationOfferRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponseDTO getUserDetails(String email) {
        User user = getUserByEmail(email);

        return mapToDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUserDetails(String email, UpdateUserDTO updateUserDTO) {
        User user = getUserByEmail(email);

        if (updateUserDTO.fullName() != null) {
            user.setFullName(StringUtils.capitalizeFirstOnly(updateUserDTO.fullName()));
        }
        if (updateUserDTO.email() != null) {
            if (!user.getEmail().equals(updateUserDTO.email()) && userRepository.existsByEmail(updateUserDTO.email())) {
                throw new UserAlreadyExistsException("Email already exists: " + updateUserDTO.email());
            }
            user.setEmail(updateUserDTO.email());
        }
        if (updateUserDTO.phone() != null) {
            user.setPhone(updateUserDTO.phone());
        }
        if (updateUserDTO.age() != null) {
            user.setAge(updateUserDTO.age());
        }
        if (updateUserDTO.bloodGroup() != null) {
            user.setBloodGroup(updateUserDTO.bloodGroup());
        }
        if (updateUserDTO.city() != null) {
            user.setCity(StringUtils.capitalizeFirstOnly(updateUserDTO.city()));
        }
        if (updateUserDTO.address() != null) {
            user.setAddress(StringUtils.capitalizeFirstOnly(updateUserDTO.address()));
        }
        if (updateUserDTO.isAvailable() != null) {
            user.setIsAvailable(updateUserDTO.isAvailable());
        }
        if (updateUserDTO.lastDonationDate() != null) {
            user.setLastDonationDate(updateUserDTO.lastDonationDate());
        }

        userRepository.save(user);

        return mapToDTO(user);
    }

    public void changePassword(String username, PasswordDTO passwordDTO) {
        User user = getUserByEmail(username);

        if (!passwordDTO.newPassword().equals(passwordDTO.confirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        if (user.getPassword().equals(passwordDTO.newPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as your current password.");
        }

        String hashedPassword = passwordEncoder.encode(passwordDTO.confirmPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public void deactivateUser(String email) {
        User user = getUserByEmail(email);

        user.deactivate();

        userRepository.save(user);
    }

    public UserStatsDTO userStats(String username) {
        User user = getUserByEmail(username);

        return new UserStatsDTO(
                donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.COMPLETED),
                donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.PENDING),
                donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.ACCEPTED),
                donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.COMPLETED),
                donationOfferRepository.countByDonorIdAndStatus(user.getId(), OfferStatus.DECLINED),
                user.getLastDonationDate(),
                bloodRequestRepository.countByRequesterId(user.getId()),
                bloodRequestRepository.countByRequesterIdAndStatus(user.getId(), RequestStatus.OPEN),
                bloodRequestRepository.countByRequesterIdAndStatus(user.getId(), RequestStatus.FULFILLED),
                bloodRequestRepository.countByRequesterIdAndStatus(user.getId(), RequestStatus.CANCELLED)
        );
    }

    public Page<UserResponseDTO> searchByUsername(String email, String username, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        if (username == null || username.trim().isEmpty()) {
            return Page.empty(pageable);
        }

        User user = getUserByEmail(email);

        Page<User> users = userRepository.searchByUserFullName(username, user.getId(), pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No user available with name");
        }

        return users.map(this::mapToDTO);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User data not found"));
    }

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.getAge(),
                user.getBloodGroup(),
                user.getCity(),
                user.getAddress(),
                user.getAccountType(),
                user.getIsAvailable(),
                user.getLastDonationDate(),
                user.getCreatedAt()
        );
    }
}
