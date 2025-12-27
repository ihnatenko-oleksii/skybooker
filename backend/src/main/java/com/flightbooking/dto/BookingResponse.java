package com.flightbooking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private String status;
    private BigDecimal totalPrice;
    private String currency;
    private Boolean extraBaggage;
    private Boolean insurance;
    private FlightSummary flight;
    private List<PassengerInfo> passengers;

    public BookingResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public Boolean getExtraBaggage() {
        return extraBaggage;
    }

    public void setExtraBaggage(Boolean extraBaggage) {
        this.extraBaggage = extraBaggage;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public FlightSummary getFlight() {
        return flight;
    }

    public void setFlight(FlightSummary flight) {
        this.flight = flight;
    }

    public List<PassengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerInfo> passengers) {
        this.passengers = passengers;
    }

    public static class FlightSummary {
        private Long id;
        private String flightNumber;
        private String fromAirport;
        private String toAirport;
        private LocalDateTime departureAt;
        private LocalDateTime arrivalAt;

        public FlightSummary() {
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
    }

    public static class PassengerInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String documentNumber;

        public PassengerInfo() {
        }

        public PassengerInfo(Long id, String firstName, String lastName, LocalDate birthDate, String documentNumber) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.documentNumber = documentNumber;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public String getDocumentNumber() {
            return documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
        }
    }
}
