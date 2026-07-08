package com.dksa.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dksa.entity.Slot;
import com.dksa.entity.SlotStatus;

public interface SlotRepository extends JpaRepository<Slot, Long> {

	List<Slot> findByBookingDate(LocalDate bookingDate);

	List<Slot> findByTurfIdAndBookingDate(Long turfId, LocalDate bookingDate);

	List<Slot> findByTurfIdAndBookingDateAndStatus(Long turfId, LocalDate bookingDate, SlotStatus status);

	boolean existsByIdAndStatus(Long id, SlotStatus status);
	
	
	@Query("""
			SELECT s
			FROM Slot s
			WHERE s.turf.id = :turfId
			AND s.bookingDate >= :start
			AND s.bookingDate < :end
			""")
	List<Slot> findByTurfAndMonth(Long turfId, LocalDate start, LocalDate end);

}