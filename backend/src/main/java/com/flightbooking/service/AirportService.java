package com.flightbooking.service;

import com.flightbooking.entity.Airport;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AirportService {
    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    /**
     * Aktualizuje dane lotniska.
     * UML: Lotnisko.zaktualizujDane(noweDane: Lotnisko)
     */
    @Transactional
    public Airport updateAirport(String code, Airport newData) {
        Airport airport = airportRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with code: " + code));
        
        airport.updateData(newData);
        return airportRepository.save(airport);
    }
}
