package com.flightbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FlightCreateRequest {
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "From airport code is required")
    private String fromAirportCode;

    @NotBlank(message = "To airport code is required")
    private String toAirportCode;

    @NotNull(message = "Departure date is required")
    private LocalDateTime departureAt;

    @NotNull(message = "Arrival date is required")
    private LocalDateTime arrivalAt;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Available seats is required")
    @PositiveOrZero(message = "Available seats must be positive or zero")
    private Integer availableSeats;

    private String travelClass;
    private String baggageInfo;
    private String changeCancelRules;

    public FlightCreateRequest() {
    }

    // Getters and Setters
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFromAirportCode() {
        return fromAirportCode;
    }

    public void setFromAirportCode(String fromAirportCode) {
        this.fromAirportCode = fromAirportCode;
    }

    public String getToAirportCode() {
        return toAirportCode;
    }

    public void setToAirportCode(String toAirportCode) {
        this.toAirportCode = toAirportCode;
    }

    public LocalDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(LocalDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public LocalDateTime getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(LocalDateTime arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public String getBaggageInfo() {
        return baggageInfo;
    }

    public void setBaggageInfo(String baggageInfo) {
        this.baggageInfo = baggageInfo;
    }

    public String getChangeCancelRules() {
        return changeCancelRules;
    }

    public void setChangeCancelRules(String changeCancelRules) {
        this.changeCancelRules = changeCancelRules;
    }
}
