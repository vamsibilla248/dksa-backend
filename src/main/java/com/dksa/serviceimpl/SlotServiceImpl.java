package com.dksa.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dksa.dto.CalendarSlotResponse;
import com.dksa.dto.SlotRequest;
import com.dksa.dto.SlotResponse;
import com.dksa.entity.Slot;
import com.dksa.entity.SlotStatus;
import com.dksa.entity.Turf;
import com.dksa.exception.ResourceNotFoundException;
import com.dksa.repository.SlotRepository;
import com.dksa.repository.TurfRepository;
import com.dksa.service.SlotService;

@Service
public class SlotServiceImpl implements SlotService {

	private final SlotRepository slotRepository;
	private final TurfRepository turfRepository;

	public SlotServiceImpl(SlotRepository slotRepository, TurfRepository turfRepository) {

		this.slotRepository = slotRepository;
		this.turfRepository = turfRepository;
	}

	@Override
	public SlotResponse createSlot(SlotRequest request) {

		Turf turf = turfRepository.findById(request.getTurfId())
				.orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

		Slot slot = Slot.builder().turf(turf).bookingDate(request.getBookingDate()).slotTime(request.getSlotTime())
				.price(request.getPrice()).status(SlotStatus.OPEN).build();

		slot = slotRepository.save(slot);

		return mapToResponse(slot);
	}

	@Override
	public List<SlotResponse> getAllSlots() {

		return slotRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	@Override
	public SlotResponse getSlotById(Long id) {

		Slot slot = slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		return mapToResponse(slot);
	}

	@Override
	public SlotResponse updateSlot(Long id, SlotRequest request) {

		Slot slot = slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		Turf turf = turfRepository.findById(request.getTurfId())
				.orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

		slot.setTurf(turf);
		slot.setBookingDate(request.getBookingDate());
		slot.setSlotTime(request.getSlotTime());
		slot.setPrice(request.getPrice());

		slot = slotRepository.save(slot);

		return mapToResponse(slot);
	}

	@Override
	public void deleteSlot(Long id) {

		Slot slot = slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		slotRepository.delete(slot);
	}

	private SlotResponse mapToResponse(Slot slot) {

		return SlotResponse.builder().id(slot.getId()).turfId(slot.getTurf().getId())
				.turfName(slot.getTurf().getTurfName()).bookingDate(slot.getBookingDate()).slotTime(slot.getSlotTime())
				.price(slot.getPrice()).status(slot.getStatus()).build();
	}
	
	@Override
	public List<SlotResponse> getSlotsByTurfAndDate(Long turfId, LocalDate date) {

	    return slotRepository
	            .findByTurfIdAndBookingDate(turfId, date)
	            .stream()
	            .sorted(java.util.Comparator.comparing(Slot::getSlotTime))
	            .map(this::mapToResponse)
	            .toList();
	}

	@Override
	public void updateSlotStatus(Long slotId, SlotStatus status) {

		Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		slot.setStatus(status);

		slotRepository.save(slot);
	}
	
	@Override
	public List<CalendarSlotResponse> getCalendarSlots(
	        Long turfId,
	        Integer month,
	        Integer year) {

	    LocalDate start = LocalDate.of(year, month, 1);
	    LocalDate end = start.plusMonths(1);

	    return slotRepository
	            .findByTurfAndMonth(turfId, start, end)
	            .stream()
	            .map(slot ->
	                    CalendarSlotResponse.builder()
	                            .slotDate(slot.getBookingDate())
	                            .slotTime(slot.getSlotTime())
	                            .status(slot.getStatus().name())
	                            .turfName(slot.getTurf().getTurfName())
	                            .build())
	            .toList();
	}
	
	@Override
	public List<SlotResponse> getAdminSlotsByTurfAndDate(
	        Long turfId,
	        LocalDate date) {

	    return slotRepository
	            .findByTurfIdAndBookingDate(
	                    turfId,
	                    date)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}


}