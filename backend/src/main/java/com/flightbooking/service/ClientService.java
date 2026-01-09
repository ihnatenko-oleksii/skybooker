package com.flightbooking.service;

import com.flightbooking.dto.BookingCreateRequest;
import com.flightbooking.dto.BookingCreateResponse;
import com.flightbooking.dto.BookingExtrasRequest;
import com.flightbooking.dto.BookingResponse;
import com.flightbooking.dto.FlightResponse;
import com.flightbooking.dto.PassengerRequest;
import com.flightbooking.entity.Passenger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Serwis implementujący metody biznesowe Klienta z diagramu UML.
 * Deleguje do istniejących serwisów (FlightService, BookingService) tam gdzie to odpowiednie.
 */
@Service
public class ClientService {

    private final FlightService flightService;
    private final BookingService bookingService;

    public ClientService(FlightService flightService, BookingService bookingService) {
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    /**
     * Tworzy obiekt Passenger (nie zapisuje do bazy danych).
     * UML: Klient.utworzPasazera(imie, nazwisko, dataUrodzenia, numerDokumentu)
     */
    public Passenger createPassenger(String firstName, String lastName, LocalDate birthDate, String documentNumber) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setBirthDate(birthDate);
        passenger.setDocumentNumber(documentNumber);
        return passenger;
    }

    /**
     * Wyszukuje loty.
     * UML: Klient.wyszukajLoty(z:Lotnisko, do:Lotnisko, data:DateOnly)
     */
    @Transactional(readOnly = true)
    public List<FlightResponse> searchFlights(String from, String to, LocalDate date, Integer passengers, String travelClass) {
        return flightService.searchFlights(from, to, date, passengers, travelClass);
    }

    /**
     * Tworzy rezerwację.
     * UML: Klient.utworzRezerwacje(lot:Lot, danePasazerow:List<Pasazer>)
     */
    @Transactional
    public BookingCreateResponse createReservation(Long userId, Long flightId, List<PassengerRequest> passengers, BookingExtrasRequest extras) {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setFlightId(flightId);
        request.setPassengers(passengers);
        request.setExtras(extras);
        
        return bookingService.createBooking(userId, request);
    }

    /**
     * Wyświetla rezerwacje użytkownika.
     * UML: Klient.wyswietlRezerwacje()
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> viewReservations(Long userId) {
        return bookingService.getUserBookings(userId);
    }

    /**
     * Anuluje rezerwację.
     * UML: Klient.anulujRezerwacje(rezerwacja:Rezerwacja)
     */
    @Transactional
    public void cancelReservation(Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    /**
     * Modyfikuje rezerwację (zmienia pasażerów lub dodatki).
     * UML: Klient.zmienRezerwacje(staraRezerwacja:Rezerwacja, nowaRezerwacja:Rezerwacja)
     */
    @Transactional
    public BookingResponse modifyReservation(Long bookingId, List<PassengerRequest> newPassengers, BookingExtrasRequest newExtras) {
        return bookingService.modifyBooking(bookingId, newPassengers, newExtras);
    }

    /**
     * Aktualizuje pasażera w rezerwacji.
     * UML: Pasazer.ZakutalizujDane()
     */
    @Transactional
    public BookingResponse updatePassenger(Long bookingId, Long passengerId, PassengerRequest newData) {
        return bookingService.updatePassenger(bookingId, passengerId, newData);
    }
}
