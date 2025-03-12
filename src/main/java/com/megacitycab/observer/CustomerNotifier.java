package com.megacitycab.observer;

import com.megacitycab.model.Customer;

public class CustomerNotifier implements Observer<Customer> {
    @Override
    public void notify(Customer customer, String eventType) {
        switch (eventType) {
            case "CREATE":
                System.out.println("[SERVER LOG] Notification sent to customer: A new account has been created for " + customer.getName());
                break;
            case "UPDATE":
                System.out.println("[SERVER LOG] Notification sent to customer: Your account details have been updated. Name: " + customer.getName());
                break;
            case "DELETE":
                System.out.println("[SERVER LOG] Notification sent to customer: Your account has been deleted. Name: " + customer.getName());
                break;
            default:
                System.out.println("[SERVER LOG] Unknown event type for customer: " + eventType);
                break;
        }
    }
}