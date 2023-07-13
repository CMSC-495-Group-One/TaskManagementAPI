package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.model.Task;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.TaskRepository;
import com.group1.taskmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskDto> findAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(Task::toDto)
                .collect(Collectors.toList());
    }

    public TaskDto findTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return Task.toDto(task);
    }

    public void createTask(TaskDto taskDto) {
        User user = userRepository.findById(taskDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + taskDto.getUserId()));
        Task newTask = Task.fromDto(taskDto, user);
        newTask.setCreatedDate(LocalDateTime.now());
        taskRepository.save(newTask);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Task updatedTaskFromDto;

        if (taskDto.getUserId() != null) {
            User user = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + taskDto.getUserId()));
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
        List<Task> tasks = taskRepository.findByUserId(id);
        return tasks.stream()
                .map(Task::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Void> deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
