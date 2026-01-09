package com.flightbooking.controller;

import com.flightbooking.dto.BookingResponse;
import com.flightbooking.dto.FlightCreateRequest;
import com.flightbooking.dto.FlightResponse;
import com.flightbooking.dto.UserCreateRequest;
import com.flightbooking.entity.User;
import com.flightbooking.service.AdministratorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdministratorController {

    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    /**
     * Dodaje nowy lot.
     * UML: Administrator.dodajLot(cel: Lot)
     */
    @PostMapping("/flights")
    public ResponseEntity<FlightResponse> addFlight(@Valid @RequestBody FlightCreateRequest request) {
        FlightResponse flight = administratorService.addFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(flight);
    }

    /**
     * Edytuje istniejący lot.
     * UML: Administrator.edytujLot(idLotu: int, noweDane: Lot)
     */
    @PutMapping("/flights/{id}")
    public ResponseEntity<FlightResponse> editFlight(@PathVariable Long id, @Valid @RequestBody FlightCreateRequest request) {
        FlightResponse flight = administratorService.editFlight(id, request);
        return ResponseEntity.ok(flight);
    }

    /**
     * Usuwa lot.
     * UML: Administrator.usunLot(idLotu: int)
     */
    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> removeFlight(@PathVariable Long id) {
        administratorService.removeFlight(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Przegląda rezerwację.
     * UML: Administrator.przegladajRezerwacje(int idRezerwacji)
     */
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingResponse> viewReservation(@PathVariable Long id) {
        BookingResponse booking = administratorService.viewReservation(id);
        return ResponseEntity.ok(booking);
    }

    /**
     * Dodaje nowego użytkownika (Client lub Administrator).
     * UML: Administrator.dodajUzytkownika(nowy: UzytkownikSystemu)
     */
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserCreateRequest request) {
        User user = administratorService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Edytuje dane użytkownika.
     * UML: Administrator.edytujUzytkownika(idUzytkownika: int, noweDane: UzytkownikSystemu)
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        User user = administratorService.editUser(id, request);
        return ResponseEntity.ok(user);
    }

    /**
     * Usuwa użytkownika.
     * UML: Administrator.usunUzytkownika(id: int)
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        administratorService.removeUser(id);
        return ResponseEntity.noContent().build();
    }
}
