package com.flightbooking.controller;

import com.flightbooking.dto.BookingCreateRequest;
import com.flightbooking.dto.BookingCreateResponse;
import com.flightbooking.dto.BookingExtrasRequest;
import com.flightbooking.dto.BookingResponse;
import com.flightbooking.dto.PassengerRequest;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.BookingService;
import com.flightbooking.service.ClientService;
import com.flightbooking.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final ClientService clientService;
    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(ClientService clientService, BookingService bookingService, UserRepository userRepository) {
        this.clientService = clientService;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    /**
     * Tworzy rezerwację używając ClientService.
     * UML: Klient.utworzRezerwacje()
     */
    @PostMapping
    public ResponseEntity<BookingCreateResponse> createBooking(
            @Valid @RequestBody BookingCreateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(userRepository);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookingCreateResponse response = clientService.createReservation(
                userId,
                request.getFlightId(),
                request.getPassengers(),
                request.getExtras()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Pobiera rezerwacje użytkownika używając ClientService.
     * UML: Klient.wyswietlRezerwacje()
     */
    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        Long userId = SecurityUtils.getCurrentUserId(userRepository);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<BookingResponse> bookings = clientService.viewReservations(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        // Użycie BookingService bezpośrednio, ponieważ to prosta operacja odczytu
        BookingResponse booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    /**
     * Anuluje rezerwację używając ClientService.
     * UML: Klient.anulujRezerwacje()
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        clientService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Modyfikuje rezerwację używając ClientService.
     * UML: Klient.zmienRezerwacje()
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> modifyBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingModifyRequest request) {
        BookingResponse response = clientService.modifyReservation(
                id,
                request.getPassengers(),
                request.getExtras()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Aktualizuje pasażera w rezerwacji używając ClientService.
     * UML: Pasazer.ZakutalizujDane()
     */
    @PutMapping("/{bookingId}/passengers/{passengerId}")
    public ResponseEntity<BookingResponse> updatePassenger(
            @PathVariable Long bookingId,
            @PathVariable Long passengerId,
            @Valid @RequestBody PassengerRequest request) {
        BookingResponse response = clientService.updatePassenger(bookingId, passengerId, request);
        return ResponseEntity.ok(response);
    }
}

/**
 * DTO dla żądania modyfikacji rezerwacji.
 */
class BookingModifyRequest {
    private List<PassengerRequest> passengers;
    private BookingExtrasRequest extras;

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }

    public BookingExtrasRequest getExtras() {
        return extras;
    }

    public void setExtras(BookingExtrasRequest extras) {
        this.extras = extras;
    }
}
