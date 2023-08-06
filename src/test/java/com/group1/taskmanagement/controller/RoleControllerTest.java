package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.service.RoleService;
import com.group1.taskmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    private RoleController roleController;

    @BeforeEach
    void setUp() {
        roleController = new RoleController(roleService, userService);
    }

    @Test
    void getAllRoles() {
        // Arrange
        RoleDto role1 = new RoleDto();
        RoleDto role2 = new RoleDto();
        List<RoleDto> roles = Arrays.asList(role1, role2);

        when(roleService.findAll()).thenReturn(roles);

        // Act
        ResponseEntity<List<RoleDto>> response = roleController.getAllRoles();

        // Assert
        assertEquals(2, response.getBody().size());
        assertEquals(role1, response.getBody().get(0));
        assertEquals(role2, response.getBody().get(1));
    }

    @Test
    void createRole() {
        // Arrange
        RoleDto roleDto = new RoleDto();
        when(userService.hasAdminRole()).thenReturn(true);
        doNothing().when(roleService).createRole(roleDto);

        // Act
        ResponseEntity<String> response = roleController.createRole(roleDto);

        // Assert
        assertEquals("Role created!", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roleService).createRole(roleDto);
    }

    @Test
    void getRole() {
        // Arrange
        Long roleId = 1L;
        RoleDto roleDto = new RoleDto();
        when(roleService.findByRoleId(roleId)).thenReturn(roleDto);

        // Act
        ResponseEntity<RoleDto> response = roleController.getRole(roleId);

        // Assert
        assertEquals(roleDto, response.getBody());
    }

    @Test
    void updateRole() throws IllegalAccessException {
        // Arrange
        Long roleId = 1L;
        RoleDto existingRoleDto = new RoleDto();
        RoleDto updatedRoleDto = new RoleDto();

        when(userService.hasAdminRole()).thenReturn(true);
        when(roleService.updateRole(roleId, existingRoleDto)).thenReturn(updatedRoleDto);

        // Act
        ResponseEntity<RoleDto> response = roleController.updateRole(existingRoleDto, roleId);

        // Assert
        assertEquals(updatedRoleDto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roleService).updateRole(roleId, existingRoleDto);
    }


    @Test
    void deleteRole() {
        // Arrange
        Long roleId = 1L;

        when(userService.hasAdminRole()).thenReturn(true);
        when(roleService.deleteById(roleId)).thenReturn(ResponseEntity.noContent().build());

        // Act
        ResponseEntity<Void> response = roleController.deleteRole(roleId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roleService).deleteById(roleId);
    }
}