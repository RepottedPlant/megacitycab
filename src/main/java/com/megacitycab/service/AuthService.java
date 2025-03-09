package com.megacitycab.service;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.model.User;

public class AuthService {
    private final UserDAO userDao;

    public AuthService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public User login(String username, String password) {
        // Fetch the user by username
        User user = userDao.findByUsername(username);

        // Debug: Print the user fetched from the database
        System.out.println("User fetched from DB: " + user);

        // Check if the user exists, the password matches, and the role is valid
        if (user != null && user.getPassword().equals(password) &&
                (user.getRole().equals("admin") || user.getRole().equals("employee"))) {
            // Debug: Print successful login
            System.out.println("Login successful - User: " + user.getUsername());
            return user; // Authentication successful
        } else {
            // Debug: Print failed login
            System.out.println("Login failed - Invalid credentials or role");
            return null; // Authentication failed
        }
    }
}