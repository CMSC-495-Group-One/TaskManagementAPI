package com.group1.taskmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.error.ResourceNotFoundException;
import com.group1.taskmanagement.model.Task;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.TaskRepository;
import com.group1.taskmanagement.repository.UserRepository;
import com.group1.taskmanagement.service.TaskService;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(1L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> taskService.createTask(taskDto));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testFindAll() {
        // create a User object
        User user = new User();
        user.setUserId(1L);

        // create a Task object
        Task task = new Task();
        task.setUser(user);

        // return the Task object when findAll() is called on the taskRepository
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        // call the method under test
        List<TaskDto> result = taskService.findAll();

        // check the results
        assertFalse(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testFindTaskById() {
        Long id = 1L;
        // create a User object
        User user = new User();
        user.setUserId(1L);

        // create a Task object
        Task task = new Task();
        task.setUser(user);
        task.setId(id);

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        TaskDto result = taskService.findTaskById(id);

        assertNotNull(result); // result itself should not be null
        assertNotNull(result.getId()); // result's id should not be null
        assertEquals(id, result.getId()); // result's id should be as expected
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateTask_ValidData_TaskUpdated() {
        Long taskId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setUserId(userId);

        Task existingTask = new Task();
        existingTask.setUser(user);
        existingTask.setId(taskId);

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(userId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask); // Ensures save() returns a non-null object.

        assertDoesNotThrow(() -> taskService.updateTask(taskId, userId, taskDto));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask_NonExistentTaskId_ResourceNotFoundException() {
        Long taskId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setUserId(userId);

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(userId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, userId, taskDto));
    }

    @Test
    public void testUpdateTask_NonExistentUserId_ResourceNotFoundException() {
        Long taskId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setUserId(userId);

        Task existingTask = new Task();
        existingTask.setUser(user);

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(userId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, userId, taskDto));
    }
}
