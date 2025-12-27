package com.flightbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotBlank(message = "Payment method is required")
    private String method;

    @NotBlank(message = "Outcome is required (SUCCESS or FAIL)")
    private String outcome; // SUCCESS or FAIL


    public PaymentRequest() {
    }

    public PaymentRequest(Long bookingId, String method, String outcome) {
        this.bookingId = bookingId;
        this.method = method;
        this.outcome = outcome;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
