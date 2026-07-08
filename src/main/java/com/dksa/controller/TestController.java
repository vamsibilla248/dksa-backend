package com.dksa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/encode")
    public String encodePassword() {

        return passwordEncoder.encode("admin123");

    }
    
    
    @GetMapping("/match")
    public boolean matchPassword() {

        String encoded =
                passwordEncoder.encode("admin123");

        return passwordEncoder.matches(
                "admin123",
                encoded
        );
    }
    
    
    @GetMapping("/test")
    public String test() {

        return "Application Working";

    }
    
    
    

}