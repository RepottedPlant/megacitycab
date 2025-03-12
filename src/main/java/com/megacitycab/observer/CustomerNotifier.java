package com.megacitycab.observer;

import com.megacitycab.model.Customer;

public class CustomerNotifier implements Observer<Customer> {
    @Override
    public void notify(Customer customer, String message) {
        System.out.println("[SERVER LOG] Notification sent to customer: " + message + " Name: " + customer.getName());
    }
}