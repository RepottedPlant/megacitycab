package com.megacitycab.dao;

import com.megacitycab.model.Booking;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
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
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

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
            stmt.executeUpdate();

            // Retrieve the generated booking ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                booking.setId(rs.getInt(1)); // Set the generated ID
                System.out.println("Booking saved with ID: " + booking.getId());
            }
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
        String sql = "SELECT b.*, c.name AS customer_name, c.phone AS customer_phone, " +
                "f.driver_name, f.vehicle_type " +
                "FROM bookings b " +
                "JOIN customers c ON b.customer_id = c.id " +
                "LEFT JOIN fleets f ON b.fleet_id = f.id " +
                "WHERE b.id = ? OR LOWER(c.name) LIKE LOWER(?) OR LOWER(c.phone) LIKE LOWER(?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Parse booking ID if searchQuery is numeric
            int bookingId = 0;
            try {
                bookingId = Integer.parseInt(searchQuery);
            } catch (NumberFormatException e) {
                // If searchQuery is not a valid integer, set bookingId to 0 (no match)
                bookingId = 0;
            }

            // Set parameters for the search query
            stmt.setInt(1, bookingId); // Booking ID
            stmt.setString(2, "%" + searchQuery + "%"); // Customer name (case-insensitive)
            stmt.setString(3, "%" + searchQuery + "%"); // Customer phone (case-insensitive)

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Create Customer object
                Customer customer = new Customer();
                customer.setId(rs.getInt("customer_id"));
                customer.setName(rs.getString("customer_name"));
                customer.setPhone(rs.getString("customer_phone"));

                // Create Fleet object (if fleet_id is not null)
                Fleet fleet = null;
                if (rs.getObject("fleet_id") != null) {
                    fleet = new Fleet();
                    fleet.setId(rs.getInt("fleet_id"));
                    fleet.setDriverName(rs.getString("driver_name"));
                    fleet.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                }

                // Create Booking object
                Booking booking = new Booking(
                        rs.getInt("id"),
                        customer,
                        fleet,
                        rs.getString("pickup"),
                        rs.getString("destination"),
                        rs.getDouble("distance"),
                        rs.getObject("booking_date", LocalDateTime.class)
                );
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Log the error or rethrow as a custom exception
        }
        return bookings;
    }

}
