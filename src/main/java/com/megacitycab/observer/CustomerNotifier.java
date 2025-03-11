package com.megacitycab.observer;

import com.megacitycab.model.Customer;

public class CustomerNotifier implements Observer<Customer> {
    @Override
    public void notify(Customer customer) {
        // Log to server
        System.out.println("[SERVER LOG] Notification sent to customer: " + customer.getName());
    }
}