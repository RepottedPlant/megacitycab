package com.megacitycab.dao;

import com.megacitycab.model.Booking;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void save(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, fleet_id, pickup, destination, distance, booking_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getCustomer().getId());
            // If fleet is null, set the parameter as SQL NULL
            if (booking.getFleet() != null) {
                stmt.setInt(2, booking.getFleet().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
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

    // New method to retrieve all bookings
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new CustomerDAO().findById(rs.getInt("customer_id"));
                Fleet fleet = new FleetDAO().findById(rs.getInt("fleet_id"));
                double distance = rs.getDouble("distance");

                Booking booking = new Booking(
                        rs.getInt("id"),
                        customer,
                        fleet,
                        rs.getString("pickup"),
                        rs.getString("destination"),
                        distance,
                        rs.getObject("booking_date", LocalDateTime.class)
                );
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> searchAllColumns(String searchQuery) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.* FROM bookings b " +
                "JOIN customers c ON b.customer_id = c.id " +
                "WHERE b.id = ? OR c.name LIKE ? OR c.phone LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the search query
            stmt.setString(1, searchQuery); // Booking ID
            stmt.setString(2, "%" + searchQuery + "%"); // Customer name
            stmt.setString(3, "%" + searchQuery + "%"); // Customer phone

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new CustomerDAO().findById(rs.getInt("customer_id"));
                Fleet fleet = new FleetDAO().findById(rs.getInt("fleet_id"));
                double distance = rs.getDouble("distance");

                Booking booking = new Booking(
                        rs.getInt("id"),
                        customer,
                        fleet,
                        rs.getString("pickup"),
                        rs.getString("destination"),
                        distance,
                        rs.getObject("booking_date", LocalDateTime.class)
                );
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
