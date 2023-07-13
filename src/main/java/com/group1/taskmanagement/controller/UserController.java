package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.dto.TaskDto;
import com.group1.taskmanagement.dto.UserDto;
import com.group1.taskmanagement.service.TaskService;
import com.group1.taskmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base}/users")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedTask = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getUserTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findAllByUserId(id));
    }
}
