package com.dksa.service;

import java.util.List;

import com.dksa.dto.TurfRequest;
import com.dksa.dto.TurfResponse;

public interface TurfService {

	TurfResponse createTurf(TurfRequest request);

	List<TurfResponse> getAllTurfs();

	TurfResponse getTurfById(Long id);

	TurfResponse updateTurf(Long id, TurfRequest request);

	void deleteTurf(Long id);

	void toggleTurf(Long turfId, Boolean active);

	List<TurfResponse> getActiveTurfs();
}