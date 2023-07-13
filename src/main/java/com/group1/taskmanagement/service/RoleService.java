package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.interfaces.HasAdminRole;
import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.RoleRepository;
import com.group1.taskmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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

    @HasAdminRole
    public void createRole(RoleDto roleDto) {
        Role newRole = Role.fromDto(roleDto);
        roleRepository.save(newRole);
    }

    @HasAdminRole
    public RoleDto updateRole(Long id, RoleDto roleDto) throws IllegalAccessException {
        Role exsistingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role updatedRoleFromDto = Role.fromDto(roleDto);
        ObjectUpdater.updateObject(exsistingRole, updatedRoleFromDto);
        Role updatedRole = roleRepository.save(exsistingRole);
        return Role.toDto(updatedRole);
    }

    @HasAdminRole
    public ResponseEntity<Void> deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<User> users = userRepository.findAllByRoles_roleId(id);

        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        for (User user : users) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }

        roleRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
