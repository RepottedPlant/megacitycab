package com.megacitycab.dao;

import com.megacitycab.model.Fleet;
import com.megacitycab.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FleetDAO {
    public void save(Fleet fleet) {
        String sql = "INSERT INTO fleet (driver_name, vehicle_type, plate_number) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fleet.getDriverName());
            stmt.setString(2, fleet.getVehicleType());
            stmt.setString(3, fleet.getPlateNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Fleet findById(int id) {
        String sql = "SELECT * FROM fleet WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Fleet(
                        rs.getInt("id"),
                        rs.getString("driver_name"),
                        rs.getString("vehicle_type"),
                        rs.getString("plate_number"),
                        rs.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}