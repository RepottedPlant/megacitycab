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
        String sql = "INSERT INTO billings (booking_id, base_fare, tax, discount, total, pricing_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billing.getBooking().getId());
            stmt.setDouble(2, billing.getBaseFare());
            stmt.setDouble(3, billing.getTax());
            stmt.setDouble(4, billing.getDiscount());
            stmt.setDouble(5, billing.getTotal());
            stmt.setString(6, billing.getPricingType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Billing findByBookingId(int bookingId) {
        String sql = "SELECT * FROM billings WHERE booking_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Billing(
                        rs.getInt("id"),
                        new com.megacitycab.dao.BookingDAO().findById(rs.getInt("booking_id")),
                        rs.getDouble("base_fare"),
                        rs.getDouble("tax"),
                        rs.getDouble("discount"),
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
                        rs.getDouble("discount"),
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
