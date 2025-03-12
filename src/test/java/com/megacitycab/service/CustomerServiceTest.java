package com.megacitycab.service;

import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.model.Customer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerDAOStub customerDAOStub;
    private NotificationServiceStub notificationServiceStub;

    // Manual stub for CustomerDAO
    static class CustomerDAOStub extends CustomerDAO {
        private List<Customer> customers = new ArrayList<>();
        private Customer customerToReturn;

        void setCustomerToReturn(Customer customer) {
            this.customerToReturn = customer;
        }

        void setAllCustomers(List<Customer> customers) {
            this.customers = customers;
        }

        @Override
        public Customer save(Customer customer) {
            customers.add(customer); // Simulate saving to a list
            return customer;
        }

        @Override
        public boolean update(Customer customer) {
            // Simulate updating a customer
            return true;
        }

        @Override
        public boolean delete(int id) {
            // Simulate deleting a customer
            return true;
        }

        @Override
        public Customer findById(int id) {
            return customerToReturn; // Return the predefined customer
        }

        @Override
        public List<Customer> findAll() {
            return customers; // Return the predefined list of customers
        }

        @Override
        public List<Customer> findByNameOrPhone(String searchQuery) {
            return customers; // Simulate search functionality
        }
    }

    // Manual stub for NotificationService
    static class NotificationServiceStub extends NotificationService {
        @Override
        public void notifyObservers(Object entity, String eventType) {
            // Simulate notification
        }
    }

    @Before
    public void setUp() {
        customerDAOStub = new CustomerDAOStub();
        notificationServiceStub = new NotificationServiceStub();
        customerService = new CustomerService(customerDAOStub, notificationServiceStub);
    }

    @Test
    public void testCreateCustomer() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");

        // Act
        Customer savedCustomer = customerService.createCustomer(customer);

        // Assert
        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("123 Main St", savedCustomer.getAddress());
        assertEquals("123456789V", savedCustomer.getNic());
        assertEquals("123-456-7890", savedCustomer.getPhone());
    }

    @Test
    public void testUpdateCustomer() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");

        // Act
        boolean isUpdated = customerService.updateCustomer(customer);

        // Assert
        assertTrue(isUpdated);
    }

    @Test
    public void testDeleteCustomer() {
        // Arrange
        int customerId = 1;

        // Act
        boolean isDeleted = customerService.deleteCustomer(customerId);

        // Assert
        assertTrue(isDeleted);
    }

    @Test
    public void testFindCustomerById() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        customerDAOStub.setCustomerToReturn(customer);

        // Act
        Customer foundCustomer = customerService.findCustomerById(1);

        // Assert
        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getName());
        assertEquals("123 Main St", foundCustomer.getAddress());
        assertEquals("123456789V", foundCustomer.getNic());
        assertEquals("123-456-7890", foundCustomer.getPhone());
    }

    @Test
    public void testFindAllCustomers() {
        // Arrange
        Customer customer1 = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Customer customer2 = new Customer(2, "Jane Doe", "456 Elm St", "987654321V", "987-654-3210");

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        customerDAOStub.setAllCustomers(customers);

        // Act
        List<Customer> result = customerService.findAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test
    public void testSearchCustomers() {
        // Arrange
        Customer customer1 = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Customer customer2 = new Customer(2, "Jane Doe", "456 Elm St", "987654321V", "987-654-3210");

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        customerDAOStub.setAllCustomers(customers);

        // Act
        List<Customer> result = customerService.searchCustomers("Doe");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }
}