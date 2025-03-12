package com.megacitycab.dao;

import com.megacitycab.model.Billing;
import com.megacitycab.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    public void save(Billing billing) {
        String sql = "INSERT INTO billings (booking_id, base_fare, tax, total, pricing_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billing.getBooking().getId()); // Ensure booking ID is set
            stmt.setDouble(2, billing.getBaseFare());
            stmt.setDouble(3, billing.getTax());
            stmt.setDouble(4, billing.getTotal());
            stmt.setString(5, billing.getPricingType());
            stmt.executeUpdate();
            System.out.println("[SERVER LOG] Billing saved successfully for booking ID: " + billing.getBooking().getId());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving billing: " + e.getMessage());
        }
    }

    public Billing findByBookingId(int bookingId) {
        String sql = "SELECT * FROM billings WHERE booking_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Billing(
                        rs.getInt("id"),
                        new com.megacitycab.dao.BookingDAO().findById(rs.getInt("booking_id")),
                        rs.getDouble("base_fare"),
                        rs.getDouble("tax"),
                        rs.getDouble("total"),
                        rs.getString("pricing_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve all billing records
    public List<Billing> findAll() {
        List<Billing> billings = new ArrayList<>();
        String sql = "SELECT * FROM billings";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Billing billing = new Billing(
                        rs.getInt("id"),
                        new com.megacitycab.dao.BookingDAO().findById(rs.getInt("booking_id")),
                        rs.getDouble("base_fare"),
                        rs.getDouble("tax"),
                        rs.getDouble("total"),
                        rs.getString("pricing_type")
                );
                billings.add(billing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billings;
    }
}