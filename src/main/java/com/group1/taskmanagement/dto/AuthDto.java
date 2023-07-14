package com.group1.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {

    private String accessToken;
    private String tokenType = "Bearer";

    public AuthDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
