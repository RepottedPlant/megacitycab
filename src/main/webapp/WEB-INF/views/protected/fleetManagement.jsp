<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Fleet Management</title>
  <style>
    .form-section {
      margin: 20px 0;
      padding: 15px;
      border: 1px solid #ddd;
      border-radius: 5px;
    }
    .search-section {
      margin-bottom: 30px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }
    table, th, td {
      border: 1px solid #ddd;
    }
    th, td {
      padding: 8px;
      text-align: left;
    }
    th {
      background-color: #f2f2f2;
    }
  </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/protected/dashboard">Back to Dashboard</a>
<h1>Fleet Management</h1>

<!-- Display Success/Error Messages -->
<c:if test="${not empty success}">
  <div class="success-message">${success}</div>
</c:if>
<c:if test="${not empty error}">
  <div class="error-message">${error}</div>
</c:if>

<!-- Search Fleets Section -->
<div class="form-section search-section">
  <h2>Search Fleets</h2>
  <form action="${pageContext.request.contextPath}/protected/fleetManagement" method="get">
    <input type="hidden" name="action" value="searchFleets">
    <input type="text" name="searchQuery" placeholder="Enter Driver Name, Vehicle Type, Plate Number, or Driver Contact">
    <button type="submit">Search Fleets</button>
  </form>
</div>

<!-- Existing Fleets Table -->
<c:if test="${not empty fleets}">
  <div class="form-section">
    <h2>Existing Fleets</h2>
    <table>
      <thead>
      <tr>
        <th>Fleet ID</th>
        <th>Driver Name</th>
        <th>Vehicle Type</th>
        <th>Plate Number</th>
        <th>Driver Contact</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="fleet" items="${fleets}">
        <tr>
          <td>${fleet.id}</td>
          <td>${fleet.driverName}</td>
          <td>${fleet.vehicleType}</td>
          <td>${fleet.plateNumber}</td>
          <td>${fleet.driverContact}</td>
          <td>
            <a href="${pageContext.request.contextPath}/protected/fleetManagement?action=edit&id=${fleet.id}">Edit</a>
            <a href="${pageContext.request.contextPath}/protected/fleetManagement?action=delete&id=${fleet.id}" onclick="return confirm('Are you sure you want to delete this fleet?')">Delete</a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</c:if>

<!-- Fleet Form -->
<div class="form-section">
  <h2>${empty param.id ? 'Create New' : 'Edit'} Fleet</h2>
  <form action="${pageContext.request.contextPath}/protected/fleetManagement" method="post">
    <input type="hidden" name="fleetId" value="${fleet.id}">
    <input type="hidden" name="action" value="createOrUpdateFleet">

    <div class="form-section">
      <label>Driver Name:</label>
      <input type="text" name="driverName" value="${fleet.driverName}" required>
    </div>

    <div class="form-section">
      <label>Vehicle Type:</label>
      <select name="vehicleType" required>
        <option value="">-- Select Vehicle Type --</option>
        <option value="ZIP" ${fleet.vehicleType == 'ZIP' ? 'selected' : ''}>ZIP</option>
        <option value="PRO" ${fleet.vehicleType == 'PRO' ? 'selected' : ''}>PRO</option>
        <option value="BLACK" ${fleet.vehicleType == 'BLACK' ? 'selected' : ''}>BLACK</option>
      </select>
    </div>

    <div class="form-section">
      <label>Plate Number:</label>
      <input type="text" name="plateNumber" value="${fleet.plateNumber}" required>
    </div>

    <div class="form-section">
      <label>Driver Contact:</label>
      <input type="text" name="driverContact" value="${fleet.driverContact}" required>
    </div>

    <div class="form-actions">
      <button type="submit">${empty param.id ? 'Create Fleet' : 'Update Fleet'}</button>
    </div>
  </form>
</div>
</body>
</html>
