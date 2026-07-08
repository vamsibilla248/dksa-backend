package com.dksa.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    String upload(
            MultipartFile file);
}