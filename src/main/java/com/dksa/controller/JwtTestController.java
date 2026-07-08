package com.dksa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.jwt.JwtUtil;

@RestController
@RequestMapping("/jwt")
public class JwtTestController {

    private final JwtUtil jwtUtil;

    public JwtTestController(
            JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/generate")
    public String generate() {

        return jwtUtil.generateToken(
                "vamsi@gmail.com");
    }
    
    
    @GetMapping("/extract")
    public String extract(
            @RequestParam String token) {

        return jwtUtil.extractUsername(
                token);
    }
}