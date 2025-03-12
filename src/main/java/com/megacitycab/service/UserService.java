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
        System.out.println("[DEBUG] Initializing UserService with UserDAO and NotificationService.");
        this.userDao = userDao;
        this.notificationService = notificationService;

        // Register the UserNotifier observer
        notificationService.addObserver(User.class, new UserNotifier());
        System.out.println("[DEBUG] UserNotifier observer registered.");
    }

    // Create a new user
    public User createUser(User user) {
        System.out.println("[DEBUG] Entering createUser method.");
        try {
            // Validate user data
            validateUser(user);
            System.out.println("[DEBUG] User data validated successfully.");

            // Save the user to the database
            System.out.println("[DEBUG] Saving user to the database...");
            userDao.save(user);
            System.out.println("[DEBUG] User saved with ID: " + user.getId());

            // Notify observers (e.g., send welcome email)
            System.out.println("[DEBUG] Notifying observers...");
            notificationService.notifyObservers(user, "CREATED");
            System.out.println("[DEBUG] Observers notified.");

            return user;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in createUser: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Update an existing user
    public boolean updateUser(User user) {
        System.out.println("[DEBUG] Entering updateUser method.");
        try {
            // Validate user data
            validateUser(user);
            System.out.println("[DEBUG] User data validated successfully.");

            // Update the user in the database
            System.out.println("[DEBUG] Updating user in the database...");
            boolean isUpdated = userDao.update(user);
            if (isUpdated) {
                System.out.println("[DEBUG] User updated successfully: " + user.getId());

                // Notify observers (e.g., send update confirmation)
                System.out.println("[DEBUG] Notifying observers...");
                notificationService.notifyObservers(user, "UPDATED");
                System.out.println("[DEBUG] Observers notified.");
            } else {
                System.out.println("[DEBUG] Failed to update user: " + user.getId());
            }

            return isUpdated;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in updateUser: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Delete a user by ID
    public boolean deleteUser(int id) {
        System.out.println("[DEBUG] Entering deleteUser method.");
        try {
            // Validate user ID
            if (id <= 0) {
                System.out.println("[DEBUG] Invalid user ID: " + id);
                throw new IllegalArgumentException("Invalid user ID.");
            }

            // Delete the user from the database
            System.out.println("[DEBUG] Deleting user with ID: " + id);
            boolean isDeleted = userDao.delete(id);
            if (isDeleted) {
                System.out.println("[DEBUG] User deleted successfully: " + id);

                // Notify observers (e.g., send deletion confirmation)
                System.out.println("[DEBUG] Notifying observers...");
                User deletedUser = new User();
                deletedUser.setId(id);
                notificationService.notifyObservers(deletedUser, "DELETED");
                System.out.println("[DEBUG] Observers notified.");
            } else {
                System.out.println("[DEBUG] Failed to delete user: " + id);
            }

            return isDeleted;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in deleteUser: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Find a user by ID
    public User findUserById(int id) {
        System.out.println("[DEBUG] Entering findUserById method.");
        try {
            // Validate user ID
            if (id <= 0) {
                System.out.println("[DEBUG] Invalid user ID: " + id);
                throw new IllegalArgumentException("Invalid user ID.");
            }

            // Retrieve the user from the database
            System.out.println("[DEBUG] Retrieving user with ID: " + id);
            User user = userDao.findById(id);
            if (user != null) {
                System.out.println("[DEBUG] User found: " + user.getUsername());
            } else {
                System.out.println("[DEBUG] User not found with ID: " + id);
            }

            return user;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in findUserById: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Retrieve all users
    public List<User> findAllUsers() {
        System.out.println("[DEBUG] Entering findAllUsers method.");
        try {
            // Retrieve all users from the database
            System.out.println("[DEBUG] Retrieving all users...");
            List<User> users = userDao.findAll();
            System.out.println("[DEBUG] Retrieved " + users.size() + " users.");
            return users;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in findAllUsers: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Search users by username or role
    public List<User> searchUsers(String searchQuery) {
        System.out.println("[DEBUG] Entering searchUsers method.");
        try {
            // Validate search query
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                System.out.println("[DEBUG] Search query is empty. Returning all users.");
                return findAllUsers();
            }

            // Search users in the database
            System.out.println("[DEBUG] Searching users with query: " + searchQuery);
            List<User> users = userDao.findByUsernameOrRole(searchQuery);
            System.out.println("[DEBUG] Found " + users.size() + " users matching the query.");
            return users;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in searchUsers: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    // Validate user data
    private void validateUser(User user) {
        System.out.println("[DEBUG] Entering validateUser method.");
        try {
            if (user == null) {
                System.out.println("[DEBUG] User is null.");
                throw new IllegalArgumentException("User cannot be null.");
            }
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                System.out.println("[DEBUG] Username is empty.");
                throw new IllegalArgumentException("Username cannot be empty.");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                System.out.println("[DEBUG] Password is empty.");
                throw new IllegalArgumentException("Password cannot be empty.");
            }
            if (user.getRole() == null || user.getRole().trim().isEmpty()) {
                System.out.println("[DEBUG] Role is empty.");
                throw new IllegalArgumentException("Role cannot be empty.");
            }
            System.out.println("[DEBUG] User data validated successfully.");
        } catch (Exception e) {
            System.out.println("[DEBUG] Error in validateUser: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }
}