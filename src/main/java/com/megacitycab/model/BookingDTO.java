// src/main/java/com/megacitycab/model/BookingDTO.java
package com.megacitycab.model;

public class BookingDTO {
    private String customerName;
    private String nic;
    private String pickup;
    private String destination;

    // Constructors
    public BookingDTO() {}

    public BookingDTO(String customerName, String nic, String pickup, String destination) {
        this.customerName = customerName;
        this.nic = nic;
        this.pickup = pickup;
        this.destination = destination;
    }

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
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
}