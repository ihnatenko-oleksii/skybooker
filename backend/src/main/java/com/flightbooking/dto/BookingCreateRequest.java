package com.flightbooking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BookingCreateRequest {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerRequest> passengers;

    private BookingExtrasRequest extras;


    public BookingCreateRequest() {
    }

    public BookingCreateRequest(Long flightId, List<PassengerRequest> passengers, BookingExtrasRequest extras) {
        this.flightId = flightId;
        this.passengers = passengers;
        this.extras = extras;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }

    public BookingExtrasRequest getExtras() {
        return extras;
    }

    public void setExtras(BookingExtrasRequest extras) {
        this.extras = extras;
    }
}
