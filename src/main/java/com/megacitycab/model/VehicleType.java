package com.megacitycab.model;

public enum VehicleType {
    ZIP(500.00, 15.00),     // Base: LKR500, Rate/km: LKR15
    PRO(800.00, 20.00),     // Base: LKR800, Rate/km: LKR20
    BLACK(1000.00, 25.00);    // Base: LKR1000, Rate/km: LKR25

    private final double basePrice;
    private final double ratePerKm;

    VehicleType(double basePrice, double ratePerKm) {
        this.basePrice = basePrice;
        this.ratePerKm = ratePerKm;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getRatePerKm() {
        return ratePerKm;
    }
}