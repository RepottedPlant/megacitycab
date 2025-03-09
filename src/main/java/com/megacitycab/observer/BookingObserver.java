package com.megacitycab.observer;

import com.megacitycab.model.Booking;

public interface BookingObserver {
    void notify(Booking booking);
}