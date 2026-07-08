package com.dksa.serviceimpl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.dksa.service.ImageUploadService;

@Service
public class ImageUploadServiceImpl
        implements ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadServiceImpl(
            Cloudinary cloudinary) {

        this.cloudinary =
                cloudinary;
    }

    @Override
    public String upload(
            MultipartFile file) {

        try {

            Map<?, ?> result =
                    cloudinary.uploader()
                            .upload(
                                    file.getBytes(),
                                    Map.of());

            return result
                    .get("secure_url")
                    .toString();

        } catch (Exception ex) {

            throw new RuntimeException(
                    ex.getMessage());
        }
    }
}