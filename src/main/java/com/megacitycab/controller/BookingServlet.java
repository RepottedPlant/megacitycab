package com.megacitycab.controller;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.dao.FleetDAO;
import com.megacitycab.dto.BookingBillingDTO;
import com.megacitycab.dto.BookingDTO;
import com.megacitycab.model.Billing;
import com.megacitycab.model.Booking;
import com.megacitycab.model.Customer;
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
import java.util.ArrayList;
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

        if ("searchBookings".equals(action)) {
            System.out.println("Search Bookings action triggered with searchQuery: " + searchQuery);

            // Search across all columns
            List<Booking> bookings = bookingService.searchBookings(searchQuery);

            // Convert List<Booking> to List<BookingBillingDTO>
            List<BookingBillingDTO> bookingBillingList = new ArrayList<>();
            for (Booking booking : bookings) {
                Billing billing = bookingService.getBillingService().getBillingByBookingId(booking.getId());
                bookingBillingList.add(new BookingBillingDTO(booking, billing));
            }

            req.setAttribute("bookingBillingList", bookingBillingList);
            System.out.println("Converted bookingBillingList size: " + bookingBillingList.size());
        } else if ("searchCustomers".equals(action)) {
            System.out.println("Search Customers action triggered with searchQuery: " + searchQuery);
            CustomerDAO customerDAO = new CustomerDAO();
            List<Customer> customers = customerDAO.findByNameOrPhone(searchQuery);
            req.setAttribute("customers", customers);
        } else {
            System.out.println("No search action specified. Retrieving all combined booking and billing details.");
            // Set the combined list
            List<com.megacitycab.dto.BookingBillingDTO> bookingBillingList = bookingService.getAllBookingBillingDetails();
            req.setAttribute("bookingBillingList", bookingBillingList);
        }
        // Also retrieve all customers for the booking form
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.findAll();
        req.setAttribute("customers", customers);
        System.out.println("Forwarding to bookingManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/bookingManagement.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("BookingServlet doPost() called.");
        // Map all parameters from the form to BookingDTO
        BookingDTO bookingDTO = new BookingDTO();
        try {
            bookingDTO.setCustomerId(Integer.parseInt(req.getParameter("customerId")));
            bookingDTO.setFleetId(Integer.parseInt(req.getParameter("fleetId")));
            bookingDTO.setCustomerName(req.getParameter("customerName"));
            bookingDTO.setNic(req.getParameter("nic"));
            bookingDTO.setPickup(req.getParameter("pickup"));
            bookingDTO.setDestination(req.getParameter("destination"));
            bookingDTO.setDistance(Double.parseDouble(req.getParameter("distance")));
            System.out.println("BookingDTO populated: "
                    + "customerId=" + bookingDTO.getCustomerId()
                    + ", fleetId=" + bookingDTO.getFleetId()
                    + ", customerName=" + bookingDTO.getCustomerName()
                    + ", NIC=" + bookingDTO.getNic()
                    + ", pickup=" + bookingDTO.getPickup()
                    + ", destination=" + bookingDTO.getDestination()
                    + ", distance=" + bookingDTO.getDistance());
        } catch (Exception e) {
            System.out.println("Error parsing booking parameters: " + e.getMessage());
            e.printStackTrace();
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

        // Redirect to success page
        System.out.println("Forwarding to success.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/success.jsp").forward(req, resp);
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
