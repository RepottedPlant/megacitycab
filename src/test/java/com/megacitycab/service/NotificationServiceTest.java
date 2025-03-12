package com.megacitycab.service;

import com.megacitycab.observer.Observer;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class NotificationServiceTest {

    private NotificationService notificationService;
    private ObserverStub<Customer> customerObserver;
    private ObserverStub<Fleet> fleetObserver;

    @Before
    public void setUp() {
        notificationService = new NotificationService();
        customerObserver = new ObserverStub<>(); // Manual stub for Customer observer
        fleetObserver = new ObserverStub<>(); // Manual stub for Fleet observer
    }

    @Test
    public void testAddObserver() {
        // Arrange
        notificationService.addObserver(Customer.class, customerObserver);

        // Act
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        notificationService.notifyObservers(customer, "A new customer has been created.");

        // Assert
        List<String> notifications = customerObserver.getNotifications();
        assertEquals(1, notifications.size());
        assertEquals("A new customer has been created.:" + customer.toString(), notifications.get(0));
    }

    @Test
    public void testRemoveObserver() {
        // Arrange
        notificationService.addObserver(Customer.class, customerObserver);
        notificationService.removeObserver(Customer.class, customerObserver);

        // Act
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        notificationService.notifyObservers(customer, "A new customer has been created.");

        // Assert
        List<String> notifications = customerObserver.getNotifications();
        assertEquals(0, notifications.size()); // No notifications should be received
    }

    @Test
    public void testNotifyObservers() {
        // Arrange
        notificationService.addObserver(Customer.class, customerObserver);
        notificationService.addObserver(Fleet.class, fleetObserver);

        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        // Act
        notificationService.notifyObservers(customer, "A new customer has been created.");
        notificationService.notifyObservers(fleet, "Fleet details have been updated.");

        // Assert
        List<String> customerNotifications = customerObserver.getNotifications();
        List<String> fleetNotifications = fleetObserver.getNotifications();

        assertEquals(1, customerNotifications.size());
        assertEquals("A new customer has been created.:" + customer.toString(), customerNotifications.get(0));

        assertEquals(1, fleetNotifications.size());
        assertEquals("Fleet details have been updated.:" + fleet.toString(), fleetNotifications.get(0));
    }

    @Test
    public void testNotifyObservers_NoObservers() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");

        // Act
        notificationService.notifyObservers(customer, "A new customer has been created.");

        // Assert
        List<String> customerNotifications = customerObserver.getNotifications();
        assertEquals(0, customerNotifications.size()); // No notifications should be received
    }

    // Manual stub for Observer
    static class ObserverStub<T> implements Observer<T> {
        private List<String> notifications = new ArrayList<>();

        @Override
        public void notify(T entity, String message) {
            notifications.add(message + ":" + entity.toString()); // Simulate notification
        }

        List<String> getNotifications() {
            return notifications;
        }
    }
}