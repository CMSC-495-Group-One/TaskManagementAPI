package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base}/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody RoleDto roleDto) {
        roleService.createRole(roleDto);
        return ResponseEntity.ok("Role created!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findByRoleId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto roleDto, @PathVariable Long id) throws IllegalAccessException {
        RoleDto updatedRole = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        return roleService.deleteById(id);
    }
}
