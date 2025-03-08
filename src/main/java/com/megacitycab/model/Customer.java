package com.megacitycab.model;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String nic; // National Identity Card
    private String telephone;

    // Constructors
    public Customer() {}

    public Customer(int id, String name, String address, String nic, String telephone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.nic = nic;
        this.telephone = telephone;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}