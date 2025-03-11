// src/main/java/com/megacitycab/model/Fleet.java
package com.megacitycab.model;

public class Fleet {
    private int id;
    private String driverName;
    private VehicleType vehicleType;
    private String plateNumber;
    private String driverContact;


    // Constructors
    public Fleet() {}

    public Fleet(int id, String driverName, VehicleType vehicleType, String plateNumber, String driverContact) {
        this.id = id;
        this.driverName = driverName;
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.driverContact = driverContact;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }
}