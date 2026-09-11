package com.uday.blood_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uday.blood_connect.dto.request.PasswordDTO;
import com.uday.blood_connect.dto.request.UpdateUserDTO;
import com.uday.blood_connect.dto.response.UserResponseDTO;
import com.uday.blood_connect.dto.response.UserStatsDTO;
import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.enums.Role;
import com.uday.blood_connect.exception.ResourceNotFoundException;
import com.uday.blood_connect.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityTestBeans.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@gmail.com")
    void getUserDetails_WhenUserExists_Return200() throws Exception {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        when(userService.getUserDetails("test@gmail.com")).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"));
    }

    @Test
    void getUserDetails_WhenNoAuth_Return401() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void getUserDetailsById_WhenUserExists_Return200() throws Exception {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        when(userService.getUserDetailsById("test@gmail.com", 1L)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"));
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void getUserDetailsById_WhenUserNotExists_Return404() throws Exception {
        when(userService.getUserDetailsById("test@gmail.com", 1L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void updateUser_WhenUserExists_Return200() throws Exception {
        UserResponseDTO userResponseDTO = createUserResponseDTO();
        UpdateUserDTO updateUserDTO = createUpdateUserDTO();

        when(userService.updateUserDetails(eq("test@gmail.com"), any(UpdateUserDTO.class)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(patch("/api/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void changePassword_WhenUserExists_Return200() throws Exception {
        PasswordDTO passwordDTO = createPasswordDTO();

        mockMvc.perform(patch("/api/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void getUserStats_WhenUserExists_Return200() throws Exception {
        UserStatsDTO userStatsDTO = createUserStatsDTO();

        when(userService.userStats("test@gmail.com")).thenReturn(userStatsDTO);

        mockMvc.perform(get("/api/users/me/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalDonations").value(5L))
                .andExpect(jsonPath("$.data.totalRequestsMade").value(10L));
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void searchByUser_WhenUserExists_Return200() throws Exception {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        List<UserResponseDTO> userList = List.of(userResponseDTO);
        Page<UserResponseDTO> page = new PageImpl<>(userList, PageRequest.of(0, 10), userList.size());

        when(userService.getUsersBySearch(eq("test@gmail.com"), eq("A"), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/users/search")
                        .param("query", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    private UserResponseDTO createUserResponseDTO() {
        return new UserResponseDTO(
                1L,
                "test",
                "test@gmail.com",
                Role.USER,
                "1234567890",
                22,
                BloodGroup.O_POS,
                "Bengaluru",
                "Whitefield",
                AccountType.INDIVIDUAL,
                true,
                LocalDate.now(),
                LocalDateTime.now()
        );
    }

    private UpdateUserDTO createUpdateUserDTO() {
        return new UpdateUserDTO(
                "test",
                "test@gmail.com",
                "1234567890",
                22,
                BloodGroup.O_POS,
                "Chennai",
                "xyzzz",
                false,
                LocalDate.now()
        );
    }

    private PasswordDTO createPasswordDTO() {
        return new PasswordDTO("000000", "000000");
    }

    private UserStatsDTO createUserStatsDTO () {
        return new UserStatsDTO(
                5L,
                2L,
                2L,
                1L,
                0L,
                LocalDate.now(),
                10L,
                2L,
                7L,
                1L
        );
    }
}
