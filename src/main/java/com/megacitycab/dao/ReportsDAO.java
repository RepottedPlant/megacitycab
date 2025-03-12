package com.megacitycab.dao;

import com.megacitycab.dto.CustomerRevenueDTO;
import com.megacitycab.dto.DriverRevenueDTO;
import com.megacitycab.util.DatabaseUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportsDAO {

    // Total number of bookings
    public int getTotalBookings() {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Total revenue generated (sum of billing total)
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total) FROM billings";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Bookings count by pricing strategy
    public Map<String, Integer> getBookingCountByPricingStrategy() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT pricing_type, COUNT(*) as count FROM billings GROUP BY pricing_type";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("pricing_type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // Bookings count by vehicle type (via fleets table)
    public Map<String, Integer> getBookingCountByVehicleType() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT f.vehicle_type, COUNT(*) as count " +
                "FROM bookings b JOIN fleets f ON b.fleet_id = f.id " +
                "GROUP BY f.vehicle_type";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("vehicle_type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // Number of drivers (distinct driver names in fleets)
    public int getDriverCount() {
        String sql = "SELECT COUNT(DISTINCT driver_name) FROM fleets WHERE driver_name IS NOT NULL";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Bookings count by driver
    public Map<String, Integer> getBookingCountByDriver() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT f.driver_name, COUNT(*) as count " +
                "FROM bookings b JOIN fleets f ON b.fleet_id = f.id " +
                "GROUP BY f.driver_name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("driver_name"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // Total number of customers
    public int getCustomerCount() {
        String sql = "SELECT COUNT(*) FROM customers";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Highest revenue generating customer
    public CustomerRevenueDTO getHighestRevenueCustomer() {
        String sql = "SELECT c.id, c.name, SUM(bi.total) as revenue " +
                "FROM customers c " +
                "JOIN bookings b ON c.id = b.customer_id " +
                "JOIN billings bi ON b.id = bi.booking_id " +
                "GROUP BY c.id, c.name " +
                "ORDER BY revenue DESC LIMIT 1";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CustomerRevenueDTO(rs.getInt("id"), rs.getString("name"), rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Highest revenue generating driver
    public DriverRevenueDTO getHighestRevenueDriver() {
        String sql = "SELECT f.driver_name, SUM(bi.total) as revenue " +
                "FROM fleets f " +
                "JOIN bookings b ON f.id = b.fleet_id " +
                "JOIN billings bi ON b.id = bi.booking_id " +
                "GROUP BY f.driver_name " +
                "ORDER BY revenue DESC LIMIT 1";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DriverRevenueDTO(rs.getString("driver_name"), rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // User count by role
    public Map<String, Integer> getUserCountByRole() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM users GROUP BY role";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("role"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}