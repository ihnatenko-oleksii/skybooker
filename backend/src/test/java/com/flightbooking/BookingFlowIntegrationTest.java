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

    /**
     * Rejestruje użytkownika (jeśli nie istnieje) i zwraca token JWT do autentykacji.
     */
    private String registerAndGetToken(String login, String email) {
        // Spróbuj najpierw zalogować się (jeśli użytkownik już istnieje)
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin(login);
        loginDto.setPassword("test123");

        ResponseEntity<AuthResponseDto> loginResponse = restTemplate.postForEntity(
                "/api/auth/login",
                loginDto,
                AuthResponseDto.class);

        // Jeśli logowanie się powiodło, zwróć token
        if (loginResponse.getStatusCode() == HttpStatus.OK && loginResponse.getBody() != null 
                && loginResponse.getBody().getAccessToken() != null) {
            return loginResponse.getBody().getAccessToken();
        }

        // Jeśli logowanie się nie powiodło, zarejestruj nowego użytkownika
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstName("Test");
        registerDto.setLastName("User");
        registerDto.setLogin(login);
        registerDto.setEmail(email);
        registerDto.setPassword("test123");
        registerDto.setPhone("123456789");

        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                registerDto,
                String.class);

        // Jeśli rejestracja się powiodła lub użytkownik już istniał, zaloguj się
        if (registerResponse.getStatusCode() == HttpStatus.CREATED || 
            registerResponse.getStatusCode() == HttpStatus.OK) {
            loginResponse = restTemplate.postForEntity(
                    "/api/auth/login",
                    loginDto,
                    AuthResponseDto.class);

            assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(loginResponse.getBody()).isNotNull();
            assertThat(loginResponse.getBody().getAccessToken()).isNotNull();

            return loginResponse.getBody().getAccessToken();
        }

        throw new RuntimeException("Failed to register or login user: " + login);
    }

    /**
     * Tworzy nagłówki HTTP z tokenem JWT.
     */
    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    public void testCompleteBookingFlow_withPaymentSuccess() {
        // Step 0: Register and login to get JWT token (use unique login based on test name and timestamp)
        String uniqueLogin = "testuser1_" + System.currentTimeMillis();
        String uniqueEmail = uniqueLogin + "@test.com";
        String token = registerAndGetToken(uniqueLogin, uniqueEmail);
        HttpHeaders headers = createAuthHeaders(token);

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

        HttpEntity<BookingCreateRequest> entity = new HttpEntity<>(bookingRequest, headers);

        ResponseEntity<BookingCreateResponse> bookingResponse = restTemplate.postForEntity(
                "/api/bookings",
                entity,
                BookingCreateResponse.class);

        // Verify booking creation
        System.out.println("Booking created - Status: " + bookingResponse.getStatusCode() + ", Body: " + bookingResponse.getBody());
        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookingResponse.getBody()).isNotNull();
        assertThat(bookingResponse.getBody().getStatus()).isEqualTo("PENDING_PAYMENT");
        System.out.println("✓ Booking creation verified - ID: " + bookingResponse.getBody().getId() + ", Status: " + bookingResponse.getBody().getStatus());

        Long bookingId = bookingResponse.getBody().getId();

        // Step 2: Process payment (SUCCESS)
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(bookingId);
        paymentRequest.setMethod("CARD");
        paymentRequest.setOutcome("SUCCESS");

        HttpEntity<PaymentRequest> paymentEntity = new HttpEntity<>(paymentRequest, headers);
        ResponseEntity<PaymentResponse> paymentResponse = restTemplate.postForEntity(
                "/api/payments/mock",
                paymentEntity,
                PaymentResponse.class);

        // Verify payment
        System.out.println("Payment processed - Status: " + paymentResponse.getStatusCode() + ", Body: " + paymentResponse.getBody());
        assertThat(paymentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(paymentResponse.getBody()).isNotNull();
        assertThat(paymentResponse.getBody().getStatus()).isEqualTo("CONFIRMED");
        assertThat(paymentResponse.getBody().getBookingStatus()).isEqualTo("PAID");
        System.out.println("✓ Payment verified - Status: " + paymentResponse.getBody().getStatus() + ", Booking Status: " + paymentResponse.getBody().getBookingStatus());

        // Step 3: Get booking details
        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<BookingResponse> getBookingResponse = restTemplate.exchange(
                "/api/bookings/" + bookingId,
                HttpMethod.GET,
                getEntity,
                BookingResponse.class);

        // Verify booking status updated
        System.out.println("Booking retrieved - Status: " + getBookingResponse.getStatusCode() + ", Booking Status: " + (getBookingResponse.getBody() != null ? getBookingResponse.getBody().getStatus() : "null"));
        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getBookingResponse.getBody()).isNotNull();
        assertThat(getBookingResponse.getBody().getStatus()).isEqualTo("PAID");
        System.out.println("✓ Booking status verified - Final Status: " + getBookingResponse.getBody().getStatus());
    }

    @Test
    public void testBookingCancellation() {
        // Step 0: Register and login to get JWT token (use unique login based on test name and timestamp)
        String uniqueLogin = "testuser2_" + System.currentTimeMillis();
        String uniqueEmail = uniqueLogin + "@test.com";
        String token = registerAndGetToken(uniqueLogin, uniqueEmail);
        HttpHeaders headers = createAuthHeaders(token);

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

        BookingExtrasRequest extras = new BookingExtrasRequest();
        extras.setExtraBaggage(false);
        extras.setInsurance(false);
        bookingRequest.setExtras(extras);

        HttpEntity<BookingCreateRequest> entity = new HttpEntity<>(bookingRequest, headers);

        ResponseEntity<BookingCreateResponse> bookingResponse = restTemplate.postForEntity(
                "/api/bookings",
                entity,
                BookingCreateResponse.class);

        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookingResponse.getBody()).isNotNull();
        
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

        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getBookingResponse.getBody()).isNotNull();
        assertThat(getBookingResponse.getBody().getStatus()).isEqualTo("CANCELLED");
    }
}
