package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.model.RoleName;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.RoleRepository;
import com.group1.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);
        roleService = new RoleService(roleRepository, userRepository);
    }
    @Test
    void findAll() {
        Role role1 = new Role();
        Role role2 = new Role();
        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));

        List<RoleDto> result = roleService.findAll();

        verify(roleRepository).findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void findByRoleId() {
        Long roleId = 1L;
        Role role = new Role();
        role.setRoleId(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        RoleDto result = roleService.findByRoleId(roleId);

        verify(roleRepository).findById(roleId);
        assertThat(result.getRoleId()).isEqualTo(role.getRoleId());
    }

    @Test
    void createRole() {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(RoleName.ADMIN);

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);

        roleService.createRole(roleDto);

        verify(roleRepository).save(roleCaptor.capture());
        Role capturedRole = roleCaptor.getValue();

        assertEquals(RoleName.ADMIN, capturedRole.getName());
        // Add additional assertions for other attributes if necessary
    }

    @Test
    void updateRole() {
    }

    @Test
    void deleteById() {
        Long roleId = 1L;

        Role roleToDelete = new Role();
        roleToDelete.setRoleId(roleId);

        User userWithRole = new User();
        userWithRole.setRoles(new ArrayList<>(Arrays.asList(roleToDelete)));

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleToDelete));
        when(userRepository.findAllByRoles_roleId(roleId)).thenReturn(Collections.singletonList(userWithRole));

        ResponseEntity<Void> response = roleService.deleteById(roleId);

        verify(roleRepository).deleteById(eq(roleId));

        // Capture the argument passed to userRepository.save
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        // Check that the role was removed from the user's roles
        User savedUser = userCaptor.getValue();
        assertTrue(savedUser.getRoles().isEmpty());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}