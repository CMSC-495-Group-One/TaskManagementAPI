package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.error.ResourceNotFoundException;
import com.group1.taskmanagement.interfaces.HasResourceRights;
import com.group1.taskmanagement.model.Task;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.TaskRepository;
import com.group1.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<TaskDto> findAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(Task::toDto)
                .collect(Collectors.toList());
    }

    public TaskDto findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return Task.toDto(task);
    }

    public void createTask(TaskDto taskDto) {
        User user = userRepository.findById(taskDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + taskDto.getUserId() + " not found"));
        Task newTask = Task.fromDto(taskDto, user);
        newTask.setCreatedDate(LocalDateTime.now());
        taskRepository.save(newTask);
    }

    @HasResourceRights
    public TaskDto updateTask(Long id, Long userId, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        Task updatedTaskFromDto;
        if (taskDto.getUserId() != null) {
            User user = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + taskDto.getUserId() + " not found"));
            updatedTaskFromDto = Task.fromDto(taskDto, user);
        } else {
            updatedTaskFromDto = Task.fromDto(taskDto, null);
        }

        try {
            ObjectUpdater.updateObject(existingTask, updatedTaskFromDto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update task");
        }

        existingTask.setUpdatedDate(LocalDateTime.now());

        Task updatedTask = taskRepository.save(existingTask);
        return Task.toDto(updatedTask);
    }

    public List<TaskDto> findAllByUserId(Long id) {
        List<Task> tasks = taskRepository.findByUser_userId(id);
        return tasks.stream()
                .map(Task::toDto)
                .collect(Collectors.toList());
    }

    @HasResourceRights
    public TaskDto deleteById(Long id, Long userId) {
        if (!taskRepository.existsById(id)) {
            return null;
        }
        Task deletedTask = taskRepository.findById(id).get();
        taskRepository.deleteById(id);
        return Task.toDto(deletedTask);
    }
}
