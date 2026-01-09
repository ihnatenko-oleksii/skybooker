package com.flightbooking.service;

import com.flightbooking.dto.PaymentRequest;
import com.flightbooking.dto.PaymentResponse;
import com.flightbooking.entity.Booking;
import com.flightbooking.entity.BookingStatus;
import com.flightbooking.entity.Payment;
import com.flightbooking.entity.PaymentStatus;
import com.flightbooking.exception.BusinessException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public PaymentResponse processMockPayment(PaymentRequest request) {
        // Fetch booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        // Walidacja statusu rezerwacji
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new BusinessException(
                    "Booking is not in PENDING_PAYMENT status. Current status: " + booking.getStatus());
        }

        // Utworzenie lub aktualizacja płatności
        Payment payment = paymentRepository.findByBookingId(booking.getId())
                .orElse(new Payment());

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setCurrency(booking.getCurrency());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setMethod(request.getMethod());
        payment.setExternalTxId("MOCK-" + UUID.randomUUID().toString());

        // Rozpoczęcie płatności używając metody z UML
        payment.startPayment();

        // Przetwarzanie na podstawie wyniku używając metod z UML
        PaymentResponse response = new PaymentResponse();

        if ("SUCCESS".equalsIgnoreCase(request.getOutcome())) {
            payment.markAsConfirmed(); // Użycie metody z UML
            booking.setStatus(BookingStatus.PAID); // Użycie setStatus z walidacją
            response.setStatus("CONFIRMED");
            response.setBookingStatus("PAID");
        } else if ("FAIL".equalsIgnoreCase(request.getOutcome())) {
            payment.markAsRejected(); // Użycie metody z UML
            booking.setStatus(BookingStatus.PAYMENT_FAILED); // Użycie setStatus z walidacją
            response.setStatus("REJECTED");
            response.setBookingStatus("PAYMENT_FAILED");
        } else {
            throw new BusinessException("Invalid outcome. Must be SUCCESS or FAIL");
        }

        // Zapisanie płatności i rezerwacji
        payment = paymentRepository.save(payment);
        bookingRepository.save(booking);

        response.setPaymentId(payment.getId());

        return response;
    }


    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }
}
