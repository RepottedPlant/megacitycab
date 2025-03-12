package com.megacitycab.service;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.model.User;
import com.megacitycab.observer.UserNotifier;

import java.util.List;

public class UserService {
    private final UserDAO userDao;
    private final NotificationService notificationService;

    // Constructor Injection (DIP)
    public UserService(UserDAO userDao, NotificationService notificationService) {
        this.userDao = userDao;
        this.notificationService = notificationService;

        // Register the UserNotifier observer
        notificationService.addObserver(User.class, new UserNotifier());
    }

    // Create a new user
    public User createUser(User user) {
        try {
            // Validate user data
            validateUser(user);

            // Save the user to the database
            userDao.save(user);

            // Notify observers (e.g., send welcome email)
            notificationService.notifyObservers(user, "You have been added to Mega City Cabs system " +user.getUsername());

            return user;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Update an existing user
    public boolean updateUser(User user) {
        try {
            // Validate user data
            validateUser(user);

            // Update the user in the database
            boolean isUpdated = userDao.update(user);
            if (isUpdated) {
                // Notify observers (e.g., send update confirmation)
                notificationService.notifyObservers(user, "You details have been updated " +user.getUsername());
            } else {
                throw new IllegalArgumentException("Failed to update user.");
            }

            return isUpdated;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Delete a user by ID
    public boolean deleteUser(int id) {
        try {
            // Validate user ID
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid user ID.");
            }

            // Fetch the user's username before deletion
            String deletedUsername = userDao.findById(id).getUsername();

            // Delete the user from the database
            boolean isDeleted = userDao.delete(id);
            if (isDeleted) {
                // Notify observers (e.g., send deletion confirmation)
                User deletedUser = new User();
                deletedUser.setId(id);
                notificationService.notifyObservers(deletedUser, "You have been removed from Mega City Cabs system " + deletedUsername);
            } else {
                throw new IllegalArgumentException("Failed to delete user.");
            }

            return isDeleted;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Find a user by ID
    public User findUserById(int id) {
        try {
            // Validate user ID
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid user ID.");
            }
            // Retrieve the user from the database
            User user = userDao.findById(id);
            if (user != null) {
                System.out.println("User found: " + user.getUsername());
            } else {
                throw new IllegalArgumentException("User not found with ID.");
            }
            return user;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Retrieve all users
    public List<User> findAllUsers() {
        try {
            // Retrieve all users from the database
            List<User> users = userDao.findAll();
            return users;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Search users by username or role
    public List<User> searchUsers(String searchQuery) {
        try {
            // Validate search query
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                return findAllUsers();
            }
            // Search users in the database
            List<User> users = userDao.findByUsernameOrRole(searchQuery);
            return users;
        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }

    // Validate user data
    private void validateUser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null.");
            }
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty.");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }
            if (user.getRole() == null || user.getRole().trim().isEmpty()) {
                throw new IllegalArgumentException("Role cannot be empty.");
            }

        } catch (Exception e) {
            throw e; // Re-throw the exception for further handling
        }
    }
}