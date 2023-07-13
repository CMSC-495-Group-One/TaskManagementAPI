package com.group1.taskmanagement.repository;

import com.group1.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    List<User> findAllByRoles_roleId(Long roleId);
    Optional<User> findById(Long id);
}
