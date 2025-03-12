package com.megacitycab.controller;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.dao.FleetDAO;
import com.megacitycab.dto.BookingBillingDTO;
import com.megacitycab.dto.BookingDTO;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.service.BillingService;
import com.megacitycab.service.BookingService;
import com.megacitycab.service.NotificationService;
import com.megacitycab.strategy.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/protected/bookingManagement")
public class BookingServlet extends HttpServlet {
    private BookingService bookingService;


    @Override
    public void init() {
        System.out.println("BookingServlet init() called. Initializing BookingService and dependencies.");
        // Initialize dependencies with proper DAOs
        this.bookingService = new BookingService(
                new BookingDAO(),       // Use your existing BookingDAO
                new CustomerDAO(),      // Use your existing CustomerDAO
                new FleetDAO(),         // Use your existing FleetDAO
                new BillingService(
                        new com.megacitycab.dao.BillingDAO(),   // Use your existing BillingDAO
                        new StandardPricing() // Default strategy
                ),
                new NotificationService()
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("BookingServlet doGet() called.");
        String action = req.getParameter("action");
        String searchQuery = req.getParameter("searchQuery");

        List<BookingBillingDTO> bookingBillingList;
        if ("searchBookings".equals(action)) {
            bookingBillingList = bookingService.searchBookingBillingDetails(searchQuery);
        } else {
            bookingBillingList = bookingService.getAllBookingBillingDetails();
        }
        req.setAttribute("bookingBillingList", bookingBillingList);
        // Also retrieve all customers for the booking form
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.findAll();
        req.setAttribute("customers", customers);

        // Retrieve all fleets for the vehicle section
        FleetDAO fleetDAO = new FleetDAO();
        List<Fleet> fleets = fleetDAO.findAll();
        req.setAttribute("fleets", fleets);

        System.out.println("Forwarding to bookingManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/bookingManagement.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("BookingServlet doPost() called.");
        String action = req.getParameter("action");

        if ("assignFleet".equals(action)) {
            // Handle fleet assignment
            String fleetIdParam = req.getParameter("fleetId");
            String bookingIdParam = req.getParameter("bookingId");

            if (fleetIdParam == null || fleetIdParam.isEmpty() || bookingIdParam == null || bookingIdParam.isEmpty()) {
                // Handle missing parameters
                req.setAttribute("error", "Fleet ID or Booking ID is missing.");
                req.getRequestDispatcher("/WEB-INF/views/protected/bookingManagement.jsp").forward(req, resp);
                return;
            }

            try {
                int fleetId = Integer.parseInt(fleetIdParam);
                int bookingId = Integer.parseInt(bookingIdParam);

                System.out.println("Assigning fleet with ID: " + fleetId + " to booking with ID: " + bookingId);
                bookingService.assignFleetToBooking(bookingId, fleetId);

                // Redirect back to the booking management page
                resp.sendRedirect(req.getContextPath() + "/protected/bookingManagement");
                return;
            } catch (NumberFormatException e) {
                // Handle invalid integer format
                req.setAttribute("error", "Invalid Fleet ID or Booking ID.");
                req.getRequestDispatcher("/WEB-INF/views/protected/bookingManagement.jsp").forward(req, resp);
                return;
            }
        }

        BookingDTO bookingDTO = new BookingDTO();
        Customer customer;

        try {
            // Check if it's a new customer
            String newCustomerName = req.getParameter("newCustomerName");
            if (newCustomerName != null && !newCustomerName.isEmpty()) {
                // Create a new customer
                customer = new Customer();
                customer.setName(newCustomerName);
                customer.setPhone(req.getParameter("newCustomerPhone"));
                customer.setNic(req.getParameter("newCustomerNIC"));
                customer.setAddress(req.getParameter("newCustomerAddress"));

                // Save the new customer to the database
                CustomerDAO customerDAO = new CustomerDAO();
                customer = customerDAO.save(customer);
                System.out.println("New customer saved with ID: " + customer.getId());
            } else {
                // Use existing customer
                int customerId = Integer.parseInt(req.getParameter("customerId"));
                customer = new CustomerDAO().findById(customerId);
            }

            // Populate BookingDTO
            bookingDTO.setCustomerId(customer.getId());
            bookingDTO.setFleetId(Integer.parseInt(req.getParameter("fleetId")));
            bookingDTO.setPickup(req.getParameter("pickup"));
            bookingDTO.setDestination(req.getParameter("destination"));
            bookingDTO.setDistance(Double.parseDouble(req.getParameter("distance")));

        } catch (Exception e) {
            System.out.println("Error parsing booking parameters: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Invalid input. Please check the fields.");
            req.getRequestDispatcher("/WEB-INF/views/protected/bookingManagement.jsp").forward(req, resp);
            return;
        }

        // Determine pricing strategy based on user selection
        String pricingStrategy = req.getParameter("pricingStrategy");
        System.out.println("Pricing strategy selected: " + pricingStrategy);
        PricingStrategy strategy = getPricingStrategy(pricingStrategy);

        // Update BillingService with the selected strategy
        BillingService billingService = new BillingService(new com.megacitycab.dao.BillingDAO(), strategy);
        bookingService.setBillingService(billingService);
        System.out.println("BillingService updated with strategy: " + strategy.getClass().getSimpleName());

        // Process booking
        System.out.println("Creating booking...");
        bookingService.createBooking(bookingDTO, strategy);
        System.out.println("Booking created successfully.");

        // Refresh page
        System.out.println("Reload to bookingManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/bookingManagement.jsp").forward(req, resp);
    }

    private PricingStrategy getPricingStrategy(String strategy) {
        switch (strategy) {
            case "discount":
                return new DiscountPricing();
            case "peak":
                return new PeakPricing();
            case "standard":
            default:
                return new StandardPricing();
        }
    }
}
