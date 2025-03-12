package com.megacitycab.controller;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.model.User;
import com.megacitycab.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() {
        this.authService = new AuthService(new UserDAO());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Display the login form
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Authenticate the user
        User user = authService.login(username, password);

        if (user != null) {
            // Login successful
            req.getSession().setAttribute("user", user);

            // Redirect to the dashboard
            resp.sendRedirect(req.getContextPath() + "/protected/dashboard");
        } else {
            // Login failed
            req.setAttribute("error", "Invalid username or password");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}