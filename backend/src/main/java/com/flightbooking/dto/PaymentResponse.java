package com.flightbooking.dto;


public class PaymentResponse {
    private Long paymentId;
    private String status;
    private String bookingStatus;


    public PaymentResponse() {
    }

    public PaymentResponse(Long paymentId, String status, String bookingStatus) {
        this.paymentId = paymentId;
        this.status = status;
        this.bookingStatus = bookingStatus;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
