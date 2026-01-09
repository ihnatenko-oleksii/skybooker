package com.flightbooking.service;

import com.flightbooking.dto.*;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BusinessException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.PassengerRepository;
import com.flightbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;

    @Transactional
    public BookingCreateResponse createBooking(Long userId, BookingCreateRequest request) {
        // Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Fetch flight
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + request.getFlightId()));

        // Sprawdzenie dostępności
        if (!flight.checkAvailability(request.getPassengers().size())) {
            throw new BusinessException("Not enough available seats. Available: " + flight.getAvailableSeats());
        }

        // Utworzenie rezerwacji
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus(BookingStatus.PENDING_PAYMENT); // Użycie setStatus z walidacją
        booking.setCurrency(flight.getCurrency());

        // Set extras
        BookingExtrasRequest extras = request.getExtras();
        if (extras != null) {
            booking.setExtraBaggage(extras.getExtraBaggage());
            booking.setInsurance(extras.getInsurance());
        }

        // Dodanie pasażerów używając metody addPassengers z UML
        List<Passenger> passengers = request.getPassengers().stream()
                .map(passengerReq -> {
                    Passenger passenger = new Passenger();
                    passenger.setFirstName(passengerReq.getFirstName());
                    passenger.setLastName(passengerReq.getLastName());
                    passenger.setBirthDate(passengerReq.getBirthDate());
                    passenger.setDocumentNumber(passengerReq.getDocumentNumber());
                    return passenger;
                })
                .collect(java.util.stream.Collectors.toList());
        booking.addPassengers(passengers);

        // Obliczenie całkowitej ceny używając logiki domenowej
        booking.calculatePrice();

        // Zapisanie rezerwacji
        booking = bookingRepository.save(booking);

        // Zwrócenie odpowiedzi - użycie getPrice() z UML
        BookingCreateResponse response = new BookingCreateResponse();
        response.setId(booking.getId());
        response.setStatus(booking.getStatus().name());
        response.setTotalPrice(booking.getPrice()); // Użycie getPrice() z UML
        response.setCurrency(booking.getCurrency());

        return response;
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return mapToResponse(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.PAID) {
            // In a real system, you might want to check cancellation rules and process
            // refunds
            // For MVP, we just change the status
        }

        // Użycie metody cancelReservation z UML
        booking.cancelReservation();
        bookingRepository.save(booking);
    }

    /**
     * Modifies a booking - updates passengers and extras.
     * UML: Klient.zmienRezerwacje()
     */
    @Transactional
    public BookingResponse modifyBooking(Long bookingId, List<PassengerRequest> newPassengers, BookingExtrasRequest newExtras) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Cannot modify cancelled booking");
        }

        if (booking.getStatus() == BookingStatus.PAID) {
            throw new BusinessException("Cannot modify paid booking. Please contact support.");
        }

        // Aktualizacja pasażerów - usunięcie starych i dodanie nowych używając metody addPassengers z UML
        booking.getPassengers().clear();
        if (newPassengers != null && !newPassengers.isEmpty()) {
            List<Passenger> passengers = newPassengers.stream()
                    .map(passengerReq -> {
                        Passenger passenger = new Passenger();
                        passenger.setFirstName(passengerReq.getFirstName());
                        passenger.setLastName(passengerReq.getLastName());
                        passenger.setBirthDate(passengerReq.getBirthDate());
                        passenger.setDocumentNumber(passengerReq.getDocumentNumber());
                        return passenger;
                    })
                    .collect(java.util.stream.Collectors.toList());
            booking.addPassengers(passengers);
        }

        // Aktualizacja dodatków
        if (newExtras != null) {
            booking.setExtraBaggage(newExtras.getExtraBaggage());
            booking.setInsurance(newExtras.getInsurance());
        }

        // Przeliczenie ceny
        booking.calculatePrice();

        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    /**
     * Updates a single passenger in a booking.
     * UML: Pasazer.ZakutalizujDane()
     */
    @Transactional
    public BookingResponse updatePassenger(Long bookingId, Long passengerId, PassengerRequest newData) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.PAID) {
            throw new BusinessException("Cannot update passengers in " + booking.getStatus() + " booking");
        }

        Passenger passenger = booking.getPassengers().stream()
                .filter(p -> p.getId().equals(passengerId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + passengerId));

        // Utworzenie tymczasowego pasażera z nowymi danymi dla metody updateData
        Passenger newDataPassenger = new Passenger();
        newDataPassenger.setFirstName(newData.getFirstName());
        newDataPassenger.setLastName(newData.getLastName());
        newDataPassenger.setBirthDate(newData.getBirthDate());
        newDataPassenger.setDocumentNumber(newData.getDocumentNumber());

        passenger.updateData(newDataPassenger);

        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUser().getId());
        response.setCreatedAt(booking.getCreatedAt());
        response.setStatus(booking.getStatus().name());
        response.setTotalPrice(booking.getPrice()); // Użycie getPrice() z UML
        response.setCurrency(booking.getCurrency());
        response.setExtraBaggage(booking.getExtraBaggage());
        response.setInsurance(booking.getInsurance());

        // Mapowanie podsumowania lotu
        Flight flight = booking.getFlight();
        BookingResponse.FlightSummary flightSummary = new BookingResponse.FlightSummary();
        flightSummary.setId(flight.getId());
        flightSummary.setFlightNumber(flight.getFlightNumber());
        flightSummary.setFromAirport(flight.getFromAirport().getCode());
        flightSummary.setToAirport(flight.getToAirport().getCode());
        flightSummary.setDepartureAt(flight.getDepartureAt());
        flightSummary.setArrivalAt(flight.getArrivalAt());
        response.setFlight(flightSummary);

        // Mapowanie pasażerów
        List<BookingResponse.PassengerInfo> passengerInfos = booking.getPassengers().stream()
                .map(p -> new BookingResponse.PassengerInfo(
                        p.getId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getBirthDate(),
                        p.getDocumentNumber()))
                .collect(Collectors.toList());
        response.setPassengers(passengerInfos);

        return response;
    }

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository,
            UserRepository userRepository, PassengerRepository passengerRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.passengerRepository = passengerRepository;
    }
}
