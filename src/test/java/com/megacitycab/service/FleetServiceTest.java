package com.megacitycab.service;

import com.megacitycab.dao.FleetDAO;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FleetServiceTest {

    private FleetService fleetService;
    private FleetDAOStub fleetDAOStub;
    private NotificationServiceStub notificationServiceStub;

    @Before
    public void setUp() {
        fleetDAOStub = new FleetDAOStub();
        notificationServiceStub = new NotificationServiceStub();
        fleetService = new FleetService(fleetDAOStub, notificationServiceStub);
    }

    @Test
    public void testCreateFleet() {
        // Arrange
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        // Act
        Fleet savedFleet = fleetService.createFleet(fleet);

        // Assert
        assertNotNull(savedFleet);
        assertEquals(1, savedFleet.getId()); // Verify auto-generated ID
        assertEquals("Driver One", savedFleet.getDriverName());
        assertEquals(VehicleType.ZIP, savedFleet.getVehicleType());
        assertEquals("ABC123", savedFleet.getPlateNumber());
        assertEquals("987-654-3210", savedFleet.getDriverContact());

        // Verify notification
        assertEquals(1, notificationServiceStub.getNotifications().size());
        assertEquals("You have been added to Mega City Cabs fleet Driver One", notificationServiceStub.getNotifications().get(0));
    }

    @Test
    public void testUpdateFleet() {
        // Arrange
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        // Act
        boolean isUpdated = fleetService.updateFleet(fleet);

        // Assert
        assertTrue(isUpdated);

        // Verify notification
        assertEquals(1, notificationServiceStub.getNotifications().size());
        assertEquals("Your details have been updated Driver One", notificationServiceStub.getNotifications().get(0));
    }

    @Test
    public void testDeleteFleet() {
        // Arrange
        int fleetId = 1;
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");
        fleetDAOStub.setFleetToReturn(fleet); // Ensure the fleet is returned when fetched

        // Act
        boolean isDeleted = fleetService.deleteFleet(fleetId);

        // Assert
        assertTrue(isDeleted);

        // Verify notification
        assertEquals(1, notificationServiceStub.getNotifications().size());
        assertEquals("You have been removed from Mega City Cabs fleet Driver One", notificationServiceStub.getNotifications().get(0));
    }

    @Test
    public void testFindFleetById() {
        // Arrange
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");
        fleetDAOStub.setFleetToReturn(fleet);

        // Act
        Fleet foundFleet = fleetService.findFleetById(1);

        // Assert
        assertNotNull(foundFleet);
        assertEquals("Driver One", foundFleet.getDriverName());
        assertEquals(VehicleType.ZIP, foundFleet.getVehicleType());
        assertEquals("ABC123", foundFleet.getPlateNumber());
        assertEquals("987-654-3210", foundFleet.getDriverContact());
    }

    @Test
    public void testFindAllFleets() {
        // Arrange
        Fleet fleet1 = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");
        Fleet fleet2 = new Fleet(2, "Driver Two", VehicleType.PRO, "XYZ789", "123-456-7890");

        List<Fleet> fleets = new ArrayList<>();
        fleets.add(fleet1);
        fleets.add(fleet2);
        fleetDAOStub.setAllFleets(fleets);

        // Act
        List<Fleet> result = fleetService.findAllFleets();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Driver One", result.get(0).getDriverName());
        assertEquals("Driver Two", result.get(1).getDriverName());
    }

    @Test
    public void testSearchFleets() {
        // Arrange
        Fleet fleet1 = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");
        Fleet fleet2 = new Fleet(2, "Driver Two", VehicleType.PRO, "XYZ789", "123-456-7890");

        List<Fleet> fleets = new ArrayList<>();
        fleets.add(fleet1);
        fleets.add(fleet2);
        fleetDAOStub.setAllFleets(fleets);

        // Act
        List<Fleet> result = fleetService.searchFleets("Driver One");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Simulated search returns all fleets
        assertEquals("Driver One", result.get(0).getDriverName());
        assertEquals("Driver Two", result.get(1).getDriverName());
    }

    // Manual stub for FleetDAO
    static class FleetDAOStub extends FleetDAO {
        private List<Fleet> fleets = new ArrayList<>();
        private Fleet fleetToReturn;

        void setFleetToReturn(Fleet fleet) {
            this.fleetToReturn = fleet;
        }

        void setAllFleets(List<Fleet> fleets) {
            this.fleets = fleets;
        }

        @Override
        public void save(Fleet fleet) {
            fleet.setId(fleets.size() + 1); // Simulate auto-generated ID
            fleets.add(fleet); // Simulate saving to a list
        }

        @Override
        public Fleet findById(int id) {
            return fleetToReturn; // Return the predefined fleet
        }

        @Override
        public List<Fleet> findAll() {
            return fleets; // Return the predefined list of fleets
        }

        @Override
        public boolean update(Fleet fleet) {
            // Simulate updating a fleet
            return true;
        }

        @Override
        public boolean delete(int id) {
            // Simulate deleting a fleet
            return true;
        }

        @Override
        public List<Fleet> searchAllColumns(String searchQuery) {
            return fleets; // Simulate search functionality
        }
    }

    // Manual stub for NotificationService
    static class NotificationServiceStub extends NotificationService {
        private List<String> notifications = new ArrayList<>();

        @Override
        public void notifyObservers(Object entity, String message) {
            notifications.add(message); // Simulate notification
        }

        List<String> getNotifications() {
            return notifications;
        }
    }
}