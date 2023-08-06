package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.UserDto;
import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.model.RoleName;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.UserRepository;
import com.group1.taskmanagement.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        SecurityContextHolder.clearContext();
    }
    @Test
    void getCurrentUser() {
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUserId()).thenReturn(1L);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getCurrentUser());
    }

    @Test
    void testHasAdminRole_UserHasAdminRole() {
        Role adminRole = new Role(null, RoleName.ADMIN, null);
        User adminUser = new User();
        adminUser.setRoles(Arrays.asList(adminRole));
        CustomUserDetails customUserDetails = new CustomUserDetails(adminUser, null);

        when(userRepository.findById(customUserDetails.getUserId())).thenReturn(java.util.Optional.of(adminUser));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customUserDetails, null)
        );

        assertTrue(userService.hasAdminRole());
    }

    @Test
    void findAll() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        assertFalse(userService.findAll().isEmpty());
    }

    @Test
    void findUserById() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.findUserById(1L);

        assertNotNull(result);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        User existingUser = new User();
        UserDto userDto = new UserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        assertDoesNotThrow(() -> userService.updateUser(userId, userDto));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void deleteById() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findUserRoles() {
        Role role1 = new Role(1L, RoleName.ADMIN, null);
        Role role2 = new Role(2L, RoleName.USER, null);
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setRoles(Arrays.asList(role1, role2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        List<String> userRoles = userService.findUserRoles(1L);

        assertEquals(2, userRoles.size());
        assertEquals("ADMIN", userRoles.get(0));
        assertEquals("USER", userRoles.get(1));
    }
}