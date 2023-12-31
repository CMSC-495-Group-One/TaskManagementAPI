package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.UserDto;
import com.group1.taskmanagement.error.ResourceNotFoundException;
import com.group1.taskmanagement.interfaces.HasResourceRights;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.UserRepository;
import com.group1.taskmanagement.security.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(customUser.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + customUser.getUserId() + " not found"));
    }

    public boolean hasAdminRole() {
        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Access is denied: No Admin Role");
        }
        return true;
    }

    public boolean hasUserRights(Long id) {
        if (id == null) return true;
        User currentUser = getCurrentUser();
        boolean hasRights = currentUser.getUserId().equals(id) || hasAdminRole();
        if (!hasRights) {
            throw new AccessDeniedException("Access is denied: Insufficient User Rights");
        }
        return true;
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(User::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return User.toDto(user);
    }

    @HasResourceRights
    public UserDto updateUser(Long userId, UserDto user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        try {
            ObjectUpdater.updateObject(existingUser, User.fromDto(user));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update user");
        }

        User updatedUser = userRepository.save(existingUser);
        return User.toDto(updatedUser);
    }

    @HasResourceRights
    public void deleteById(Long userId) {
        User user;
        Optional<User> deletedUser = userRepository.findById(userId);

        if (deletedUser.isPresent()) {
            user = deletedUser.get();
        } else {
            return;
        }

        userRepository.deleteById(userId);
    }

    public List<String> findUserRoles(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return existingUser.getRoles().stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList());
    }
}
