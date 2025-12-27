package com.flightbooking.service;

import com.flightbooking.dto.*;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BusinessException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
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

    @Transactional
    public BookingCreateResponse createBooking(Long userId, BookingCreateRequest request) {
        // Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Fetch flight
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + request.getFlightId()));

        // Check availability
        if (!flight.checkAvailability(request.getPassengers().size())) {
            throw new BusinessException("Not enough available seats. Available: " + flight.getAvailableSeats());
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        booking.setCurrency(flight.getCurrency());

        // Set extras
        BookingExtrasRequest extras = request.getExtras();
        if (extras != null) {
            booking.setExtraBaggage(extras.getExtraBaggage());
            booking.setInsurance(extras.getInsurance());
        }

        // Add passengers
        for (PassengerRequest passengerReq : request.getPassengers()) {
            Passenger passenger = new Passenger();
            passenger.setFirstName(passengerReq.getFirstName());
            passenger.setLastName(passengerReq.getLastName());
            passenger.setBirthDate(passengerReq.getBirthDate());
            passenger.setDocumentNumber(passengerReq.getDocumentNumber());
            booking.addPassenger(passenger);
        }

        // Calculate total price using Domain Logic
        booking.calculatePrice();

        // Save booking
        booking = bookingRepository.save(booking);

        // Return response
        BookingCreateResponse response = new BookingCreateResponse();
        response.setId(booking.getId());
        response.setStatus(booking.getStatus().name());
        response.setTotalPrice(booking.getTotalPrice());
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

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUser().getId());
        response.setCreatedAt(booking.getCreatedAt());
        response.setStatus(booking.getStatus().name());
        response.setTotalPrice(booking.getTotalPrice());
        response.setCurrency(booking.getCurrency());
        response.setExtraBaggage(booking.getExtraBaggage());
        response.setInsurance(booking.getInsurance());

        // Map flight summary
        Flight flight = booking.getFlight();
        BookingResponse.FlightSummary flightSummary = new BookingResponse.FlightSummary();
        flightSummary.setId(flight.getId());
        flightSummary.setFlightNumber(flight.getFlightNumber());
        flightSummary.setFromAirport(flight.getFromAirport().getCode());
        flightSummary.setToAirport(flight.getToAirport().getCode());
        flightSummary.setDepartureAt(flight.getDepartureAt());
        flightSummary.setArrivalAt(flight.getArrivalAt());
        response.setFlight(flightSummary);

        // Map passengers
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
            UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }
}
