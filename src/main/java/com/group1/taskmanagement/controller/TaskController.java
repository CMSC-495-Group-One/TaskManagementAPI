package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.service.TaskService;
import com.group1.taskmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDto taskDto) {
        taskService.createTask(taskDto);
        return ResponseEntity.ok("Task created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        User currentUser = userService.getCurrentUser();
        TaskDto updatedTask = taskService.updateTask(id, taskDto, currentUser);
        return ResponseEntity.ok(updatedTask);
    }
}
