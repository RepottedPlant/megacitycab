package com.megacitycab.strategy;

import com.megacitycab.model.Booking;

public interface PricingStrategy {
    double calculateTotal(Booking booking);
}