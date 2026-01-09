package com.flightbooking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "from_airport_code", nullable = false)
    private Airport fromAirport;

    @ManyToOne
    @JoinColumn(name = "to_airport_code", nullable = false)
    private Airport toAirport;

    @Column(name = "departure_at", nullable = false)
    private LocalDateTime departureAt;

    @Column(name = "arrival_at", nullable = false)
    private LocalDateTime arrivalAt;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "baggage_info", columnDefinition = "TEXT")
    private String baggageInfo;

    @Column(name = "change_cancel_rules", columnDefinition = "TEXT")
    private String changeCancelRules;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "travel_class", length = 20)
    private String travelClass; // ECONOMY, BUSINESS

    public Flight() {
    }

    public Flight(Long id, String flightNumber, Airport fromAirport, Airport toAirport, LocalDateTime departureAt,
            LocalDateTime arrivalAt, Integer durationMinutes, BigDecimal basePrice, String currency, String baggageInfo,
            String changeCancelRules, Integer availableSeats, String travelClass) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.durationMinutes = durationMinutes;
        this.basePrice = basePrice;
        this.currency = currency;
        this.baggageInfo = baggageInfo;
        this.changeCancelRules = changeCancelRules;
        this.availableSeats = availableSeats;
        this.travelClass = travelClass;
    }

    // Business Methods from Diagram

    public boolean checkAvailability(int count) {
        return this.availableSeats >= count;
    }

    // Simplified calculation logic - could be expanded
    public BigDecimal calculatePrice(String travelClass, boolean extraBaggage, boolean insurance) {
        BigDecimal price = this.basePrice;

        if ("BUSINESS".equalsIgnoreCase(travelClass)) {
            price = price.multiply(new BigDecimal("2.0"));
        }

        if (extraBaggage) {
            price = price.add(new BigDecimal("100.00"));
        }

        if (insurance) {
            price = price.add(new BigDecimal("50.00"));
        }

        return price;
    }

    // Getters and Setters
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

    public Airport getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(Airport fromAirport) {
        this.fromAirport = fromAirport;
    }

    public Airport getToAirport() {
        return toAirport;
    }

    public void setToAirport(Airport toAirport) {
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
}
