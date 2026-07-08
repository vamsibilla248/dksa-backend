package com.dksa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Mobile must be 10 digits"
    )
    private String mobile;

    @NotBlank(message = "Password is required")
    private String password;
}