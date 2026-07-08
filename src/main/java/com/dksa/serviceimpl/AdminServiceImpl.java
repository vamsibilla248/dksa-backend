package com.dksa.serviceimpl;

import org.springframework.stereotype.Service;

import com.dksa.dto.AnalyticsResponse;
import com.dksa.dto.DashboardResponse;
import com.dksa.entity.Payment;
import com.dksa.repository.BookingRepository;
import com.dksa.repository.PaymentRepository;
import com.dksa.repository.UserRepository;
import com.dksa.service.AdminService;

@Service
public class AdminServiceImpl
        implements AdminService {

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final PaymentRepository paymentRepository;

    public AdminServiceImpl(
            UserRepository userRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository) {

        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .totalUsers(
                        userRepository.count())
                .totalBookings(
                        bookingRepository.count())
                .totalPayments(
                        paymentRepository.count())
                .totalRevenue(
                        paymentRepository
                                .getTotalRevenue())
                .build();
    }
    
    @Override
    public AnalyticsResponse getAnalytics() {

        Double revenue =
                paymentRepository
                        .findAll()
                        .stream()
                        .mapToDouble(
                            Payment::getAmount)
                        .sum();

        Long bookings =
                bookingRepository.count();

        Long customers =
                userRepository.count();

        return AnalyticsResponse
                .builder()
                .totalRevenue(revenue)
                .totalBookings(bookings)
                .totalCustomers(customers)
                .mostPopularTurf("Coming Soon")
                .peakHour("Coming Soon")
                .build();
    }
}