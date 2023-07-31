package com.group1.taskmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.model.Task;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.TaskRepository;
import com.group1.taskmanagement.repository.UserRepository;
import com.group1.taskmanagement.service.TaskService;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
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
        this.taskService = new TaskService(taskRepository, userRepository);
    }

    @Test
    public void testFindAll() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(new Task()));

        List<TaskDto> result = taskService.findAll();

        assertFalse(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testFindTaskById() {
        Long id = 1L;
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(new Task()));

        TaskDto result = taskService.findTaskById(id);

        assertNotNull(result); // result itself should not be null
        assertNotNull(result.getId()); // result's id should not be null
        assertEquals(id, result.getId()); // result's id should be as expected
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(1L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> taskService.createTask(taskDto));
        verify(userRepository, times(1)).findById(any(Long.class));
    }
}
