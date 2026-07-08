package com.dksa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.AuthResponse;
import com.dksa.dto.LoginRequest;
import com.dksa.dto.RegisterRequest;
import com.dksa.dto.UserResponse;
import com.dksa.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Validated
            @RequestBody
            RegisterRequest request) {

        return new ResponseEntity<>(
                authService.register(request),
                HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody
            @Valid
            LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request));
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse>
    getCurrentUser() {

        return ResponseEntity.ok(
                authService.getCurrentUser());
    }
}