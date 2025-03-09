package com.megacitycab.observer;

import com.megacitycab.model.Booking;

public class SMSNotifier implements BookingObserver {
    @Override
    public void notify(Booking booking) {
        // Log to server
        System.out.println("[SERVER LOG] SMS notification sent to driver for Booking #" + booking.getId());
    }
}