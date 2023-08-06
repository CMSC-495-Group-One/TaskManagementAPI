package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.model.Task;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.TaskRepository;
import com.group1.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
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
        verify(taskRepository).findById(1L);
    }

    @Test
    void createTask() {
        Long userId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(userId);

        User user = new User();
        user.setUserId(userId);

        Task newTask = new Task();
        newTask.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        taskService.createTask(taskDto);

        verify(userRepository).findById(userId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask() {
        Long taskId = 1L;
        Long userId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(userId);

        User user = new User();
        user.setUserId(userId);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setUser(user);

        Task updatedTaskFromDto = new Task();
        updatedTaskFromDto.setUser(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        TaskDto result = taskService.updateTask(taskId, userId, taskDto);

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(userId);
        verify(taskRepository).save(existingTask);
        assertThat(result).isNotNull();
    }


    @Test
    void findAllByUserId() {
        Long userId = 1L;

        User user = new User();
        user.setUserId(userId);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setUser(user);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setUser(user);

        when(taskRepository.findByUser_userId(userId)).thenReturn(List.of(task1, task2));

        List<TaskDto> result = taskService.findAllByUserId(userId);

        verify(taskRepository).findByUser_userId(userId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(task1.getId());
        assertThat(result.get(1).getId()).isEqualTo(task2.getId());
    }


    @Test
    void deleteById() {
        Long taskId = 1L;
        Long userId = 1L;

        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteById(taskId, userId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }
}
