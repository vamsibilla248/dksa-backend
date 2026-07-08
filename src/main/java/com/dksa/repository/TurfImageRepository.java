package com.dksa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dksa.entity.TurfImage;

public interface TurfImageRepository
extends JpaRepository<
        TurfImage,
        Long> {

    List<TurfImage>
    findByTurfId(
            Long turfId);
}