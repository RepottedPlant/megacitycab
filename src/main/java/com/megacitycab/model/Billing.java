package com.megacitycab.model;

public class Billing {
    private int id;
    private Booking booking;
    private double baseFare;
    private double tax;
    private double discount;
    private double total;
    private String pricingType; // New field to track pricing strategy

    // Constructors
    public Billing() {}

    public Billing(int id, Booking booking, double baseFare, double tax, double discount, double total, String pricingType) {
        this.id = id;
        this.booking = booking;
        this.baseFare = baseFare;
        this.tax = tax;
        this.discount = discount;
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
        this.baseFare = baseFare;
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public String getPricingType() {
        return pricingType;
    }
    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }
}
