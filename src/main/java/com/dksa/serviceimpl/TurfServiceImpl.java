package com.dksa.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dksa.dto.TurfRequest;
import com.dksa.dto.TurfResponse;
import com.dksa.entity.Turf;
import com.dksa.entity.TurfImage;
import com.dksa.exception.ResourceNotFoundException;
import com.dksa.repository.TurfImageRepository;
import com.dksa.repository.TurfRepository;
import com.dksa.service.TurfService;

@Service
public class TurfServiceImpl implements TurfService {

	private final TurfRepository turfRepository;
	private final TurfImageRepository turfImageRepository;

	public TurfServiceImpl(
	        TurfRepository turfRepository,
	        TurfImageRepository turfImageRepository) {

	    this.turfRepository =
	            turfRepository;

	    this.turfImageRepository =
	            turfImageRepository;
	}

	@Override
	public TurfResponse createTurf(TurfRequest request) {
		
		System.out.println("-------createTurf repo--------"+request);

		Turf turf =
		        Turf.builder()
		                .turfName(request.getTurfName())
		                .turfType(request.getTurfType())
		                .location(request.getLocation())
		                .imageUrl(request.getImageUrl())
		                .active(request.getActive() == null
		                                ? true
		                                : request.getActive())
		                .build();

		
		turf = turfRepository.save(turf);
		
		System.out.println("-------saved--------");

		return mapToResponse(turf);
	}

	@Override
	public List<TurfResponse> getAllTurfs() {

		return turfRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	@Override
	public TurfResponse getTurfById(Long id) {

		Turf turf = turfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

		return mapToResponse(turf);
	}

	@Override
	public TurfResponse updateTurf(Long id, TurfRequest request) {

		Turf turf = turfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

		turf.setTurfName(request.getTurfName());

		turf.setTurfType(request.getTurfType());
		turf.setImageUrl(request.getImageUrl());

		turf.setActive(request.getActive());

		turf = turfRepository.save(turf);

		return mapToResponse(turf);
	}

	@Override
	public void deleteTurf(Long id) {

		Turf turf = turfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

		turfRepository.delete(turf);
	}

	

	@Override
	public void toggleTurf(Long turfId, Boolean active) {

		Turf turf = turfRepository.findById(turfId).orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

		turf.setActive(active);

		turfRepository.save(turf);
	}
	
	@Override
	public List<TurfResponse>
	getActiveTurfs() {

	    return turfRepository
	            .findByActiveTrue()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	
	private TurfResponse mapToResponse(
	        Turf turf) {

	    List<String> images =
	            turfImageRepository
	                    .findByTurfId(
	                            turf.getId())
	                    .stream()
	                    .map(
	                        TurfImage::getImageUrl)
	                    .toList();

	    return TurfResponse.builder()
	            .id(turf.getId())
	            .turfName(
	                    turf.getTurfName())
	            .turfType(
	                    turf.getTurfType())
	            .location(
	                    turf.getLocation())
	            .imageUrl(
	                    turf.getImageUrl())
	            .imageUrls(
	                    images)
	            .active(
	                    turf.getActive())
	            .build();
	}
}