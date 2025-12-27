package com.flightbooking;

import com.flightbooking.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingFlowIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCompleteBookingFlow_withPaymentSuccess() {
        // Step 1: Create booking
        BookingCreateRequest bookingRequest = new BookingCreateRequest();
        bookingRequest.setFlightId(1L);

        List<PassengerRequest> passengers = new ArrayList<>();
        PassengerRequest passenger = new PassengerRequest();
        passenger.setFirstName("Jan");
        passenger.setLastName("Kowalski");
        passenger.setBirthDate(LocalDate.of(2000, 1, 1));
        passenger.setDocumentNumber("ABC123456");
        passengers.add(passenger);

        bookingRequest.setPassengers(passengers);

        BookingExtrasRequest extras = new BookingExtrasRequest();
        extras.setExtraBaggage(false);
        extras.setInsurance(false);
        bookingRequest.setExtras(extras);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", "1");
        HttpEntity<BookingCreateRequest> entity = new HttpEntity<>(bookingRequest, headers);

        ResponseEntity<BookingCreateResponse> bookingResponse = restTemplate.postForEntity(
                "/api/bookings",
                entity,
                BookingCreateResponse.class);

        // Verify booking creation
        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookingResponse.getBody()).isNotNull();
        assertThat(bookingResponse.getBody().getStatus()).isEqualTo("PENDING_PAYMENT");

        Long bookingId = bookingResponse.getBody().getId();

        // Step 2: Process payment (SUCCESS)
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(bookingId);
        paymentRequest.setMethod("CARD");
        paymentRequest.setOutcome("SUCCESS");

        ResponseEntity<PaymentResponse> paymentResponse = restTemplate.postForEntity(
                "/api/payments/mock",
                paymentRequest,
                PaymentResponse.class);

        // Verify payment
        assertThat(paymentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(paymentResponse.getBody()).isNotNull();
        assertThat(paymentResponse.getBody().getStatus()).isEqualTo("CONFIRMED");
        assertThat(paymentResponse.getBody().getBookingStatus()).isEqualTo("PAID");

        // Step 3: Get booking details
        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<BookingResponse> getBookingResponse = restTemplate.exchange(
                "/api/bookings/" + bookingId,
                HttpMethod.GET,
                getEntity,
                BookingResponse.class);

        // Verify booking status updated
        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getBookingResponse.getBody()).isNotNull();
        assertThat(getBookingResponse.getBody().getStatus()).isEqualTo("PAID");
    }

    @Test
    public void testBookingCancellation() {
        // Step 1: Create booking
        BookingCreateRequest bookingRequest = new BookingCreateRequest();
        bookingRequest.setFlightId(1L);

        List<PassengerRequest> passengers = new ArrayList<>();
        PassengerRequest passenger = new PassengerRequest();
        passenger.setFirstName("Anna");
        passenger.setLastName("Nowak");
        passenger.setBirthDate(LocalDate.of(1995, 5, 15));
        passenger.setDocumentNumber("XYZ789012");
        passengers.add(passenger);

        bookingRequest.setPassengers(passengers);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", "1");
        HttpEntity<BookingCreateRequest> entity = new HttpEntity<>(bookingRequest, headers);

        ResponseEntity<BookingCreateResponse> bookingResponse = restTemplate.postForEntity(
                "/api/bookings",
                entity,
                BookingCreateResponse.class);

        Long bookingId = bookingResponse.getBody().getId();

        // Step 2: Cancel booking
        HttpEntity<Void> cancelEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> cancelResponse = restTemplate.exchange(
                "/api/bookings/" + bookingId + "/cancel",
                HttpMethod.POST,
                cancelEntity,
                Void.class);

        // Verify cancellation
        assertThat(cancelResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Step 3: Get booking and verify status
        ResponseEntity<BookingResponse> getBookingResponse = restTemplate.exchange(
                "/api/bookings/" + bookingId,
                HttpMethod.GET,
                cancelEntity,
                BookingResponse.class);

        assertThat(getBookingResponse.getBody().getStatus()).isEqualTo("CANCELLED");
    }
}
