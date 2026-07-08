package com.dksa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dksa.entity.BookingSlot;

public interface BookingSlotRepository
        extends JpaRepository<
                BookingSlot,
                Long> {

}