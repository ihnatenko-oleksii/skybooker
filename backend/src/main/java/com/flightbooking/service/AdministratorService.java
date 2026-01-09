package com.flightbooking.service;

import com.flightbooking.dto.BookingResponse;
import com.flightbooking.dto.FlightCreateRequest;
import com.flightbooking.dto.FlightResponse;
import com.flightbooking.dto.UserCreateRequest;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BusinessException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serwis implementujący metody biznesowe Administratora z diagramu UML.
 */
@Service
public class AdministratorService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministratorService(FlightRepository flightRepository,
                                 AirportRepository airportRepository,
                                 BookingService bookingService,
                                 UserRepository userRepository,
                                 BookingRepository bookingRepository,
                                 PasswordEncoder passwordEncoder) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Dodaje nowy lot.
     * UML: Administrator.dodajLot(cel: Lot)
     */
    @Transactional
    public FlightResponse addFlight(FlightCreateRequest request) {
        // Walidacja czy lotniska istnieją
        Airport fromAirport = airportRepository.findById(request.getFromAirportCode())
                .orElseThrow(() -> new ResourceNotFoundException("From airport not found: " + request.getFromAirportCode()));
        
        Airport toAirport = airportRepository.findById(request.getToAirportCode())
                .orElseThrow(() -> new ResourceNotFoundException("To airport not found: " + request.getToAirportCode()));

        // Walidacja dat
        if (request.getDepartureAt().isAfter(request.getArrivalAt())) {
            throw new BusinessException("Data odlotu musi być przed datą przylotu");
        }

        // Utworzenie lotu
        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setFromAirport(fromAirport);
        flight.setToAirport(toAirport);
        flight.setDepartureAt(request.getDepartureAt());
        flight.setArrivalAt(request.getArrivalAt());
        flight.setDurationMinutes(request.getDurationMinutes());
        flight.setBasePrice(request.getBasePrice());
        flight.setCurrency(request.getCurrency());
        flight.setAvailableSeats(request.getAvailableSeats());
        flight.setTravelClass(request.getTravelClass());
        flight.setBaggageInfo(request.getBaggageInfo());
        flight.setChangeCancelRules(request.getChangeCancelRules());

        flight = flightRepository.save(flight);
        return mapToResponse(flight);
    }

    /**
     * Edytuje istniejący lot.
     * UML: Administrator.edytujLot(idLotu: int, noweDane: Lot)
     */
    @Transactional
    public FlightResponse editFlight(Long flightId, FlightCreateRequest request) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Lot nie znaleziony o id: " + flightId));

        // Walidacja czy lotniska istnieją
        Airport fromAirport = airportRepository.findById(request.getFromAirportCode())
                .orElseThrow(() -> new ResourceNotFoundException("From airport not found: " + request.getFromAirportCode()));
        
        Airport toAirport = airportRepository.findById(request.getToAirportCode())
                .orElseThrow(() -> new ResourceNotFoundException("To airport not found: " + request.getToAirportCode()));

        // Walidacja dat
        if (request.getDepartureAt().isAfter(request.getArrivalAt())) {
            throw new BusinessException("Data odlotu musi być przed datą przylotu");
        }

        // Sprawdzenie czy lot ma aktywne rezerwacje
        long activeBookings = bookingRepository.countByFlightIdAndStatusNot(flightId, BookingStatus.CANCELLED);
        if (activeBookings > 0 && !request.getFromAirportCode().equals(flight.getFromAirport().getCode())) {
            throw new BusinessException("Cannot change departure airport - flight has active bookings");
        }
        if (activeBookings > 0 && !request.getToAirportCode().equals(flight.getToAirport().getCode())) {
            throw new BusinessException("Cannot change arrival airport - flight has active bookings");
        }

        // Aktualizacja lotu
        flight.setFlightNumber(request.getFlightNumber());
        flight.setFromAirport(fromAirport);
        flight.setToAirport(toAirport);
        flight.setDepartureAt(request.getDepartureAt());
        flight.setArrivalAt(request.getArrivalAt());
        flight.setDurationMinutes(request.getDurationMinutes());
        flight.setBasePrice(request.getBasePrice());
        flight.setCurrency(request.getCurrency());
        flight.setAvailableSeats(request.getAvailableSeats());
        flight.setTravelClass(request.getTravelClass());
        flight.setBaggageInfo(request.getBaggageInfo());
        flight.setChangeCancelRules(request.getChangeCancelRules());

        flight = flightRepository.save(flight);
        return mapToResponse(flight);
    }

    /**
     * Usuwa lot.
     * UML: Administrator.usunLot(idLotu: int)
     */
    @Transactional
    public void removeFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Lot nie znaleziony o id: " + flightId));

        // Sprawdzenie czy lot ma aktywne rezerwacje
        long activeBookings = bookingRepository.countByFlightIdAndStatusNot(flightId, BookingStatus.CANCELLED);
        if (activeBookings > 0) {
            throw new BusinessException("Cannot delete flight - it has " + activeBookings + " active booking(s)");
        }

        flightRepository.delete(flight);
    }

    /**
     * Przegląda rezerwację.
     * UML: Administrator.przegladajRezerwacje(int idRezerwacji)
     */
    @Transactional(readOnly = true)
    public BookingResponse viewReservation(Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    /**
     * Dodaje nowego użytkownika (Client lub Administrator).
     * UML: Administrator.dodajUzytkownika(nowy: UzytkownikSystemu)
     */
    @Transactional
    public User addUser(UserCreateRequest request) {
        // Sprawdzenie czy login już istnieje
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new BusinessException("Login is already taken: " + request.getLogin());
        }

        // Sprawdzenie czy email już istnieje
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already taken: " + request.getEmail());
        }

        User user;
        if ("ADMIN".equalsIgnoreCase(request.getUserType())) {
            user = new Administrator();
        } else {
            user = new Client();
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Edytuje dane użytkownika.
     * UML: Administrator.edytujUzytkownika(idUzytkownika: int, noweDane: UzytkownikSystemu)
     */
    @Transactional
    public User editUser(Long userId, UserCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie znaleziony o id: " + userId));

        // Sprawdzenie czy login jest zmieniany i czy nowy login jest dostępny
        if (!user.getLogin().equals(request.getLogin()) && userRepository.existsByLogin(request.getLogin())) {
            throw new BusinessException("Login is already taken: " + request.getLogin());
        }

        // Check if email is being changed and if new email is available
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already taken: " + request.getEmail());
        }

        // Update user data
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPhone(request.getPhone());

        // Aktualizacja hasła jeśli podano
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Usuwa użytkownika.
     * UML: Administrator.usunUzytkownika(id: int)
     */
    @Transactional
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie znaleziony o id: " + userId));

        // Sprawdzenie czy użytkownik ma aktywne rezerwacje
        long activeBookings = bookingRepository.countByUserIdAndStatusNot(userId, BookingStatus.CANCELLED);
        if (activeBookings > 0) {
            throw new BusinessException("Cannot delete user - user has " + activeBookings + " active booking(s)");
        }

        userRepository.delete(user);
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
}
