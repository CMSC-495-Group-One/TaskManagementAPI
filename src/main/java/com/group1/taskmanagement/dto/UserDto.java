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
    private String firstname;
    private String lastname;
    private String email;
    private List<Long> tasks = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

}
