<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Dashboard - Megacity Cab</title>
  <style>
    :root {
      --primary-color: #2c3e50;
      --secondary-color: #3498db;
      --background-color: #f8f9fa;
      --text-color: #2c3e50;
    }
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: var(--background-color);
      color: var(--text-color);
      margin: 0;
      padding: 20px;
    }
    .container {
      max-width: 1200px;
      margin: 0 auto;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
    }
    h1 {
      color: var(--primary-color);
      font-size: 2.5rem;
      margin: 0;
    }
    .btn {
      display: inline-block;
      padding: 10px 20px;
      background-color: var(--secondary-color);
      color: white;
      text-decoration: none;
      border-radius: 5px;
      transition: background-color 0.3s;
      border: none;
      cursor: pointer;
    }
    .btn:hover {
      background-color: #2980b9;
    }
    .dashboard-card {
      background: white;
      border-radius: 10px;
      padding: 20px;
      box-shadow: 0 2px 15px rgba(0,0,0,0.1);
      margin-bottom: 30px;
    }
    .dashboard-card h2 {
      color: var(--secondary-color);
      margin-bottom: 15px;
    }
    /* Grid layout for the navigation menu */
    .menu-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 20px;
    }
    /* Menu items styled like cards/buttons */
    .menu-item {
      background-color: var(--secondary-color);
      color: #fff;
      border-radius: 5px;
      padding: 30px 0;
      text-align: center;
      transition: background-color 0.3s ease;
      text-decoration: none;
      font-weight: bold;
      text-transform: uppercase;
    }
    .menu-item:hover {
      background-color: #2980b9;
    }
    @media (max-width: 768px) {
      h1 {
        font-size: 2rem;
      }
    }
  </style>
</head>
<body>
<div class="container">
  <!-- Header Section -->
  <div class="header">
    <h1>Dashboard</h1>
    <!-- Displaying user info in plain text; you can style further if needed -->
    <div>
      <a href="${pageContext.request.contextPath}/logout" class="btn">Logout</a>
    </div>
  </div>

  <div class="dashboard-card" style="margin-bottom: 10px;">
    <p style="margin: 0;">Welcome, ${user.username} (${user.role})</p>
  </div>

  <!-- Main Navigation -->
  <div class="dashboard-card">
    <h2>Navigation</h2>
    <div class="menu-grid">
      <a href="${pageContext.request.contextPath}/protected/bookingManagement" class="menu-item">
        <h3>Booking Management</h3>
      </a>
      <a href="${pageContext.request.contextPath}/protected/customerManagement" class="menu-item">
        <h3>Customer Management</h3>
      </a>
      <a href="${pageContext.request.contextPath}/protected/fleetManagement" class="menu-item">
        <h3>Fleet Management</h3>
      </a>
      <c:if test="${user.role == 'admin'}">
        <a href="${pageContext.request.contextPath}/protected/userManagement" class="menu-item">
          <h3>User Management</h3>
        </a>
        <a href="${pageContext.request.contextPath}/protected/reports" class="menu-item">
          <h3>Reports</h3>
        </a>
      </c:if>
    </div>
  </div>

</div>
</body>
</html>
