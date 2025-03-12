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
        System.out.println("[DEBUG] Initializing FleetService with FleetDAO and NotificationService.");
        this.fleetDao = fleetDao;
        this.notificationService = notificationService;

        // Register the FleetNotifier observer
        notificationService.addObserver(Fleet.class, new FleetNotifier());
        System.out.println("[DEBUG] FleetNotifier observer registered.");
    }

    // Create a new fleet
    public Fleet createFleet(Fleet fleet) {
        System.out.println("[DEBUG] Entering createFleet method.");
        try {
            // Validate fleet data
            validateFleet(fleet);
            System.out.println("[DEBUG] Fleet data validated successfully.");

            // Save the fleet to the database
            System.out.println("[DEBUG] Saving fleet to the database...");
            fleetDao.save(fleet);
            System.out.println("[DEBUG] Fleet saved with ID: " + fleet.getId());

            // Notify observers
            System.out.println("[DEBUG] Notifying observers...");
            notificationService.notifyObservers(fleet, "CREATE");
            System.out.println("[DEBUG] Observers notified.");

            return fleet;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in createFleet: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Update an existing fleet
    public boolean updateFleet(Fleet fleet) {
        System.out.println("[DEBUG] Entering updateFleet method.");
        try {
            // Validate fleet data
            validateFleet(fleet);
            System.out.println("[DEBUG] Fleet data validated successfully.");

            // Update the fleet in the database
            System.out.println("[DEBUG] Updating fleet in the database...");
            fleetDao.update(fleet);
            System.out.println("[DEBUG] Fleet updated successfully: " + fleet.getId());

            // Notify observers
            System.out.println("[DEBUG] Notifying observers...");
            notificationService.notifyObservers(fleet, "UPDATE");
            System.out.println("[DEBUG] Observers notified.");

            return true;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in updateFleet: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Delete a fleet by ID
    public boolean deleteFleet(int id) {
        System.out.println("[DEBUG] Entering deleteFleet method.");
        try {
            // Validate fleet ID
            if (id <= 0) {
                System.out.println("[DEBUG] Invalid fleet ID: " + id);
                throw new IllegalArgumentException("Invalid fleet ID.");
            }

            // Delete the fleet from the database
            System.out.println("[DEBUG] Deleting fleet with ID: " + id);
            fleetDao.delete(id);
            System.out.println("[DEBUG] Fleet deleted successfully: " + id);

            // Notify observers
            System.out.println("[DEBUG] Notifying observers...");
            Fleet deletedFleet = new Fleet();
            deletedFleet.setId(id);
            notificationService.notifyObservers(deletedFleet, "DELETE");
            System.out.println("[DEBUG] Observers notified.");

            return true;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in deleteFleet: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Find a fleet by ID
    public Fleet findFleetById(int id) {
        System.out.println("[DEBUG] Entering findFleetById method.");
        try {
            // Validate fleet ID
            if (id <= 0) {
                System.out.println("[DEBUG] Invalid fleet ID: " + id);
                throw new IllegalArgumentException("Invalid fleet ID.");
            }

            // Retrieve the fleet from the database
            System.out.println("[DEBUG] Retrieving fleet with ID: " + id);
            Fleet fleet = fleetDao.findById(id);
            if (fleet != null) {
                System.out.println("[DEBUG] Fleet found: " + fleet.getDriverName());
            } else {
                System.out.println("[DEBUG] Fleet not found with ID: " + id);
            }

            return fleet;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in findFleetById: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Retrieve all fleets
    public List<Fleet> findAllFleets() {
        System.out.println("[DEBUG] Entering findAllFleets method.");
        try {
            // Retrieve all fleets from the database
            System.out.println("[DEBUG] Retrieving all fleets...");
            List<Fleet> fleets = fleetDao.findAll();
            System.out.println("[DEBUG] Retrieved " + fleets.size() + " fleets.");
            return fleets;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in findAllFleets: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Search fleets by driver name or plate number
    public List<Fleet> searchFleets(String searchQuery) {
        System.out.println("[DEBUG] Entering searchFleets method.");
        try {
            // Validate search query
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                System.out.println("[DEBUG] Search query is empty. Returning all fleets.");
                return findAllFleets();
            }

            // Search fleets in the database
            System.out.println("[DEBUG] Searching fleets with query: " + searchQuery);
            List<Fleet> fleets = fleetDao.searchAllColumns(searchQuery);
            System.out.println("[DEBUG] Found " + fleets.size() + " fleets matching the query.");
            return fleets;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in searchFleets: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Validate fleet data
    private void validateFleet(Fleet fleet) {
        System.out.println("[DEBUG] Entering validateFleet method.");
        try {
            if (fleet == null) {
                System.out.println("[DEBUG] Fleet is null.");
                throw new IllegalArgumentException("Fleet cannot be null.");
            }
            if (fleet.getDriverName() == null || fleet.getDriverName().trim().isEmpty()) {
                System.out.println("[DEBUG] Driver name is empty.");
                throw new IllegalArgumentException("Driver name cannot be empty.");
            }
            if (fleet.getVehicleType() == null) {
                System.out.println("[DEBUG] Vehicle type is empty.");
                throw new IllegalArgumentException("Vehicle type cannot be empty.");
            }
            if (fleet.getPlateNumber() == null || fleet.getPlateNumber().trim().isEmpty()) {
                System.out.println("[DEBUG] Plate number is empty.");
                throw new IllegalArgumentException("Plate number cannot be empty.");
            }
            if (fleet.getDriverContact() == null || fleet.getDriverContact().trim().isEmpty()) {
                System.out.println("[DEBUG] Driver contact is empty.");
                throw new IllegalArgumentException("Driver contact cannot be empty.");
            }
            System.out.println("[DEBUG] Fleet data validated successfully.");
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in validateFleet: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }
}