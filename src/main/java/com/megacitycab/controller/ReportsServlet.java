package com.megacitycab.controller;

import com.megacitycab.dto.CustomerRevenueDTO;
import com.megacitycab.dto.DriverRevenueDTO;
import com.megacitycab.service.ReportsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/protected/reports")
public class ReportsServlet extends HttpServlet {
    private ReportsService reportsService;

    @Override
    public void init() {
        reportsService = new ReportsService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve all report metrics
        int totalBookings = reportsService.getTotalBookings();
        double totalRevenue = reportsService.getTotalRevenue();
        Map<String, Integer> bookingsByPricing = reportsService.getBookingCountByPricingStrategy();
        Map<String, Integer> bookingsByVehicle = reportsService.getBookingCountByVehicleType();
        int driverCount = reportsService.getDriverCount();
        Map<String, Integer> bookingsByDriver = reportsService.getBookingCountByDriver();
        int customerCount = reportsService.getCustomerCount();
        CustomerRevenueDTO highestRevenueCustomer = reportsService.getHighestRevenueCustomer();
        DriverRevenueDTO highestRevenueDriver = reportsService.getHighestRevenueDriver();
        Map<String, Integer> userCountByRole = reportsService.getUserCountByRole();

        // Set attributes for JSP
        req.setAttribute("totalBookings", totalBookings);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("bookingsByPricing", bookingsByPricing);
        req.setAttribute("bookingsByVehicle", bookingsByVehicle);
        req.setAttribute("driverCount", driverCount);
        req.setAttribute("bookingsByDriver", bookingsByDriver);
        req.setAttribute("customerCount", customerCount);
        req.setAttribute("highestRevenueCustomer", highestRevenueCustomer);
        req.setAttribute("highestRevenueDriver", highestRevenueDriver);
        req.setAttribute("userCountByRole", userCountByRole);

        req.getRequestDispatcher("/WEB-INF/views/protected/reports.jsp").forward(req, resp);
    }
}