package com.megacitycab.dto;

public class CustomerRevenueDTO {
    private int customerId;
    private String customerName;
    private double revenue;

    public CustomerRevenueDTO(int customerId, String customerName, double revenue) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.revenue = revenue;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getRevenue() {
        return revenue;
    }
}
