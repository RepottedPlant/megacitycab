<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard - Megacity Cab</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f4f4f4;
    }
    .header {
      background-color: #333;
      color: #fff;
      padding: 10px 20px;
      text-align: center;
    }
    .container {
      max-width: 1200px;
      margin: 20px auto;
      padding: 20px;
      background-color: #fff;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    .menu {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      margin-top: 20px;
    }
    .menu-item {
      flex: 1 1 calc(25% - 20px);
      padding: 20px;
      background-color: #007bff;
      color: #fff;
      text-align: center;
      text-decoration: none;
      border-radius: 5px;
      transition: background-color 0.3s ease;
    }
    .menu-item:hover {
      background-color: #0056b3;
    }
    .logout {
      margin-top: 20px;
      text-align: center;
    }
    .logout a {
      color: #007bff;
      text-decoration: none;
    }
    .logout a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
<div class="header">
  <h1>Dashboard</h1>
  <p>Welcome, ${user.username} (${user.role})</p>
  <p>Debug: User Role = ${user.role}</p> <!-- Debug statement -->
</div>

<div class="container">
  <div class="menu">
    <a href="${pageContext.request.contextPath}/protected/bookingManagement" class="menu-item">
      <h2>Booking Management</h2>
    </a>
    <a href="${pageContext.request.contextPath}/protected/customer-management" class="menu-item">
      <h2>Customer Management</h2>
    </a>
    <a href="${pageContext.request.contextPath}/protected/fleet-management" class="menu-item">
      <h2>Fleet Management</h2>
    </a>
  </div>
    <%-- Show User Management and Reports only for Admins --%>
  <c:if test="${user.role == 'admin'}">
    <div class="menu">
      <a href="${pageContext.request.contextPath}/protected/user-management" class="menu-item">
        <h2>User Management</h2>
      </a>
      <a href="${pageContext.request.contextPath}/protected/reports" class="menu-item">
        <h2>Reports</h2>
      </a>
    </div>
  </c:if>

  <div class="logout">
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
  </div>
</div>
</body>
</html>