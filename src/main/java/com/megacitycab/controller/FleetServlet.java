package com.megacitycab.controller;

import com.megacitycab.dao.FleetDAO;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import com.megacitycab.service.FleetService;
import com.megacitycab.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/protected/fleetManagement")
public class FleetServlet extends HttpServlet {
    private FleetService fleetService;

    @Override
    public void init() {
        System.out.println("[DEBUG] FleetServlet init() called. Initializing FleetService.");
        this.fleetService = new FleetService(new FleetDAO(), new NotificationService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[DEBUG] FleetServlet doGet() called.");
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

        if ("searchFleets".equals(action)) {
            // Handle fleet search
            System.out.println("[DEBUG] Search Fleets action triggered with searchQuery: " + searchQuery);
            List<Fleet> fleets = fleetService.searchFleets(searchQuery);
            req.setAttribute("fleets", fleets);
        } else if ("edit".equals(action)) {
            // Handle editing a fleet
            String fleetIdParam = req.getParameter("id");
            if (fleetIdParam != null && !fleetIdParam.isEmpty()) {
                try {
                    int fleetId = Integer.parseInt(fleetIdParam);
                    System.out.println("[DEBUG] Editing fleet with ID: " + fleetId);
                    Fleet fleet = fleetService.findFleetById(fleetId);
                    if (fleet != null) {
                        System.out.println("[DEBUG] Fleet found: " + fleet.getDriverName());
                        req.setAttribute("fleet", fleet); // Pass the fleet to the JSP
                    } else {
                        System.out.println("[DEBUG] Fleet not found with ID: " + fleetId);
                        req.setAttribute("error", "Fleet not found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[DEBUG] Invalid Fleet ID: " + fleetIdParam);
                    req.setAttribute("error", "Invalid Fleet ID.");
                }
            } else {
                System.out.println("[DEBUG] Fleet ID is missing.");
                req.setAttribute("error", "Fleet ID is missing.");
            }
        } else if ("delete".equals(action)) {
            // Handle deleting a fleet
            String fleetIdParam = req.getParameter("id");
            if (fleetIdParam != null && !fleetIdParam.isEmpty()) {
                try {
                    int fleetId = Integer.parseInt(fleetIdParam);
                    System.out.println("[DEBUG] Deleting fleet with ID: " + fleetId);
                    boolean isDeleted = fleetService.deleteFleet(fleetId);
                    if (isDeleted) {
                        System.out.println("[DEBUG] Fleet deleted successfully: " + fleetId);
                        req.setAttribute("success", "Fleet deleted successfully.");
                    } else {
                        System.out.println("[DEBUG] Failed to delete fleet: " + fleetId);
                        req.setAttribute("error", "Failed to delete fleet.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[DEBUG] Invalid Fleet ID: " + fleetIdParam);
                    req.setAttribute("error", "Invalid Fleet ID.");
                }
            } else {
                System.out.println("[DEBUG] Fleet ID is missing.");
                req.setAttribute("error", "Fleet ID is missing.");
            }
        }

        // Retrieve all fleets for display (if no search query is provided)
        if (!"searchFleets".equals(action)) {
            System.out.println("[DEBUG] Retrieving all fleets for display.");
            List<Fleet> fleets = fleetService.findAllFleets();
            req.setAttribute("fleets", fleets);
        }

        System.out.println("[DEBUG] Forwarding to fleetManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/fleetManagement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[DEBUG] FleetServlet doPost() called.");
        String action = req.getParameter("action");

        if ("createOrUpdateFleet".equals(action)) {
            System.out.println("[DEBUG] Handling createOrUpdateFleet action.");
            Fleet fleet = new Fleet();
            String fleetIdParam = req.getParameter("fleetId");

            try {
                // Populate fleet details from the form
                System.out.println("[DEBUG] Populating fleet details from the form.");
                fleet.setDriverName(req.getParameter("driverName"));
                fleet.setVehicleType(VehicleType.valueOf(req.getParameter("vehicleType")));
                fleet.setPlateNumber(req.getParameter("plateNumber"));
                fleet.setDriverContact(req.getParameter("driverContact"));

                System.out.println("[DEBUG] Fleet details: " + fleet);

                if (fleetIdParam != null && !fleetIdParam.isEmpty()) {
                    // Update existing fleet
                    System.out.println("[DEBUG] Updating existing fleet with ID: " + fleetIdParam);
                    fleet.setId(Integer.parseInt(fleetIdParam));
                    boolean isUpdated = fleetService.updateFleet(fleet);
                    if (isUpdated) {
                        System.out.println("[DEBUG] Fleet updated successfully: " + fleet.getId());
                        req.setAttribute("success", "Fleet updated successfully.");
                    } else {
                        System.out.println("[DEBUG] Failed to update fleet: " + fleet.getId());
                        req.setAttribute("error", "Failed to update fleet.");
                    }
                } else {
                    // Create new fleet
                    System.out.println("[DEBUG] Creating new fleet.");
                    fleet = fleetService.createFleet(fleet);
                    System.out.println("[DEBUG] Fleet created successfully with ID: " + fleet.getId());
                    req.setAttribute("success", "Fleet created successfully.");
                }
            } catch (Exception e) {
                System.out.println("[DEBUG] Error processing fleet: " + e.getMessage());
                e.printStackTrace();
                req.setAttribute("error", "Invalid input. Please check the fields.");
            }
        }

        // Retrieve all fleets for display
        System.out.println("[DEBUG] Retrieving all fleets for display.");
        List<Fleet> fleets = fleetService.findAllFleets();
        req.setAttribute("fleets", fleets);

        // Forward to the fleet management page
        System.out.println("[DEBUG] Forwarding to fleetManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/fleetManagement.jsp").forward(req, resp);
    }
}