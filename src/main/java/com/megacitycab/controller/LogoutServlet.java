package com.megacitycab.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Debug: Print session before invalidation
        System.out.println("Session before invalidation: " + req.getSession(false));

        // Invalidate the session
        req.getSession().invalidate();

        // Debug: Print session after invalidation
        System.out.println("Session after invalidation: " + req.getSession(false)); // Should print null

        // Debug: Print redirect URL
        System.out.println("Redirecting to: " + req.getContextPath() + "/index.jsp");

        // Redirect to the index page
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}