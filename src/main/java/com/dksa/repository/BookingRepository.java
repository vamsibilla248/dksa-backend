package com.dksa.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dksa.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    @Query("""
            select count(b)
            from Booking b
            """)
    Long getTotalBookings();

    @Query("""
            select coalesce(sum(b.totalAmount),0)
            from Booking b
            where b.paymentStatus='PAID'
            """)
    Double getTotalRevenue();

    // Database Independent
    @Query("""
            select count(b)
            from Booking b
            where b.bookingTime >= :start
            and b.bookingTime < :end
            """)
    Long getTodayBookings(LocalDateTime start,
                          LocalDateTime end);

    // Database Independent
    @Query("""
            select coalesce(sum(b.totalAmount),0)
            from Booking b
            where b.bookingTime >= :start
            and b.bookingTime < :end
            and b.paymentStatus='PAID'
            """)
    Double getTodayRevenue(LocalDateTime start,
                           LocalDateTime end);

    Long countByBookingTime(LocalDate date);

    Long countBy();

    // This still depends on JPQL MONTH() support
    @Query("""
            select
            month(b.bookingTime),
            count(b.id)
            from Booking b
            group by month(b.bookingTime)
            order by month(b.bookingTime)
            """)
    List<Object[]> getMonthlyBookings();
    
    List<Booking> findAllByOrderByBookingTimeAsc();
    
    List<Booking> findAllByOrderByIdDesc();

    List<Booking> findByUserIdOrderByIdDesc(Long userId);

}