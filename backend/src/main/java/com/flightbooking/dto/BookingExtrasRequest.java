package com.flightbooking.dto;

public class BookingExtrasRequest {
    private Boolean extraBaggage;
    private Boolean insurance;

    public BookingExtrasRequest() {
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
}
