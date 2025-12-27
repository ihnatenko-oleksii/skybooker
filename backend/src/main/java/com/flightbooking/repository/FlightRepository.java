package com.flightbooking.repository;

import com.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE " +
            "f.fromAirport.code = :from AND " +
            "f.toAirport.code = :to AND " +
            "f.departureAt >= :dateStart AND " +
            "f.departureAt < :dateEnd AND " +
            "f.availableSeats >= :passengers " +
            "ORDER BY f.departureAt ASC")
    List<Flight> searchFlights(@Param("from") String from,
            @Param("to") String to,
            @Param("dateStart") LocalDateTime dateStart,
            @Param("dateEnd") LocalDateTime dateEnd,
            @Param("passengers") int passengers);
}
