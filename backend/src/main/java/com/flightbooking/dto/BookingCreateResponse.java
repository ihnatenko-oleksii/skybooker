package com.flightbooking.dto;


import java.math.BigDecimal;

public class BookingCreateResponse {
    private Long id;
    private String status;
    private BigDecimal totalPrice;
    private String currency;


    public BookingCreateResponse() {
    }

    public BookingCreateResponse(Long id, String status, BigDecimal totalPrice, String currency) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
