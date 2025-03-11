package com.megacitycab.dao;

import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import com.megacitycab.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FleetDAO {
    public void save(Fleet fleet) {
        String sql = "INSERT INTO fleets (driver_name, vehicle_type, plate_number, driver_contact) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fleet.getDriverName());
            // Convert the VehicleType enum to a string using its name() method
            stmt.setString(2, fleet.getVehicleType().name());
            stmt.setString(3, fleet.getPlateNumber());
            stmt.setString(4, fleet.getDriverContact());
            stmt.executeUpdate();
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
                // Get the "vehicle_type" column as a String
                String typeStr = rs.getString("vehicle_type");
                VehicleType vehicleType = null;
                if (typeStr != null) {
                    // Convert the String to the VehicleType enum
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
}
