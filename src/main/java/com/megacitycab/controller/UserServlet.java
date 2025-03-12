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

        this.userService = new UserService(new UserDAO(), new NotificationService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

            users = userService.findAllUsers();
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/protected/userManagement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("createOrUpdateUser".equals(action)) {

            User user = new User();
            String userIdParam = req.getParameter("userId");

            try {
                // Populate user details from the form

                user.setUsername(req.getParameter("username"));
                user.setPassword(req.getParameter("password"));
                user.setRole(req.getParameter("role")); // Ensure role is set from the form


                if (userIdParam != null && !userIdParam.isEmpty()) {
                    // Update existing user

                    user.setId(Integer.parseInt(userIdParam));
                    boolean isUpdated = userService.updateUser(user);
                    if (isUpdated) {
                        req.setAttribute("success", "User updated successfully.");
                    } else {
                        req.setAttribute("error", "Failed to update user.");
                    }
                } else {
                    // Create new user

                    user = userService.createUser(user);
                    req.setAttribute("success", "User created successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Invalid input. Please check the fields.");
            }
        }

        // Retrieve all users for display

        List<User> users = userService.findAllUsers();
        req.setAttribute("users", users);

        // Forward to the user management page (reloads the page with updated data)

        req.getRequestDispatcher("/WEB-INF/views/protected/userManagement.jsp").forward(req, resp);
    }
}