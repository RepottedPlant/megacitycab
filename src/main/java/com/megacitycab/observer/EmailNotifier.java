package com.megacitycab.observer;

import com.megacitycab.model.Booking;

public class EmailNotifier implements BookingObserver {
    @Override
    public void notify(Booking booking) {
        // Log to server
        System.out.println("[SERVER LOG] Email notification sent to customer for Booking #" + booking.getId());
    }
}