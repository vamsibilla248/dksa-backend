package com.dksa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dksa.entity.Turf;

public interface TurfRepository
        extends JpaRepository<Turf, Long> {

    List<Turf> findByActiveTrue();
}