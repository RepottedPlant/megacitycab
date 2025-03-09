package com.megacitycab.dao;

import com.megacitycab.model.Booking;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BookingDAO {
    public void save(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, fleet_id, pickup, destination, distance, booking_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getCustomer().getId());
            stmt.setInt(2, booking.getFleet() != null ? booking.getFleet().getId() : null);
            stmt.setString(3, booking.getPickup());
            stmt.setString(4, booking.getDestination());
            stmt.setDouble(5, booking.getDistance());
            stmt.setObject(6, booking.getBookingDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new CustomerDAO().findById(rs.getInt("customer_id"));
                Fleet fleet = new FleetDAO().findById(rs.getInt("fleet_id"));
                double distance = rs.getDouble("distance");

                return new Booking(
                        rs.getInt("id"),
                        customer,
                        fleet,
                        rs.getString("pickup"),
                        rs.getString("destination"),
                        distance,
                        rs.getObject("booking_date", LocalDateTime.class)

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}