// src/main/java/com/megacitycab/controller/BookingServlet.java
package com.megacitycab.controller;

import com.megacitycab.dao.*;
import com.megacitycab.dto.BookingDTO;
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

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() {
        // Initialize dependencies with proper DAOs
        this.bookingService = new BookingService(
                new BookingDAO(),       // Use your existing BookingDAO
                new CustomerDAO(),      // Use your existing CustomerDAO
                new FleetDAO(),         // Use your existing FleetDAO
                new BillingService(
                        new BillingDAO(),   // Use your existing BillingDAO
                        new StandardPricing() // Default strategy
                ),
                new NotificationService()
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Show booking form
        req.getRequestDispatcher("/WEB-INF/views/protected/bookingForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Map all parameters from the form to BookingDTO
        BookingDTO bookingDTO = new BookingDTO();

        // Required by BookingService
        bookingDTO.setCustomerId(Integer.parseInt(req.getParameter("customerId")));
        bookingDTO.setFleetId(Integer.parseInt(req.getParameter("fleetId")));

        // Additional fields from your DTO
        bookingDTO.setCustomerName(req.getParameter("customerName"));
        bookingDTO.setNic(req.getParameter("nic"));
        bookingDTO.setPickup(req.getParameter("pickup"));
        bookingDTO.setDestination(req.getParameter("destination"));
        bookingDTO.setDistance(Double.parseDouble(req.getParameter("distance")));

        // Determine pricing strategy based on user selection
        String pricingStrategy = req.getParameter("pricingStrategy");
        PricingStrategy strategy = getPricingStrategy(pricingStrategy);

        // Update BillingService with the selected strategy
        BillingService billingService = new BillingService(new BillingDAO(), strategy);
        bookingService.setBillingService(billingService);

        // Process booking
        bookingService.createBooking(bookingDTO, strategy);

        // Redirect to success page
        req.getRequestDispatcher("/WEB-INF/views/protected/success.jsp").forward(req, resp);
    }

    /**
     * Helper method to get the appropriate PricingStrategy based on user selection.
     */
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