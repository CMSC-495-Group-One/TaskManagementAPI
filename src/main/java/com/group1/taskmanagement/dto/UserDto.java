package com.group1.taskmanagement.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private List<TaskDto> tasks = new ArrayList<>();
    private List<RoleDto> roles = new ArrayList<>();

}
