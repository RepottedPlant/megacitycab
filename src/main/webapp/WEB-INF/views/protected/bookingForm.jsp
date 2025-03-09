<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 09-Mar-25
  Time: 1:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book a Cab</title>
</head>
<body>
<h1>Book a Cab</h1>
<form action="${pageContext.request.contextPath}/booking" method="post">
    <!-- Customer Details -->
    <label for="customerId">Customer ID:</label>
    <input type="number" id="customerId" name="customerId" required>

    <label for="customerName">Name:</label>
    <input type="text" id="customerName" name="customerName" required>

    <label for="nic">NIC:</label>
    <input type="text" id="nic" name="nic" required>

    <!-- Fleet Details -->
    <label for="fleetId">Vehicle ID:</label>
    <input type="number" id="fleetId" name="fleetId" required>

    <!-- Ride Details -->
    <label for="pickup">Pickup Location:</label>
    <input type="text" id="pickup" name="pickup" required>

    <label for="destination">Destination:</label>
    <input type="text" id="destination" name="destination" required>

    <label for="distance">Distance (km):</label>
    <input type="number" id="distance" name="distance" step="0.1" required>

    <!-- Pricing Strategy -->
    <label for="pricingStrategy">Pricing Strategy:</label>
    <select id="pricingStrategy" name="pricingStrategy" required>
        <option value="standard">Standard</option>
        <option value="discount">Discount</option>
        <option value="peak">Peak</option>
    </select>

    <button type="submit">Book Ride</button>
</form>
</body>
</html>