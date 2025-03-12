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
        System.out.println("[DEBUG] Initializing CustomerService with CustomerDAO and NotificationService.");
        this.customerDao = customerDao;
        this.notificationService = notificationService;

        // Register the CustomerNotifier observer
        notificationService.addObserver(Customer.class, new CustomerNotifier());
        System.out.println("[DEBUG] CustomerNotifier observer registered.");
    }

    // Create a new customer
    public Customer createCustomer(Customer customer) {
        System.out.println("[DEBUG] Entering createCustomer method.");
        try {
            // Validate customer data
            validateCustomer(customer);
            System.out.println("[DEBUG] Customer data validated successfully.");

            // Save the customer to the database
            System.out.println("[DEBUG] Saving customer to the database...");
            Customer savedCustomer = customerDao.save(customer);
            System.out.println("[DEBUG] Customer saved with ID: " + savedCustomer.getId());

            // Notify observers (e.g., send welcome email)
            System.out.println("[DEBUG] Notifying observers...");
            notificationService.notifyObservers(savedCustomer, "CREATED");
            System.out.println("[DEBUG] Observers notified.");

            return savedCustomer;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in createCustomer: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Update an existing customer
    public boolean updateCustomer(Customer customer) {
        System.out.println("[DEBUG] Entering updateCustomer method.");
        try {
            // Validate customer data
            validateCustomer(customer);
            System.out.println("[DEBUG] Customer data validated successfully.");

            // Update the customer in the database
            System.out.println("[DEBUG] Updating customer in the database...");
            boolean isUpdated = customerDao.update(customer);
            if (isUpdated) {
                System.out.println("[DEBUG] Customer updated successfully: " + customer.getId());

                // Notify observers (e.g., send update confirmation)
                System.out.println("[DEBUG] Notifying observers...");
                notificationService.notifyObservers(customer, "UPDATED");
                System.out.println("[DEBUG] Observers notified.");
            } else {
                System.out.println("[DEBUG] Failed to update customer: " + customer.getId());
            }

            return isUpdated;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in updateCustomer: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Delete a customer by ID
    public boolean deleteCustomer(int id) {
        System.out.println("[DEBUG] Entering deleteCustomer method.");
        try {
            // Validate customer ID
            if (id <= 0) {
                System.out.println("[DEBUG] Invalid customer ID: " + id);
                throw new IllegalArgumentException("Invalid customer ID.");
            }

            // Delete the customer from the database
            System.out.println("[DEBUG] Deleting customer with ID: " + id);
            boolean isDeleted = customerDao.delete(id);
            if (isDeleted) {
                System.out.println("[DEBUG] Customer deleted successfully: " + id);

                // Notify observers (e.g., send deletion confirmation)
                System.out.println("[DEBUG] Notifying observers...");
                Customer deletedCustomer = new Customer();
                deletedCustomer.setId(id);
                notificationService.notifyObservers(deletedCustomer, "DELETED");
                System.out.println("[DEBUG] Observers notified.");
            } else {
                System.out.println("[DEBUG] Failed to delete customer: " + id);
            }

            return isDeleted;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in deleteCustomer: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Find a customer by ID
    public Customer findCustomerById(int id) {
        System.out.println("[DEBUG] Entering findCustomerById method.");
        try {
            // Validate customer ID
            if (id <= 0) {
                System.out.println("[DEBUG] Invalid customer ID: " + id);
                throw new IllegalArgumentException("Invalid customer ID.");
            }

            // Retrieve the customer from the database
            System.out.println("[DEBUG] Retrieving customer with ID: " + id);
            Customer customer = customerDao.findById(id);
            if (customer != null) {
                System.out.println("[DEBUG] Customer found: " + customer.getName());
            } else {
                System.out.println("[DEBUG] Customer not found with ID: " + id);
            }

            return customer;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in findCustomerById: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Retrieve all customers
    public List<Customer> findAllCustomers() {
        System.out.println("[DEBUG] Entering findAllCustomers method.");
        try {
            // Retrieve all customers from the database
            System.out.println("[DEBUG] Retrieving all customers...");
            List<Customer> customers = customerDao.findAll();
            System.out.println("[DEBUG] Retrieved " + customers.size() + " customers.");
            return customers;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in findAllCustomers: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Search customers by name or phone
    public List<Customer> searchCustomers(String searchQuery) {
        System.out.println("[DEBUG] Entering searchCustomers method.");
        try {
            // Validate search query
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                System.out.println("[DEBUG] Search query is empty. Returning all customers.");
                return findAllCustomers();
            }

            // Search customers in the database
            System.out.println("[DEBUG] Searching customers with query: " + searchQuery);
            List<Customer> customers = customerDao.findByNameOrPhone(searchQuery);
            System.out.println("[DEBUG] Found " + customers.size() + " customers matching the query.");
            return customers;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in searchCustomers: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Validate customer data
    private void validateCustomer(Customer customer) {
        System.out.println("[DEBUG] Entering validateCustomer method.");
        try {
            if (customer == null) {
                System.out.println("[DEBUG] Customer is null.");
                throw new IllegalArgumentException("Customer cannot be null.");
            }
            if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                System.out.println("[DEBUG] Customer name is empty.");
                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
                System.out.println("[DEBUG] Customer phone is empty.");
                throw new IllegalArgumentException("Customer phone cannot be empty.");
            }
            if (customer.getNic() == null || customer.getNic().trim().isEmpty()) {
                System.out.println("[DEBUG] Customer NIC is empty.");
                throw new IllegalArgumentException("Customer NIC cannot be empty.");
            }
            if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                System.out.println("[DEBUG] Customer address is empty.");
                throw new IllegalArgumentException("Customer address cannot be empty.");
            }
            System.out.println("[DEBUG] Customer data validated successfully.");
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in validateCustomer: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }
}