package com.flightbooking.service;

import com.flightbooking.dto.FlightResponse;
import com.flightbooking.entity.Flight;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FlightService {

    private final FlightRepository flightRepository;

    public List<FlightResponse> searchFlights(String from, String to, LocalDate date,
            Integer passengers, String travelClass) {
        LocalDateTime dateStart = date.atStartOfDay();
        LocalDateTime dateEnd = date.atTime(LocalTime.MAX);

        List<Flight> flights = flightRepository.searchFlights(from, to, dateStart, dateEnd, passengers);

        return flights.stream()
                .filter(f -> travelClass == null || travelClass.equalsIgnoreCase(f.getTravelClass()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return mapToResponse(flight);
    }

    private FlightResponse mapToResponse(Flight flight) {
        FlightResponse response = new FlightResponse();
        response.setId(flight.getId());
        response.setFlightNumber(flight.getFlightNumber());
        response.setFromAirport(flight.getFromAirport().getCode());
        response.setToAirport(flight.getToAirport().getCode());
        response.setDepartureAt(flight.getDepartureAt());
        response.setArrivalAt(flight.getArrivalAt());
        response.setDurationMinutes(flight.getDurationMinutes());
        response.setPrice(flight.getBasePrice());
        response.setCurrency(flight.getCurrency());
        response.setAvailableSeats(flight.getAvailableSeats());
        response.setTravelClass(flight.getTravelClass());
        response.setBaggageInfo(flight.getBaggageInfo());
        response.setChangeCancelRules(flight.getChangeCancelRules());
        return response;
    }


    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }
}
