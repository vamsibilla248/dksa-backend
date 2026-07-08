package com.dksa.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dksa.dto.CalendarSlotResponse;
import com.dksa.dto.SlotRequest;
import com.dksa.dto.SlotResponse;
import com.dksa.service.SlotService;

@RestController
@RequestMapping("/api/admin/slots")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminSlotController {

    private final SlotService slotService;

    public AdminSlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    // ==========================
    // Get Slots By Turf & Date
    // ==========================

    @GetMapping
    public ResponseEntity<List<SlotResponse>> getSlotsByTurfAndDate(
            @RequestParam Long turfId,
            @RequestParam String date) {

        return ResponseEntity.ok(
                slotService.getSlotsByTurfAndDate(
                        turfId,
                        LocalDate.parse(date)));
    }

    // ==========================
    // Get All Slots
    // ==========================

    @GetMapping("/all")
    public ResponseEntity<List<SlotResponse>> getAllSlots() {

        return ResponseEntity.ok(
                slotService.getAllSlots());
    }

    // ==========================
    // Get Slot By Id
    // ==========================

    @GetMapping("/{id}")
    public ResponseEntity<SlotResponse> getSlotById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                slotService.getSlotById(id));
    }

    // ==========================
    // Create Slot
    // ==========================

    @PostMapping
    public ResponseEntity<SlotResponse> createSlot(
            @RequestBody SlotRequest request) {

        SlotResponse slot =
                slotService.createSlot(request);

        return new ResponseEntity<>(
                slot,
                HttpStatus.CREATED);
    }

    // ==========================
    // Update Slot
    // ==========================

    @PutMapping("/{id}")
    public ResponseEntity<SlotResponse> updateSlot(
            @PathVariable Long id,
            @RequestBody SlotRequest request) {

        return ResponseEntity.ok(
                slotService.updateSlot(
                        id,
                        request));
    }

    // ==========================
    // Delete Slot
    // ==========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable Long id) {

        slotService.deleteSlot(id);

        return ResponseEntity.noContent().build();
    }

    // ==========================
    // Calendar View
    // ==========================

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarSlotResponse>> calendar(
            @RequestParam Long turfId,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        return ResponseEntity.ok(
                slotService.getCalendarSlots(
                        turfId,
                        month,
                        year));
    }
}