package com.flightbooking.controller;

import com.flightbooking.entity.Airport;
import com.flightbooking.service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {
    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    /**
     * Aktualizuje dane lotniska.
     * UML: Lotnisko.zaktualizujDane()
     */
    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Airport> updateAirport(
            @PathVariable String code,
            @RequestBody Airport newData) {
        Airport updated = airportService.updateAirport(code, newData);
        return ResponseEntity.ok(updated);
    }
}
