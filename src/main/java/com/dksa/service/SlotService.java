package com.dksa.service;

import java.time.LocalDate;
import java.util.List;

import com.dksa.dto.CalendarSlotResponse;
import com.dksa.dto.SlotRequest;
import com.dksa.dto.SlotResponse;
import com.dksa.entity.SlotStatus;

public interface SlotService {

    // ==========================
    // Create Slot
    // ==========================
    SlotResponse createSlot(
            SlotRequest request);

    // ==========================
    // Get All Slots
    // ==========================
    List<SlotResponse> getAllSlots();

    // ==========================
    // Get Slot By Id
    // ==========================
    SlotResponse getSlotById(
            Long id);

    // ==========================
    // Update Slot
    // ==========================
    SlotResponse updateSlot(
            Long id,
            SlotRequest request);

    // ==========================
    // Delete Slot
    // ==========================
    void deleteSlot(
            Long id);

    // ==========================
    // Get Slots By Turf & Date
    // ==========================
    List<SlotResponse> getSlotsByTurfAndDate(
            Long turfId,
            LocalDate date);

    // ==========================
    // Update Slot Status
    // ==========================
    void updateSlotStatus(
            Long slotId,
            SlotStatus status);

    // ==========================
    // Calendar View
    // ==========================
    List<CalendarSlotResponse> getCalendarSlots(
            Long turfId,
            Integer month,
            Integer year);

	List<SlotResponse> getAdminSlotsByTurfAndDate(Long turfId, LocalDate date);
}