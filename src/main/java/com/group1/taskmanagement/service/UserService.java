package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.UserDto;
import com.group1.taskmanagement.interfaces.HasAdminRole;
import com.group1.taskmanagement.interfaces.HasResourceRights;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.UserRepository;
import com.group1.taskmanagement.security.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(customUser.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean hasAdminRole() {
        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
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

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return User.toDto(user);
    }

    @HasResourceRights
    public UserDto updateUser(Long id, UserDto user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        try {
            ObjectUpdater.updateObject(existingUser, User.fromDto(user));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update user");
        }

        User updatedUser = userRepository.save(existingUser);
        return User.toDto(updatedUser);
    }

    @HasAdminRole
    public UserDto deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        User deletedUser = userRepository.findById(id).get();
        userRepository.deleteById(id);
        return User.toDto(deletedUser);
    }
}
