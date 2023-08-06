package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(taskService);
    }

    @Test
    void getAllTasks() {
        // Arrange
        TaskDto task1 = new TaskDto();
        TaskDto task2 = new TaskDto();
        List<TaskDto> tasks = Arrays.asList(task1, task2);

        when(taskService.findAll()).thenReturn(tasks);

        // Act
        ResponseEntity<List<TaskDto>> response = taskController.getAllTasks();

        // Assert
        assertEquals(2, response.getBody().size());
        assertEquals(task1, response.getBody().get(0));
        assertEquals(task2, response.getBody().get(1));
    }

    @Test
    void createTask() {
        // Arrange
        TaskDto taskDto = new TaskDto();
        doNothing().when(taskService).createTask(taskDto);

        // Act
        ResponseEntity<String> response = taskController.createTask(taskDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task created", response.getBody());
    }


    @Test
    void getTask() {
        // Arrange
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();

        when(taskService.findTaskById(taskId)).thenReturn(taskDto);

        // Act
        ResponseEntity<TaskDto> response = taskController.getTask(taskId);

        // Assert
        assertEquals(taskDto, response.getBody());
    }

    @Test
    void updateTask() {
        // Arrange
        Long id = 1L;
        Long userId = 42L;
        TaskDto existingTask = new TaskDto();
        existingTask.setUserId(userId);
        TaskDto updateTask = new TaskDto();

        when(taskService.findTaskById(id)).thenReturn(existingTask);
        when(taskService.updateTask(eq(id), anyLong(), eq(updateTask))).thenReturn(updateTask);

        // Act
        ResponseEntity<TaskDto> response = taskController.updateTask(id, updateTask);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateTask, response.getBody());
    }

    @Test
    void deleteTask() {
        // Arrange
        Long id = 1L;
        Long userId = 42L;
        TaskDto existingTask = new TaskDto();
        existingTask.setUserId(userId);

        when(taskService.findTaskById(id)).thenReturn(existingTask);
        doNothing().when(taskService).deleteById(id, userId);

        // Act
        ResponseEntity<Void> response = taskController.deleteTask(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}