package com.megacitycab.controller;

import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.model.Customer;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/protected/customerManagement")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService;

    @Override
    public void init() {
        System.out.println("[DEBUG] CustomerServlet init() called. Initializing CustomerService.");
        this.customerService = new CustomerService(new CustomerDAO(), new NotificationService<>());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[DEBUG] CustomerServlet doGet() called.");
        String action = req.getParameter("action");
        String searchQuery = req.getParameter("searchQuery");

        // Retrieve success/error messages from session
        String success = (String) req.getSession().getAttribute("success");
        String error = (String) req.getSession().getAttribute("error");

        if (success != null) {
            System.out.println("[DEBUG] Success message found in session: " + success);
            req.setAttribute("success", success);
            req.getSession().removeAttribute("success"); // Clear the session attribute
        }
        if (error != null) {
            System.out.println("[DEBUG] Error message found in session: " + error);
            req.setAttribute("error", error);
            req.getSession().removeAttribute("error"); // Clear the session attribute
        }

        if ("searchCustomers".equals(action)) {
            // Handle customer search
            System.out.println("[DEBUG] Search Customers action triggered with searchQuery: " + searchQuery);
            List<Customer> customers = customerService.searchCustomers(searchQuery);
            req.setAttribute("customers", customers);
        } else if ("edit".equals(action)) {
            // Handle editing a customer
            String customerIdParam = req.getParameter("id");
            if (customerIdParam != null && !customerIdParam.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(customerIdParam);
                    System.out.println("[DEBUG] Editing customer with ID: " + customerId);
                    Customer customer = customerService.findCustomerById(customerId);
                    if (customer != null) {
                        System.out.println("[DEBUG] Customer found: " + customer.getName());
                        req.setAttribute("customer", customer); // Pass the customer to the JSP
                    } else {
                        System.out.println("[DEBUG] Customer not found with ID: " + customerId);
                        req.setAttribute("error", "Customer not found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[DEBUG] Invalid Customer ID: " + customerIdParam);
                    req.setAttribute("error", "Invalid Customer ID.");
                }
            } else {
                System.out.println("[DEBUG] Customer ID is missing.");
                req.setAttribute("error", "Customer ID is missing.");
            }
        } else if ("delete".equals(action)) {
            // Handle deleting a customer
            String customerIdParam = req.getParameter("id");
            if (customerIdParam != null && !customerIdParam.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(customerIdParam);
                    System.out.println("[DEBUG] Deleting customer with ID: " + customerId);
                    boolean isDeleted = customerService.deleteCustomer(customerId);
                    if (isDeleted) {
                        System.out.println("[DEBUG] Customer deleted successfully: " + customerId);
                        req.setAttribute("success", "Customer deleted successfully.");
                    } else {
                        System.out.println("[DEBUG] Failed to delete customer: " + customerId);
                        req.setAttribute("error", "Failed to delete customer.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[DEBUG] Invalid Customer ID: " + customerIdParam);
                    req.setAttribute("error", "Invalid Customer ID.");
                }
            } else {
                System.out.println("[DEBUG] Customer ID is missing.");
                req.setAttribute("error", "Customer ID is missing.");
            }
        }

        // Retrieve all customers for display (if no search query is provided)
        if (!"searchCustomers".equals(action)) {
            System.out.println("[DEBUG] Retrieving all customers for display.");
            List<Customer> customers = customerService.findAllCustomers();
            req.setAttribute("customers", customers);
        }

        System.out.println("[DEBUG] Forwarding to customerManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/customerManagement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[DEBUG] CustomerServlet doPost() called.");
        String action = req.getParameter("action");

        if ("createOrUpdateCustomer".equals(action)) {
            System.out.println("[DEBUG] Handling createOrUpdateCustomer action.");
            Customer customer = new Customer();
            String customerIdParam = req.getParameter("customerId");

            try {
                // Populate customer details from the form
                System.out.println("[DEBUG] Populating customer details from the form.");
                customer.setName(req.getParameter("name"));
                customer.setPhone(req.getParameter("phone"));
                customer.setNic(req.getParameter("nic"));
                customer.setAddress(req.getParameter("address"));

                System.out.println("[DEBUG] Customer details: " + customer);

                if (customerIdParam != null && !customerIdParam.isEmpty()) {
                    // Update existing customer
                    System.out.println("[DEBUG] Updating existing customer with ID: " + customerIdParam);
                    customer.setId(Integer.parseInt(customerIdParam));
                    boolean isUpdated = customerService.updateCustomer(customer);
                    if (isUpdated) {
                        System.out.println("[DEBUG] Customer updated successfully: " + customer.getId());
                        req.setAttribute("success", "Customer updated successfully.");
                    } else {
                        System.out.println("[DEBUG] Failed to update customer: " + customer.getId());
                        req.setAttribute("error", "Failed to update customer.");
                    }
                } else {
                    // Create new customer
                    System.out.println("[DEBUG] Creating new customer.");
                    customer = customerService.createCustomer(customer);
                    System.out.println("[DEBUG] Customer created successfully with ID: " + customer.getId());
                    req.setAttribute("success", "Customer created successfully.");
                }
            } catch (Exception e) {
                System.out.println("[DEBUG] Error processing customer: " + e.getMessage());
                e.printStackTrace();
                req.setAttribute("error", "Invalid input. Please check the fields.");
            }
        }

        // Retrieve all customers for display
        System.out.println("[DEBUG] Retrieving all customers for display.");
        List<Customer> customers = customerService.findAllCustomers();
        req.setAttribute("customers", customers);

        // Forward to the customer management page
        System.out.println("[DEBUG] Forwarding to customerManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/customerManagement.jsp").forward(req, resp);
    }
}