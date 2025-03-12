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
        this.fleetService = new FleetService(new FleetDAO(), new NotificationService());
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

        if ("searchFleets".equals(action)) {
            // Handle fleet search

            List<Fleet> fleets = fleetService.searchFleets(searchQuery);
            req.setAttribute("fleets", fleets);
        } else if ("edit".equals(action)) {
            // Handle editing a fleet
            String fleetIdParam = req.getParameter("id");
            if (fleetIdParam != null && !fleetIdParam.isEmpty()) {
                try {
                    int fleetId = Integer.parseInt(fleetIdParam);

                    Fleet fleet = fleetService.findFleetById(fleetId);
                    if (fleet != null) {
                        req.setAttribute("fleet", fleet); // Pass the fleet to the JSP
                    } else {
                        req.setAttribute("error", "Fleet not found.");
                    }
                } catch (NumberFormatException e) {

                    req.setAttribute("error", "Invalid Fleet ID.");
                }
            } else {

                req.setAttribute("error", "Fleet ID is missing.");
            }
        } else if ("delete".equals(action)) {
            // Handle deleting a fleet
            String fleetIdParam = req.getParameter("id");
            if (fleetIdParam != null && !fleetIdParam.isEmpty()) {
                try {
                    int fleetId = Integer.parseInt(fleetIdParam);

                    boolean isDeleted = fleetService.deleteFleet(fleetId);
                    if (isDeleted) {

                        req.setAttribute("success", "Fleet deleted successfully.");
                    } else {

                        req.setAttribute("error", "Failed to delete fleet.");
                    }
                } catch (NumberFormatException e) {

                    req.setAttribute("error", "Invalid Fleet ID.");
                }
            } else {

                req.setAttribute("error", "Fleet ID is missing.");
            }
        }

        // Retrieve all fleets for display (if no search query is provided)
        if (!"searchFleets".equals(action)) {

            List<Fleet> fleets = fleetService.findAllFleets();
            req.setAttribute("fleets", fleets);
        }


        req.getRequestDispatcher("/WEB-INF/views/protected/fleetManagement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("createOrUpdateFleet".equals(action)) {

            Fleet fleet = new Fleet();
            String fleetIdParam = req.getParameter("fleetId");

            try {
                // Populate fleet details from the form

                fleet.setDriverName(req.getParameter("driverName"));
                fleet.setVehicleType(VehicleType.valueOf(req.getParameter("vehicleType")));
                fleet.setPlateNumber(req.getParameter("plateNumber"));
                fleet.setDriverContact(req.getParameter("driverContact"));


                if (fleetIdParam != null && !fleetIdParam.isEmpty()) {
                    // Update existing fleet

                    fleet.setId(Integer.parseInt(fleetIdParam));
                    boolean isUpdated = fleetService.updateFleet(fleet);
                    if (isUpdated) {
                        req.setAttribute("success", "Fleet updated successfully.");
                    } else {
                        req.setAttribute("error", "Failed to update fleet.");
                    }
                } else {
                    // Create new fleet

                    fleet = fleetService.createFleet(fleet);
                    req.setAttribute("success", "Fleet created successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Invalid input. Please check the fields.");
            }
        }

        // Retrieve all fleets for display

        List<Fleet> fleets = fleetService.findAllFleets();
        req.setAttribute("fleets", fleets);

        // Forward to the fleet management page

        req.getRequestDispatcher("/WEB-INF/views/protected/fleetManagement.jsp").forward(req, resp);
    }
}