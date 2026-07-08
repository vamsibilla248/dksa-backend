package com.dksa.serviceimpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dksa.dto.AuthResponse;
import com.dksa.dto.ChangePasswordRequest;
import com.dksa.dto.LoginRequest;
import com.dksa.dto.ProfileResponse;
import com.dksa.dto.RegisterRequest;
import com.dksa.dto.UpdateProfileRequest;
import com.dksa.dto.UserResponse;
import com.dksa.entity.User;
import com.dksa.exception.InvalidCredentialsException;
import com.dksa.exception.ResourceAlreadyExistsException;
import com.dksa.exception.ResourceNotFoundException;
import com.dksa.jwt.JwtUtil;
import com.dksa.repository.UserRepository;
import com.dksa.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String register(RegisterRequest request) {

        if(userRepository.existsByEmail(
                request.getEmail())) {

        	throw new ResourceAlreadyExistsException(
        	        "Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role("CUSTOMER")
                .build();

        userRepository.save(user);

        return "User Registered Successfully";
    }
    
    @Override
    public AuthResponse login(
            LoginRequest request) {
    	
    	System.out.println("-----loginnnnnnnnnnnn----"+request);

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
        
        System.out.println("-----user--found--"+user);

        boolean isValid =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!isValid) {

            throw new InvalidCredentialsException(
                    "Invalid Credentials");
        }

        String token =
                jwtUtil.generateToken(
                        user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole())
                .username(user.getName())
                .message("Login Successful")
                .build();
    }
    
    
    @Override
    public UserResponse getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .build();
    }
    
    @Override
    public ProfileResponse getProfile() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return ProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .mobileNumber(user.getMobile())
                .build();
    }
    
    
    @Override
    public ProfileResponse updateProfile(
            UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        user.setName(
                request.getName());

        user.setMobile(
                request.getMobileNumber());


        userRepository.save(user);

        return ProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .mobileNumber(user.getMobile())
                .build();
    }
    
    
    @Override
    public void changePassword(
            ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Current password incorrect");
        }

        if (!request.getNewPassword()
                .equals(
                        request.getConfirmPassword())) {

            throw new RuntimeException(
                    "Passwords do not match");
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        userRepository.save(user);
    }
    
}