// src/main/java/com/megacitycab/model/Booking.java
package com.megacitycab.model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private Customer customer;
    private Fleet fleet;
    private String pickup;
    private String destination;
    private double distance;
    private LocalDateTime bookingDate;

    // Constructors
    public Booking() {}

    public Booking(int id, Customer customer, Fleet fleet, String pickup, String destination, double distance, LocalDateTime bookingDate) {
        this.id = id;
        this.customer = customer;
        this.fleet = fleet;
        this.pickup = pickup;
        this.destination = destination;
        this.distance = distance;
        this.bookingDate = bookingDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}