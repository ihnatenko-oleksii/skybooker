package com.flightbooking.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FlightResponse {
    private Long id;
    private String flightNumber;
    private String fromAirport;
    private String toAirport;
    private LocalDateTime departureAt;
    private LocalDateTime arrivalAt;
    private Integer durationMinutes;
    private BigDecimal price;
    private String currency;
    private Integer availableSeats;
    private String travelClass;
    private String baggageInfo;
    private String changeCancelRules;


    public FlightResponse() {
    }

    public FlightResponse(Long id, String flightNumber, String fromAirport, String toAirport, LocalDateTime departureAt, LocalDateTime arrivalAt, Integer durationMinutes, BigDecimal price, String currency, Integer availableSeats, String travelClass, String baggageInfo, String changeCancelRules) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.currency = currency;
        this.availableSeats = availableSeats;
        this.travelClass = travelClass;
        this.baggageInfo = baggageInfo;
        this.changeCancelRules = changeCancelRules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
