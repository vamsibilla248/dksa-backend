package com.dksa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dksa.dto.TurfRequest;
import com.dksa.dto.TurfResponse;
import com.dksa.service.TurfService;

@RestController
@RequestMapping("/api/admin/turfs")
public class TurfController {

	private final TurfService turfService;

	public TurfController(TurfService turfService) {

		this.turfService = turfService;
	}

	@PostMapping
	public ResponseEntity<TurfResponse> createTurf(@RequestBody TurfRequest request) {
		
		System.out.println("-------createTurf--------");

		return ResponseEntity.ok(turfService.createTurf(request));
	}

	@GetMapping
	public ResponseEntity<List<TurfResponse>> getAllTurfs() {

		return ResponseEntity.ok(turfService.getAllTurfs());
	}

	@GetMapping("/{id}")
	public ResponseEntity<TurfResponse> getTurfById(@PathVariable Long id) {

		return ResponseEntity.ok(turfService.getTurfById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TurfResponse> updateTurf(@PathVariable Long id, @RequestBody TurfRequest request) {

		return ResponseEntity.ok(turfService.updateTurf(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTurf(@PathVariable Long id) {

		turfService.deleteTurf(id);

		return ResponseEntity.ok("Turf Deleted Successfully");
	}

	@PutMapping("/{id}/toggle")
	public ResponseEntity<String> toggleTurf(

			@PathVariable Long id,

			@RequestParam Boolean active) {

		turfService.toggleTurf(id, active);

		return ResponseEntity.ok("Turf status updated");
	}
}