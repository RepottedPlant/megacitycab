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

        // Check if the user exists, the password matches, and the role is valid
        if (user != null && user.getPassword().equals(password) &&
                (user.getRole().equals("admin") || user.getRole().equals("employee"))) {
            return user; // Authentication successful
        } else {
            return null; // Authentication failed
        }
    }
}