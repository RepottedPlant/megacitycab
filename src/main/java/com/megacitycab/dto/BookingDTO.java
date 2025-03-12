package com.megacitycab.dto;

public class BookingDTO {
    private int customerId;
    private int fleetId;
    private String customerName;
    private String nic;
    private String pickup;
    private String destination;
    private double distance;

    // Constructors
    public BookingDTO() {
    }

    public BookingDTO(int customerId, int fleetId, String customerName, String nic,
                      String pickup, String destination, double distance) {
        this.customerId = customerId;
        this.fleetId = fleetId;
        this.customerName = customerName;
        this.nic = nic;
        this.pickup = pickup;
        this.destination = destination;
        this.distance = distance;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getFleetId() {
        return fleetId;
    }

    public void setFleetId(int fleetId) {
        this.fleetId = fleetId;
    }

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = Math.round(distance * 100.0) / 100.0;
    }
}