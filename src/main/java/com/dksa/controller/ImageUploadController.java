package com.dksa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dksa.service.ImageUploadService;

@RestController
@RequestMapping("/api/admin/images")
public class ImageUploadController {

    private final ImageUploadService
            imageUploadService;

    public ImageUploadController(
            ImageUploadService imageUploadService) {

        this.imageUploadService =
                imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String>
    upload(
            @RequestParam(
                    "file")
            MultipartFile file) {

        return ResponseEntity.ok(
                imageUploadService.upload(
                        file));
    }
}