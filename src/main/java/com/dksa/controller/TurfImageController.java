package com.dksa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dksa.entity.Turf;
import com.dksa.entity.TurfImage;
import com.dksa.exception.ResourceNotFoundException;
import com.dksa.repository.TurfImageRepository;
import com.dksa.repository.TurfRepository;

@RestController
@RequestMapping("/api/admin/turf-images")
public class TurfImageController {

    private final TurfRepository turfRepository;

    private final TurfImageRepository turfImageRepository;

    public TurfImageController(
            TurfRepository turfRepository,
            TurfImageRepository turfImageRepository) {

        this.turfRepository =
                turfRepository;

        this.turfImageRepository =
                turfImageRepository;
    }

    @PostMapping("/{turfId}")
    public ResponseEntity<String>
    addImage(

            @PathVariable
            Long turfId,

            @RequestParam
            String imageUrl) {

        Turf turf =
                turfRepository
                        .findById(turfId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Turf not found"));

        TurfImage image =
                TurfImage.builder()
                        .imageUrl(imageUrl)
                        .turf(turf)
                        .build();

        turfImageRepository.save(
                image);

        return ResponseEntity.ok(
                "Image Added");
    }
}