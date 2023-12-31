package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.service.TaskService;
import com.group1.taskmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
        TaskDto existingTask = taskService.findTaskById(id);
        TaskDto updatedTask = taskService.updateTask(id, existingTask.getUserId(), taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        TaskDto existingTask = taskService.findTaskById(id);
        taskService.deleteById(id, existingTask.getUserId());
        return ResponseEntity.noContent().build();
    }
}
