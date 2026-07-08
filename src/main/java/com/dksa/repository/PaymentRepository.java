package com.dksa.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dksa.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByBookingId(Long bookingId);

    @Query("""
            select coalesce(sum(p.amount),0)
            from Payment p
            where p.paymentStatus='PAID'
            """)
    Double getTotalRevenue();

    @Query("""
            select coalesce(sum(p.amount),0)
            from Payment p
            where p.paymentStatus='SUCCESS'
            and p.createdAt >= :start
            and p.createdAt < :end
            """)
    BigDecimal getTodayRevenue(LocalDateTime start,
                               LocalDateTime end);

    @Query("""
            select p
            from Payment p
            order by p.createdAt
            """)
    List<Payment> findAllOrderByCreatedAt();
    
    List<Payment> findAllByOrderByCreatedAtAsc();

}