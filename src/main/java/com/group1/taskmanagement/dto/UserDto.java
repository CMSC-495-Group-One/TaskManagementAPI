package com.group1.taskmanagement.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private List<Long> tasks = new ArrayList<>();
    private List<String> roles = new ArrayList<>();
}
