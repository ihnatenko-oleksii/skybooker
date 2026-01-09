package com.flightbooking.repository;

import com.flightbooking.entity.Booking;
import com.flightbooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    long countByFlightIdAndStatusNot(Long flightId, BookingStatus status);
    
    long countByUserIdAndStatusNot(Long userId, BookingStatus status);
}
