package com.dksa.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dksa.dto.SlotResponse;
import com.dksa.service.SlotService;

@RestController
@RequestMapping("/api/customer")
public class CustomerSlotController {

    private final SlotService slotService;

    public CustomerSlotController(
            SlotService slotService) {

        this.slotService = slotService;
    }

    @GetMapping("/slots")
    public ResponseEntity<List<SlotResponse>>
    getSlotsByDateAndTurf(

            @RequestParam Long turfId,

            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                slotService.getSlotsByTurfAndDate(
                        turfId,
                        date));
    }
}