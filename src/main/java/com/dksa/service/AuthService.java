package com.dksa.service;

import com.dksa.dto.AuthResponse;
import com.dksa.dto.ChangePasswordRequest;
import com.dksa.dto.LoginRequest;
import com.dksa.dto.ProfileResponse;
import com.dksa.dto.RegisterRequest;
import com.dksa.dto.UpdateProfileRequest;
import com.dksa.dto.UserResponse;

public interface AuthService {

    String register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser();
    
    ProfileResponse getProfile();

    ProfileResponse updateProfile(
            UpdateProfileRequest request);
    
    void changePassword(
            ChangePasswordRequest request);
}