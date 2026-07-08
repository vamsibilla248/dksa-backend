package com.dksa.service;

public interface AppSettingService {

	boolean getPaymentStatus();

	void updatePaymentStatus(boolean paymentRequired);
}