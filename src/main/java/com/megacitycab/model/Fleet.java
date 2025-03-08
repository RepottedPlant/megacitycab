// src/main/java/com/megacitycab/model/Fleet.java
package com.megacitycab.model;

public class Fleet {
    private int id;
    private String driverName;
    private String vehicleType;
    private String plateNumber;
    private String phoneNumber;

    // Constructors
    public Fleet() {}

    public Fleet(int id, String driverName, String vehicleType, String plateNumber, String phoneNumber) {
        this.id = id;
        this.driverName = driverName;
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}