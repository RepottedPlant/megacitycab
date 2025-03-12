package com.megacitycab.service;

import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.model.Customer;
import com.megacitycab.observer.CustomerNotifier;

import java.util.List;

public class CustomerService {
    private final CustomerDAO customerDao;
    private final NotificationService notificationService;

    // Constructor Injection (DIP)
    public CustomerService(CustomerDAO customerDao, NotificationService notificationService) {

        this.customerDao = customerDao;
        this.notificationService = notificationService;

        // Register the CustomerNotifier observer
        notificationService.addObserver(Customer.class, new CustomerNotifier());

    }

    // Create a new customer
    public Customer createCustomer(Customer customer) {

        try {
            // Validate customer data
            validateCustomer(customer);


            // Save the customer to the database

            Customer savedCustomer = customerDao.save(customer);

            // Notify observers (e.g., send welcome email)

            notificationService.notifyObservers(savedCustomer, "Thank you for choosing Mega City Cabs " +savedCustomer.getName());


            return savedCustomer;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Update an existing customer
    public boolean updateCustomer(Customer customer) {

        try {
            // Validate customer data
            validateCustomer(customer);


            // Update the customer in the database

            boolean isUpdated = customerDao.update(customer);
            if (isUpdated) {
                // Notify observers (e.g., send update confirmation)
                notificationService.notifyObservers(customer, "Your details have been updated " +customer.getName());

            } else {
                throw new IllegalArgumentException("Failed to update customer.");
            }

            return isUpdated;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Delete a customer by ID
    public boolean deleteCustomer(int id) {
        try {
            // Validate customer ID
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid customer ID.");
            }

            // Fetch the customer's name before deletion
            String deletedCustomerName = customerDao.findById(id).getName();

            // Delete the customer from the database
            boolean isDeleted = customerDao.delete(id);
            if (isDeleted) {
                // Notify observers (e.g., send deletion confirmation)
                Customer deletedCustomer = new Customer();
                deletedCustomer.setId(id);
                notificationService.notifyObservers(deletedCustomer, "You have been removed from our database " + deletedCustomerName);
            } else {
                throw new IllegalArgumentException("Failed to delete customer.");
            }

            return isDeleted;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Find a customer by ID
    public Customer findCustomerById(int id) {

        try {
            // Validate customer ID
            if (id <= 0) {

                throw new IllegalArgumentException("Invalid customer ID.");
            }

            // Retrieve the customer from the database

            Customer customer = customerDao.findById(id);
            if (customer != null) {
                System.out.println("Customer found: " + customer.getName());
            } else {
                throw new IllegalArgumentException("Customer not found with ID.");
            }

            return customer;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Retrieve all customers
    public List<Customer> findAllCustomers() {

        try {
            // Retrieve all customers from the database

            List<Customer> customers = customerDao.findAll();

            return customers;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Search customers by name or phone
    public List<Customer> searchCustomers(String searchQuery) {

        try {
            // Validate search query
            if (searchQuery == null || searchQuery.trim().isEmpty()) {

                return findAllCustomers();
            }

            // Search customers in the database

            List<Customer> customers = customerDao.findByNameOrPhone(searchQuery);

            return customers;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Validate customer data
    private void validateCustomer(Customer customer) {

        try {
            if (customer == null) {

                throw new IllegalArgumentException("Customer cannot be null.");
            }
            if (customer.getName() == null || customer.getName().trim().isEmpty()) {

                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {

                throw new IllegalArgumentException("Customer phone cannot be empty.");
            }
            if (customer.getNic() == null || customer.getNic().trim().isEmpty()) {

                throw new IllegalArgumentException("Customer NIC cannot be empty.");
            }
            if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {

                throw new IllegalArgumentException("Customer address cannot be empty.");
            }

        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }
}