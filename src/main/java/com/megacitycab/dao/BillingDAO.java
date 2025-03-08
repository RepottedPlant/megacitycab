package com.megacitycab.dao;

import com.megacitycab.model.Billing;
import com.megacitycab.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDAO {
    public void save(Billing billing) {
        String sql = "INSERT INTO billing (booking_id, base_fare, tax, discount, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billing.getBooking().getId());
            stmt.setDouble(2, billing.getBaseFare());
            stmt.setDouble(3, billing.getTax());
            stmt.setDouble(4, billing.getDiscount());
            stmt.setDouble(5, billing.getTotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Billing findByBookingId(int bookingId) {
        String sql = "SELECT * FROM billing WHERE booking_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Billing(
                        rs.getInt("id"),
                        new BookingDAO().findById(rs.getInt("booking_id")),
                        rs.getDouble("base_fare"),
                        rs.getDouble("tax"),
                        rs.getDouble("discount"),
                        rs.getDouble("total")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}