package com.flightbooking.controller;

import com.flightbooking.dto.FlightResponse;
import com.flightbooking.service.ClientService;
import com.flightbooking.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final ClientService clientService;
    private final FlightService flightService;

    /**
     * Wyszukuje loty używając ClientService.
     * UML: Klient.wyszukajLoty()
     */
    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false, defaultValue = "1") Integer passengers,
            @RequestParam(required = false) String travelClass) {

        List<FlightResponse> flights = clientService.searchFlights(from, to, date, passengers, travelClass);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        FlightResponse flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }

    public FlightController(ClientService clientService, FlightService flightService) {
        this.clientService = clientService;
        this.flightService = flightService;
    }
}
