package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(Role::toDto)
                .collect(Collectors.toList());
    }

    public RoleDto findByRoleId(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        return Role.toDto(role);
    }

    public void createRole(RoleDto roleDto) {
        Role newRole = Role.fromDto(roleDto);
        roleRepository.save(newRole);
    }

    public RoleDto updateRole(Long id, RoleDto roleDto) throws IllegalAccessException {
        Role exsistingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role updatedRoleFromDto = Role.fromDto(roleDto);
        ObjectUpdater.updateObject(exsistingRole, updatedRoleFromDto);
        Role updatedRole = roleRepository.save(exsistingRole);
        return Role.toDto(updatedRole);
    }
}
