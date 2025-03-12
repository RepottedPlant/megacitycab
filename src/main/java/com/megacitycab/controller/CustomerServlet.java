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

        this.customerService = new CustomerService(new CustomerDAO(), new NotificationService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        String searchQuery = req.getParameter("searchQuery");

        // Retrieve success/error messages from session
        String success = (String) req.getSession().getAttribute("success");
        String error = (String) req.getSession().getAttribute("error");

        if (success != null) {

            req.setAttribute("success", success);
            req.getSession().removeAttribute("success"); // Clear the session attribute
        }
        if (error != null) {

            req.setAttribute("error", error);
            req.getSession().removeAttribute("error"); // Clear the session attribute
        }

        if ("searchCustomers".equals(action)) {
            // Handle customer search

            List<Customer> customers = customerService.searchCustomers(searchQuery);
            req.setAttribute("customers", customers);
        } else if ("edit".equals(action)) {
            // Handle editing a customer
            String customerIdParam = req.getParameter("id");
            if (customerIdParam != null && !customerIdParam.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(customerIdParam);

                    Customer customer = customerService.findCustomerById(customerId);
                    if (customer != null) {
                        req.setAttribute("customer", customer); // Pass the customer to the JSP
                    } else {

                        req.setAttribute("error", "Customer not found.");
                    }
                } catch (NumberFormatException e) {

                    req.setAttribute("error", "Invalid Customer ID.");
                }
            } else {

                req.setAttribute("error", "Customer ID is missing.");
            }
        } else if ("delete".equals(action)) {
            // Handle deleting a customer
            String customerIdParam = req.getParameter("id");
            if (customerIdParam != null && !customerIdParam.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(customerIdParam);

                    boolean isDeleted = customerService.deleteCustomer(customerId);
                    if (isDeleted) {

                        req.setAttribute("success", "Customer deleted successfully.");
                    } else {

                        req.setAttribute("error", "Failed to delete customer.");
                    }
                } catch (NumberFormatException e) {

                    req.setAttribute("error", "Invalid Customer ID.");
                }
            } else {

                req.setAttribute("error", "Customer ID is missing.");
            }
        }

        // Retrieve all customers for display (if no search query is provided)
        if (!"searchCustomers".equals(action)) {

            List<Customer> customers = customerService.findAllCustomers();
            req.setAttribute("customers", customers);
        }


        req.getRequestDispatcher("/WEB-INF/views/protected/customerManagement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("createOrUpdateCustomer".equals(action)) {

            Customer customer = new Customer();
            String customerIdParam = req.getParameter("customerId");

            try {
                // Populate customer details from the form

                customer.setName(req.getParameter("name"));
                customer.setPhone(req.getParameter("phone"));
                customer.setNic(req.getParameter("nic"));
                customer.setAddress(req.getParameter("address"));


                if (customerIdParam != null && !customerIdParam.isEmpty()) {
                    // Update existing customer

                    customer.setId(Integer.parseInt(customerIdParam));
                    boolean isUpdated = customerService.updateCustomer(customer);
                    if (isUpdated) {
                        req.setAttribute("success", "Customer updated successfully.");
                    } else {
                        req.setAttribute("error", "Failed to update customer.");
                    }
                } else {
                    // Create new customer

                    customer = customerService.createCustomer(customer);
                    req.setAttribute("success", "Customer created successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Invalid input. Please check the fields.");
            }
        }

        // Retrieve all customers for display

        List<Customer> customers = customerService.findAllCustomers();
        req.setAttribute("customers", customers);

        // Forward to the customer management page

        req.getRequestDispatcher("/WEB-INF/views/protected/customerManagement.jsp").forward(req, resp);
    }
}