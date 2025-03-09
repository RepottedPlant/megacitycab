package com.megacitycab.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    public static Connection getConnection() {
        try {
            // Debug: Print connection creation
            System.out.println("Creating new database connection...");

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3307/megacitycab", "root", ""
            );

            // Debug: Print successful connection
            System.out.println("Database connection established: " + connection);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }
}