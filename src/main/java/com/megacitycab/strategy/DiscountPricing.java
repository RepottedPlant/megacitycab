package com.megacitycab.strategy;

import com.megacitycab.model.Booking;
import com.megacitycab.model.VehicleType;

public class DiscountPricing implements PricingStrategy {
    @Override
    public double calculateTotal(Booking booking) {
        VehicleType type = booking.getFleet().getVehicleType();
        double base = type.getBasePrice();
        double rate = type.getRatePerKm();
        double distance = booking.getDistance();
        double subtotal = base + (rate * distance);
        return subtotal * 0.90; // 10% discount
    }
}
