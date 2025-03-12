package com.megacitycab.dto;

public class DriverRevenueDTO {
    private String driverName;
    private double revenue;

    public DriverRevenueDTO(String driverName, double revenue) {
        this.driverName = driverName;
        this.revenue = revenue;
    }

    public String getDriverName() {
        return driverName;
    }

    public double getRevenue() {
        return revenue;
    }
}
