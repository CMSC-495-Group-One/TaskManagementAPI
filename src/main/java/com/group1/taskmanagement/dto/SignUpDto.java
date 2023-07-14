package com.group1.taskmanagement.dto;

import lombok.Data;

@Data
public class SignUpDto {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
}
