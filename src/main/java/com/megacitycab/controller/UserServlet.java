package com.megacitycab.controller;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.model.User;
import com.megacitycab.service.UserService;
import com.megacitycab.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/protected/userManagement")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        System.out.println("[DEBUG] UserServlet init() called. Initializing UserService.");
        this.userService = new UserService(new UserDAO(), new NotificationService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[DEBUG] UserServlet doGet() called.");
        String action = req.getParameter("action");
        String searchQuery = req.getParameter("searchQuery");

        // Retrieve messages from session and clear them
        String success = (String) req.getSession().getAttribute("success");
        String error = (String) req.getSession().getAttribute("error");
        if (success != null) {
            req.setAttribute("success", success);
            req.getSession().removeAttribute("success");
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.getSession().removeAttribute("error");
        }

        List<User> users = new ArrayList<>(); // Initialize empty list

        if ("searchUsers".equals(action)) {
            // Handle search
            users = userService.searchUsers(searchQuery);
            req.setAttribute("users", users);
        } else if ("edit".equals(action)) {
            // Handle edit
            String userIdParam = req.getParameter("id");
            if (userIdParam != null && !userIdParam.isEmpty()) {
                try {
                    int userId = Integer.parseInt(userIdParam);
                    User user = userService.findUserById(userId);
                    if (user != null) {
                        req.setAttribute("user", user);
                    } else {
                        req.setAttribute("error", "User not found.");
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "Invalid User ID.");
                }
            } else {
                req.setAttribute("error", "User ID is missing.");
            }
        } else if ("delete".equals(action)) {
            // Handle deleting a user
            String userIdParam = req.getParameter("id");
            if (userIdParam != null && !userIdParam.isEmpty()) {
                try {
                    int userId = Integer.parseInt(userIdParam);
                    System.out.println("[DEBUG] Deleting user with ID: " + userId);
                    boolean isDeleted = userService.deleteUser(userId);
                    if (isDeleted) {
                        req.setAttribute("success", "User deleted successfully.");
                    } else {
                        req.setAttribute("error", "Failed to delete user.");
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "Invalid User ID.");
                }
            } else {
                req.setAttribute("error", "User ID is missing.");
            }
        } else {
            // Clear the form for new user creation
            req.setAttribute("user", new User());
        }

        // Load users for display ONLY if not searching
        if (!"searchUsers".equals(action)) {
            System.out.println("[DEBUG] Retrieving all users for display.");
            users = userService.findAllUsers();
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/protected/userManagement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[DEBUG] UserServlet doPost() called.");
        String action = req.getParameter("action");

        if ("createOrUpdateUser".equals(action)) {
            System.out.println("[DEBUG] Handling createOrUpdateUser action.");
            User user = new User();
            String userIdParam = req.getParameter("userId");

            try {
                // Populate user details from the form
                System.out.println("[DEBUG] Populating user details from the form.");
                user.setUsername(req.getParameter("username"));
                user.setPassword(req.getParameter("password"));
                user.setRole(req.getParameter("role")); // Ensure role is set from the form

                System.out.println("[DEBUG] User details: " + user);

                if (userIdParam != null && !userIdParam.isEmpty()) {
                    // Update existing user
                    System.out.println("[DEBUG] Updating existing user with ID: " + userIdParam);
                    user.setId(Integer.parseInt(userIdParam));
                    boolean isUpdated = userService.updateUser(user);
                    if (isUpdated) {
                        System.out.println("[DEBUG] User updated successfully: " + user.getId());
                        req.setAttribute("success", "User updated successfully.");
                    } else {
                        System.out.println("[DEBUG] Failed to update user: " + user.getId());
                        req.setAttribute("error", "Failed to update user.");
                    }
                } else {
                    // Create new user
                    System.out.println("[DEBUG] Creating new user.");
                    user = userService.createUser(user);
                    System.out.println("[DEBUG] User created successfully with ID: " + user.getId());
                    req.setAttribute("success", "User created successfully.");
                }
            } catch (Exception e) {
                System.out.println("[DEBUG] Error processing user: " + e.getMessage());
                e.printStackTrace();
                req.setAttribute("error", "Invalid input. Please check the fields.");
            }
        }

        // Retrieve all users for display
        System.out.println("[DEBUG] Retrieving all users for display.");
        List<User> users = userService.findAllUsers();
        req.setAttribute("users", users);

        // Forward to the user management page (reloads the page with updated data)
        System.out.println("[DEBUG] Forwarding to userManagement.jsp.");
        req.getRequestDispatcher("/WEB-INF/views/protected/userManagement.jsp").forward(req, resp);
    }
}