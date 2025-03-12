package com.megacitycab.dao;

import com.megacitycab.model.Booking;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import com.megacitycab.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FleetDAO {

    public void save(Fleet fleet) {
        String sql = "INSERT INTO fleets (driver_name, vehicle_type, plate_number, driver_contact) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fleet.getDriverName());
            // Convert the VehicleType enum to a string using its name() method
            stmt.setString(2, fleet.getVehicleType().name());
            stmt.setString(3, fleet.getPlateNumber());
            stmt.setString(4, fleet.getDriverContact());
            stmt.executeUpdate();

            // Retrieve the generated fleet ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                fleet.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Fleet findById(int id) {
        String sql = "SELECT * FROM fleets WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String typeStr = rs.getString("vehicle_type");
                VehicleType vehicleType = null;
                if (typeStr != null) {
                    vehicleType = VehicleType.valueOf(typeStr.toUpperCase());
                }
                return new Fleet(
                        rs.getInt("id"),
                        rs.getString("driver_name"),
                        vehicleType,
                        rs.getString("plate_number"),
                        rs.getString("driver_contact")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Fleet> findAll() {
        List<Fleet> fleets = new ArrayList<>();
        String sql = "SELECT * FROM fleets";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String typeStr = rs.getString("vehicle_type");
                VehicleType vehicleType = null;
                if (typeStr != null) {
                    vehicleType = VehicleType.valueOf(typeStr.toUpperCase());
                }
                Fleet fleet = new Fleet(
                        rs.getInt("id"),
                        rs.getString("driver_name"),
                        vehicleType,
                        rs.getString("plate_number"),
                        rs.getString("driver_contact")
                );
                fleets.add(fleet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fleets;
    }

    // Update an existing fleet record
    public boolean update(Fleet fleet) {
        String sql = "UPDATE fleets SET driver_name = ?, vehicle_type = ?, plate_number = ?, driver_contact = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fleet.getDriverName());
            stmt.setString(2, fleet.getVehicleType().name());
            stmt.setString(3, fleet.getPlateNumber());
            stmt.setString(4, fleet.getDriverContact());
            stmt.setInt(5, fleet.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //used by booking management page to update bookings/billings table with billing information
    public void updateFleetInfo(Booking booking) {
        String sql = "UPDATE bookings SET customer_id = ?, fleet_id = ?, pickup = ?, destination = ?, distance = ?, booking_date = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getCustomer().getId());
            if (booking.getFleet() != null) {
                stmt.setInt(2, booking.getFleet().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, booking.getPickup());
            stmt.setString(4, booking.getDestination());
            stmt.setDouble(5, booking.getDistance());
            stmt.setObject(6, booking.getBookingDate());
            stmt.setInt(7, booking.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a fleet record by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM fleets WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search fleets across all columns (driver_name, vehicle_type, plate_number, driver_contact)
    public List<Fleet> searchAllColumns(String searchQuery) {
        List<Fleet> fleets = new ArrayList<>();
        String sql = "SELECT * FROM fleets WHERE driver_name LIKE ? OR vehicle_type LIKE ? OR plate_number LIKE ? OR driver_contact LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String queryParam = "%" + searchQuery + "%";
            stmt.setString(1, queryParam);
            stmt.setString(2, queryParam);
            stmt.setString(3, queryParam);
            stmt.setString(4, queryParam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String typeStr = rs.getString("vehicle_type");
                VehicleType vehicleType = null;
                if (typeStr != null) {
                    vehicleType = VehicleType.valueOf(typeStr.toUpperCase());
                }
                Fleet fleet = new Fleet(
                        rs.getInt("id"),
                        rs.getString("driver_name"),
                        vehicleType,
                        rs.getString("plate_number"),
                        rs.getString("driver_contact")
                );
                fleets.add(fleet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fleets;
    }
}