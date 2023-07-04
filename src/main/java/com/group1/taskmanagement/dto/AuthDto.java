package com.group1.taskmanagement.dto;

import lombok.Data;

@Data
public class AuthDto {

    private String accessToken;
    private String tokenType = "Bearer";

    public AuthDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
