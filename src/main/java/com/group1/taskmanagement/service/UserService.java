package com.group1.taskmanagement.service;

import com.group1.taskmanagement.dto.UserDto;
import com.group1.taskmanagement.model.User;
import com.group1.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
