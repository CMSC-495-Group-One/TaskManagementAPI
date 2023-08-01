package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.model.Task;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.TaskRepository;
import com.group1.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    private TaskService taskService;
    private User user;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, userRepository);
        user = new User();
        user.setUserId(1L);
    }

    @Test
    void findAll() {
        Task task = new Task();
        task.setUser(user);
        task.setId(1L);

        when(taskRepository.findAll()).thenReturn(List.of(task));
        List<TaskDto> result = taskService.findAll();
        assertFalse(result.isEmpty());
        verify(taskRepository).findAll();
    }

    @Test
    void findTaskById() {
        Task task = new Task();
        task.setUser(user);
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        TaskDto result = taskService.findTaskById(1L);
        verify(taskRepository).findById(1L);

        assertThat(result.getId()).isEqualTo(task.getId());
        assertThat(result.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void createTask() {
        
    }

    @Test
    void updateTask() {
    }

    @Test
    void findAllByUserId() {
    }

    @Test
    void deleteById() {
    }
}
