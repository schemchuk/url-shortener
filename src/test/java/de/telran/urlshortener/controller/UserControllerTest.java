package de.telran.urlshortener.controller;

import de.telran.urlshortener.config.SecurityConfig;
import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.UserMapper;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.security.JwtProvider;
import de.telran.urlshortener.service.userService.UserLookupService;
import de.telran.urlshortener.service.userService.UserService;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;


@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)  // Отключает Spring Security для тестов
class UserControllerTest {
    @Captor
    private ArgumentCaptor<Role.RoleName> roleNameCaptor;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserLookupService userLookupService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RoleMapper roleMapper;

    private User user;
    @Setter
    @Getter
    private UserRequest userRequest;
    private UserResponse userResponse;
    private RoleResponse roleResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .userName("testUser")
                .email("test@example.com")
                .password("password")
                .build();

        userRequest = UserRequest.builder()
                .userName("testUser")
                .email("test@example.com")
                .password("password")
                .build();

        roleResponse = new RoleResponse(1L, "ADMIN", null);

        userResponse = UserResponse.builder()
                .id(1L)
                .userName("testUser")
                .email("test@example.com")
                .roles(Set.of(roleResponse))
                .build();
    }

    @Test
    void createUser_success() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .userName("testUser")
                .email("test@example.com")
                .roles(Set.of(roleResponse))
                .build();

        Mockito.when(userMapper.mapToUser(any(UserRequest.class))).thenReturn(user);
        Mockito.when(userService.createUser(any(), any(), any())).thenReturn(user);
        Mockito.when(userMapper.mapToUserResponse(any(User.class))).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"testUser\", \"email\": \"test@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.userName").value(userResponse.getUserName()))
                .andExpect(jsonPath("$.roles[0].roleName").value(roleResponse.getRoleName()));
    }

    @Test
    @WithMockUser(roles = "PAID")
    void updateUser_success() throws Exception {
        UserRequest updatedUserRequest = UserRequest.builder()
                .userName("updatedUser")
                .email("updated@example.com")
                .password("newpassword")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .userName("updatedUser")
                .email("updated@example.com")
                .password("newpassword")
                .build();

        UserResponse updatedUserResponse = UserResponse.builder()
                .id(1L)
                .userName("updatedUser")
                .email("updated@example.com")
                .roles(Set.of(roleResponse))  // Обновленная роль
                .build();

        Mockito.when(userMapper.mapToUser(any(UserRequest.class))).thenReturn(updatedUser);
        Mockito.when(userService.updateUser(anyLong(), any(User.class))).thenReturn(updatedUser);
        Mockito.when(userMapper.mapToUserResponse(any(User.class))).thenReturn(updatedUserResponse);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"updatedUser\",\"email\":\"updated@example.com\",\"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.userName").value("updatedUser"))
                .andExpect(jsonPath("$.roles[0].roleName").value("ADMIN"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeUserRole_success() throws Exception {
        UserResponse updatedUserResponse = UserResponse.builder()
                .id(1L)
                .userName("testUser")
                .email("test@example.com")
                .roles(Set.of(new RoleResponse(1L, "ADMIN", null)))  // Обновленная роль
                .build();

        Mockito.when(userService.changeUserRole(anyLong(), eq("ADMIN"))).thenReturn(user);
        Mockito.when(userMapper.mapToUserResponse(any(User.class))).thenReturn(updatedUserResponse);

        mockMvc.perform(patch("/users/{id}/role", 1L)
                        .param("newRole", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserResponse.getId()))
                .andExpect(jsonPath("$.userName").value(updatedUserResponse.getUserName()))
                .andExpect(jsonPath("$.roles[0].roleName").value("ADMIN"));
    }

}
