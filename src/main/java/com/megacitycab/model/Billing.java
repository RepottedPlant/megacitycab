package com.megacitycab.model;

public class Billing {
    private int id;
    private Booking booking;
    private double baseFare;
    private double tax;
    private double total;
    private String pricingType; // New field to track pricing strategy

    // Constructors
    public Billing() {}

    public Billing(int id, Booking booking, double baseFare, double tax, double total, String pricingType) {
        this.id = id;
        this.booking = booking;
        this.baseFare = baseFare;
        this.tax = tax;
        this.total = total;
        this.pricingType = pricingType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Booking getBooking() {
        return booking;
    }
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    public double getBaseFare() {
        return baseFare;
    }
    public void setBaseFare(double baseFare) {
        this.baseFare = Math.round(baseFare * 100.0) / 100.0; // Round to 2 decimal places
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = Math.round(tax * 100.0) / 100.0; // Round to 2 decimal places
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = Math.round(total * 100.0) / 100.0; // Round to 2 decimal places
    }
    public String getPricingType() {
        return pricingType;
    }
    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }
}
