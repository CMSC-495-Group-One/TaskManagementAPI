package com.group1.taskmanagement.repository;

import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
