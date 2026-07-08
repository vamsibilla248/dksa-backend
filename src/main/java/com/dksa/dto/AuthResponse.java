package com.dksa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;

    private String role;

    private String username;

    private String message;
}