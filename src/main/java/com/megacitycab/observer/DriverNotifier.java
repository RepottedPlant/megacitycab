package com.megacitycab.observer;

import com.megacitycab.model.Booking;

public class DriverNotifier implements Observer<Booking> {
    @Override
    public void notify(Booking booking) {
        // Log to server
        System.out.println("[SERVER LOG] Notification sent to driver for Booking #" + booking.getId());
    }
}