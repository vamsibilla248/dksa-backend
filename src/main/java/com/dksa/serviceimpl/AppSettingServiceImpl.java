package com.dksa.serviceimpl;

import org.springframework.stereotype.Service;

import com.dksa.entity.AppSetting;
import com.dksa.repository.AppSettingRepository;
import com.dksa.service.AppSettingService;

@Service
public class AppSettingServiceImpl implements AppSettingService {

	private final AppSettingRepository repository;

	public AppSettingServiceImpl(AppSettingRepository repository) {

		this.repository = repository;
	}

	@Override
	public boolean getPaymentStatus() {

		return repository.findById(1L).map(AppSetting::isPaymentRequired).orElse(true);
	}

	@Override
	public void updatePaymentStatus(boolean paymentRequired) {

		AppSetting setting = repository.findById(1L).orElse(AppSetting.builder().id(1L).paymentRequired(true).build());

		setting.setPaymentRequired(paymentRequired);

		repository.save(setting);
	}
}