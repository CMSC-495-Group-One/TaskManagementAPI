package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.dto.UserDto;
import com.group1.taskmanagement.service.TaskService;
import com.group1.taskmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, taskService);
    }

    @Test
    void getAllUsers() {
        // Arrange
        UserDto user1 = new UserDto();
        UserDto user2 = new UserDto();

        List<UserDto> users = Arrays.asList(user1, user2);
        when(userService.findAll()).thenReturn(users);

        // Act
        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getUser() {
        // Arrange
        Long userId = 1L;
        UserDto userDto = new UserDto();
        when(userService.findUserById(userId)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.getUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void updateUser() {
        // Arrange
        Long userId = 1L;
        UserDto existingUser = new UserDto();
        UserDto updatedUser = new UserDto();

        when(userService.updateUser(userId, existingUser)).thenReturn(updatedUser);

        // Act
        ResponseEntity<UserDto> response = userController.updateUser(userId, existingUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }


    @Test
    void deleteUser() {
        // Arrange
        Long userId = 1L;

        doNothing().when(userService).deleteById(userId);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    void getUserTasks() {
        // Arrange
        Long userId = 1L;
        TaskDto task1 = new TaskDto();
        TaskDto task2 = new TaskDto();
        List<TaskDto> tasks = Arrays.asList(task1, task2);

        when(taskService.findAllByUserId(userId)).thenReturn(tasks);

        // Act
        ResponseEntity<List<TaskDto>> response = userController.getUserTasks(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tasks.size(), response.getBody().size());
        assertEquals(task1, response.getBody().get(0));
        assertEquals(task2, response.getBody().get(1));
    }


    @Test
    void getUserRoles() {
        // Arrange
        Long userId = 1L;
        List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER");

        when(userService.findUserRoles(userId)).thenReturn(roles);

        // Act
        ResponseEntity<List<String>> response = userController.getUserRoles(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles.size(), response.getBody().size());
        assertEquals(roles.get(0), response.getBody().get(0));
        assertEquals(roles.get(1), response.getBody().get(1));
    }

}