package com.megacitycab.service;

import com.megacitycab.dao.FleetDAO;
import com.megacitycab.model.Fleet;
import com.megacitycab.observer.FleetNotifier;

import java.util.List;

public class FleetService {
    private final FleetDAO fleetDao;
    private final NotificationService notificationService;

    // Constructor Injection (DIP)
    public FleetService(FleetDAO fleetDao, NotificationService notificationService) {
        this.fleetDao = fleetDao;
        this.notificationService = notificationService;

        // Register the FleetNotifier observer
        notificationService.addObserver(Fleet.class, new FleetNotifier());

    }

    // Create a new fleet
    public Fleet createFleet(Fleet fleet) {
        try {
            // Validate fleet data
            validateFleet(fleet);

            // Save the fleet to the database
            fleetDao.save(fleet);

            // Notify observers
            notificationService.notifyObservers(fleet, "You have been added to Mega City Cabs fleet " +fleet.getDriverName());

            return fleet;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Update an existing fleet
    public boolean updateFleet(Fleet fleet) {
        try {
            // Validate fleet data
            validateFleet(fleet);

            // Update the fleet in the database
            fleetDao.update(fleet);

            // Notify observers
            notificationService.notifyObservers(fleet, "Your details have been updated " +fleet.getDriverName());

            return true;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Delete a fleet by ID
    public boolean deleteFleet(int id) {
        try {
            // Validate fleet ID
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid fleet ID.");
            }

            // Fetch the fleet's driver name before deletion
            String deletedFleetDriverName = fleetDao.findById(id).getDriverName();

            // Delete the fleet from the database
            boolean isDeleted = fleetDao.delete(id);
            if (isDeleted) {
                // Notify observers (e.g., send deletion confirmation)
                Fleet deletedFleet = new Fleet();
                deletedFleet.setId(id);
                notificationService.notifyObservers(deletedFleet, "You have been removed from Mega City Cabs fleet " + deletedFleetDriverName);
            } else {
                throw new IllegalArgumentException("Failed to delete fleet.");
            }

            return isDeleted;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Find a fleet by ID
    public Fleet findFleetById(int id) {
        try {
            // Validate fleet ID
            if (id <= 0) {

                throw new IllegalArgumentException("Invalid fleet ID.");
            }

            // Retrieve the fleet from the database
            Fleet fleet = fleetDao.findById(id);
            if (fleet != null) {
                System.out.println("Fleet found: " + fleet.getDriverName());
            } else {
                throw new IllegalArgumentException("Fleet not found with ID.");

            }
            return fleet;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Retrieve all fleets
    public List<Fleet> findAllFleets() {
        try {
            // Retrieve all fleets from the database

            List<Fleet> fleets = fleetDao.findAll();

            return fleets;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Search fleets by driver name or plate number
    public List<Fleet> searchFleets(String searchQuery) {
        try {
            // Validate search query
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                return findAllFleets();
            }

            // Search fleets in the database
            List<Fleet> fleets = fleetDao.searchAllColumns(searchQuery);

            return fleets;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Validate fleet data
    private void validateFleet(Fleet fleet) {
        try {
            if (fleet == null) {

                throw new IllegalArgumentException("Fleet cannot be null.");
            }
            if (fleet.getDriverName() == null || fleet.getDriverName().trim().isEmpty()) {

                throw new IllegalArgumentException("Driver name cannot be empty.");
            }
            if (fleet.getVehicleType() == null) {

                throw new IllegalArgumentException("Vehicle type cannot be empty.");
            }
            if (fleet.getPlateNumber() == null || fleet.getPlateNumber().trim().isEmpty()) {

                throw new IllegalArgumentException("Plate number cannot be empty.");
            }
            if (fleet.getDriverContact() == null || fleet.getDriverContact().trim().isEmpty()) {

                throw new IllegalArgumentException("Driver contact cannot be empty.");
            }

        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }
}