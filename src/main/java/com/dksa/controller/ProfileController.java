package com.dksa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.ChangePasswordRequest;
import com.dksa.dto.ProfileResponse;
import com.dksa.dto.UpdateProfileRequest;
import com.dksa.service.AuthService;

@RestController
@RequestMapping("/api/customer/profile")
public class ProfileController {

    private final AuthService authService;

    public ProfileController(
            AuthService authService) {

        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse>
    getProfile() {

        return ResponseEntity.ok(
                authService.getProfile());
    }

    @PutMapping
    public ResponseEntity<ProfileResponse>
    updateProfile(
            @RequestBody
            UpdateProfileRequest request) {

        return ResponseEntity.ok(
                authService.updateProfile(
                        request));
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<String>
    changePassword(
            @RequestBody
            ChangePasswordRequest request) {

        authService.changePassword(
                request);

        return ResponseEntity.ok(
                "Password Updated");
    }
}