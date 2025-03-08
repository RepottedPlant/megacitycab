package com.megacitycab.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static Connection connection;

    private DatabaseUtil() {} // Private constructor for Singleton

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3307/megacitycab", "root", ""
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to the database.");
            }
        }
        return connection;
    }
}