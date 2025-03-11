package com.megacitycab.dto;

import com.megacitycab.model.Booking;
import com.megacitycab.model.Billing;

public class BookingBillingDTO {
    private Booking booking;
    private Billing billing;

    public BookingBillingDTO(Booking booking, Billing billing) {
        this.booking = booking;
        this.billing = billing;
    }

    // Getters and Setters
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }
}
