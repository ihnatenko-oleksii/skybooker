package com.flightbooking.service;

import com.flightbooking.entity.Booking;
import com.flightbooking.entity.Client;
import com.flightbooking.entity.InvoiceData;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serwis do generowania faktur.
 * UML: DaneDoFaktury.WygenerujFakture()
 */
@Service
public class InvoiceService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public InvoiceService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Generuje fakturę dla konkretnej rezerwacji.
     */
    @Transactional(readOnly = true)
    public String generateInvoiceForBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rezerwacja nie znaleziona o id: " + bookingId));

        Client client = (Client) booking.getUser();
        InvoiceData invoiceData = client.getInvoiceData();

        if (invoiceData == null) {
            // Utworzenie tymczasowych danych faktury do generowania
            invoiceData = new InvoiceData();
            java.util.ArrayList<Booking> products = new java.util.ArrayList<>();
            products.add(booking);
            invoiceData.setProducts(products);
            if (booking.getPayment() != null) {
                invoiceData.setPayment(booking.getPayment());
            }
        }

        return invoiceData.generateInvoice();
    }

    /**
     * Generuje fakturę dla wszystkich rezerwacji klienta.
     */
    @Transactional(readOnly = true)
    public String generateInvoiceForClient(Long clientId) {
        Client client = (Client) userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Klient nie znaleziony o id: " + clientId));

        InvoiceData invoiceData = client.getInvoiceData();
        if (invoiceData == null) {
            throw new ResourceNotFoundException("Invoice data not found for client: " + clientId);
        }

        return invoiceData.generateInvoice();
    }
}
