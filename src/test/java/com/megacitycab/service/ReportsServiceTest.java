package com.megacitycab.service;

import com.megacitycab.dao.ReportsDAO;
import com.megacitycab.dto.CustomerRevenueDTO;
import com.megacitycab.dto.DriverRevenueDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class ReportsServiceTest {

    private ReportsService reportsService;
    private ReportsDAOStub reportsDAOStub;

    @Before
    public void setUp() {
        reportsDAOStub = new ReportsDAOStub();
        reportsService = new ReportsService();
        reportsService.setReportsDao(reportsDAOStub); // Use the setter to inject the stub
    }

    @Test
    public void testGetTotalBookings() {
        // Act
        int totalBookings = reportsService.getTotalBookings();

        // Assert
        assertEquals(10, totalBookings);
    }

    @Test
    public void testGetTotalRevenue() {
        // Act
        double totalRevenue = reportsService.getTotalRevenue();

        // Assert
        assertEquals(5000.0, totalRevenue, 0.01);
    }

    @Test
    public void testGetBookingCountByPricingStrategy() {
        // Arrange
        reportsDAOStub.bookingCountByPricingStrategy.put("Standard", 5);
        reportsDAOStub.bookingCountByPricingStrategy.put("Peak", 3);
        reportsDAOStub.bookingCountByPricingStrategy.put("Discount", 2);

        // Act
        Map<String, Integer> result = reportsService.getBookingCountByPricingStrategy();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(5, (int) result.get("Standard"));
        assertEquals(3, (int) result.get("Peak"));
        assertEquals(2, (int) result.get("Discount"));
    }

    @Test
    public void testGetBookingCountByVehicleType() {
        // Arrange
        reportsDAOStub.bookingCountByVehicleType.put("ZIP", 6);
        reportsDAOStub.bookingCountByVehicleType.put("PRO", 4);

        // Act
        Map<String, Integer> result = reportsService.getBookingCountByVehicleType();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(6, (int) result.get("ZIP"));
        assertEquals(4, (int) result.get("PRO"));
    }

    @Test
    public void testGetDriverCount() {
        // Act
        int driverCount = reportsService.getDriverCount();

        // Assert
        assertEquals(5, driverCount);
    }

    @Test
    public void testGetBookingCountByDriver() {
        // Arrange
        reportsDAOStub.bookingCountByDriver.put("Driver One", 3);
        reportsDAOStub.bookingCountByDriver.put("Driver Two", 2);

        // Act
        Map<String, Integer> result = reportsService.getBookingCountByDriver();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(3, (int) result.get("Driver One"));
        assertEquals(2, (int) result.get("Driver Two"));
    }

    @Test
    public void testGetCustomerCount() {
        // Act
        int customerCount = reportsService.getCustomerCount();

        // Assert
        assertEquals(20, customerCount);
    }

    @Test
    public void testGetHighestRevenueCustomer() {
        // Act
        CustomerRevenueDTO result = reportsService.getHighestRevenueCustomer();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCustomerId());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(1000.0, result.getRevenue(), 0.01);
    }

    @Test
    public void testGetHighestRevenueDriver() {
        // Act
        DriverRevenueDTO result = reportsService.getHighestRevenueDriver();

        // Assert
        assertNotNull(result);
        assertEquals("Driver One", result.getDriverName());
        assertEquals(2000.0, result.getRevenue(), 0.01);
    }

    @Test
    public void testGetUserCountByRole() {
        // Arrange
        reportsDAOStub.userCountByRole.put("admin", 2);
        reportsDAOStub.userCountByRole.put("employee", 5);

        // Act
        Map<String, Integer> result = reportsService.getUserCountByRole();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, (int) result.get("admin"));
        assertEquals(5, (int) result.get("employee"));
    }

    // Manual stub for ReportsDAO
    static class ReportsDAOStub extends ReportsDAO {
        private int totalBookings = 10;
        private double totalRevenue = 5000.0;
        private Map<String, Integer> bookingCountByPricingStrategy = new HashMap<>();
        private Map<String, Integer> bookingCountByVehicleType = new HashMap<>();
        private int driverCount = 5;
        private Map<String, Integer> bookingCountByDriver = new HashMap<>();
        private int customerCount = 20;
        private CustomerRevenueDTO highestRevenueCustomer = new CustomerRevenueDTO(1, "John Doe", 1000.0);
        private DriverRevenueDTO highestRevenueDriver = new DriverRevenueDTO("Driver One", 2000.0);
        private Map<String, Integer> userCountByRole = new HashMap<>();

        @Override
        public int getTotalBookings() {
            return totalBookings;
        }

        @Override
        public double getTotalRevenue() {
            return totalRevenue;
        }

        @Override
        public Map<String, Integer> getBookingCountByPricingStrategy() {
            return bookingCountByPricingStrategy;
        }

        @Override
        public Map<String, Integer> getBookingCountByVehicleType() {
            return bookingCountByVehicleType;
        }

        @Override
        public int getDriverCount() {
            return driverCount;
        }

        @Override
        public Map<String, Integer> getBookingCountByDriver() {
            return bookingCountByDriver;
        }

        @Override
        public int getCustomerCount() {
            return customerCount;
        }

        @Override
        public CustomerRevenueDTO getHighestRevenueCustomer() {
            return highestRevenueCustomer;
        }

        @Override
        public DriverRevenueDTO getHighestRevenueDriver() {
            return highestRevenueDriver;
        }

        @Override
        public Map<String, Integer> getUserCountByRole() {
            return userCountByRole;
        }
    }
}