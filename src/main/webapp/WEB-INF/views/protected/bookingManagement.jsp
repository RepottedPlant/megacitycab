<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Management</title>
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
        .toggle-button {
            margin: 10px 0;
            cursor: pointer;
            color: #007bff;
        }
        .hidden {
            display: none;
        }
    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/protected/dashboard">Back to Dashboard</a>
<h1>Booking Management</h1>

<!-- Search Bookings Section -->
<div class="form-section search-section">
    <h2>Search Bookings</h2>
    <form action="${pageContext.request.contextPath}/protected/bookingManagement" method="get">
        <input type="hidden" name="action" value="searchBookings">
        <input type="text" name="searchQuery" placeholder="Search Bookings" value="">
        <button type="submit">Search Bookings</button>
    </form>
</div>

<!-- Combined Booking, Billing, and Fleet Details Table -->
<c:if test="${not empty bookingBillingList}">
    <div class="form-section">
        <h2>Booking Details</h2>
        <table>
            <thead>
            <tr>
                <th>Booking ID</th>
                <th>Customer Name</th>
                <th>Pickup</th>
                <th>Destination</th>
                <th>Distance</th>
                <th>Driver Name</th>
                <th>Vehicle Type</th>
                <th>Pricing Type</th>
                <th>Base Fare</th>
                <th>Tax</th>
                <th>Discount</th>
                <th>Total</th>
                <th>Booking Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="dto" items="${bookingBillingList}">
                <tr>
                    <td>${dto.booking.id}</td>
                    <td>${dto.booking.customer.name}</td>
                    <td>${dto.booking.pickup}</td>
                    <td>${dto.booking.destination}</td>
                    <td>${dto.booking.distance}</td>
                    <td>${dto.booking.fleet.driverName}</td>
                    <td>${dto.booking.fleet.vehicleType}</td>
                    <td>${dto.billing.pricingType}</td>
                    <td>${dto.billing.baseFare}</td>
                    <td>${dto.billing.tax}</td>
                    <td>${dto.billing.discount}</td>
                    <td>${dto.billing.total}</td>
                    <td>${dto.booking.bookingDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
<%--

<!-- Search Customers Section -->
<div class="form-section search-section">
    <h2>Search Customers</h2>
    <form action="${pageContext.request.contextPath}/protected/bookingManagement" method="get">
        <input type="hidden" name="action" value="searchCustomers">
        <input type="text" name="searchQuery" placeholder="Enter Customer Name or Phone">
        <button type="submit">Search Customers</button>
    </form>
</div>

<!-- Existing Customers Table -->
<c:if test="${not empty customers}">
    <div class="form-section">
        <h2>Existing Customers</h2>
        <table>
            <thead>
            <tr>
                <th>Customer ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>NIC</th>
                <th>Phone</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="customer" items="${customers}">
                <tr>
                    <td>${customer.id}</td>
                    <td>${customer.name}</td>
                    <td>${customer.address}</td>
                    <td>${customer.nic}</td>
                    <td>${customer.phone}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
--%>

<!-- Booking Form -->
<div class="form-section">
    <h2>${empty param.id ? 'Create New' : 'Edit'} Booking</h2>
    <form action="${pageContext.request.contextPath}/protected/bookingManagement" method="post">
        <input type="hidden" name="bookingId" value="${booking.id}">

        <!-- Customer Section -->
        <div class="customer-section">
            <h3>Customer Details</h3>
            <!-- Existing Customer -->
            <div id="existingCustomer">
                <label>Select Existing Customer:</label>
                <select name="customerId">
                    <option value="">-- Select Customer --</option>
                    <c:forEach items="${customers}" var="customer">
                        <option value="${customer.id}" ${customer.id == booking.customer.id ? 'selected' : ''}>
                                ${customer.name} (${customer.phone})
                        </option>
                    </c:forEach>
                </select>
                <span class="toggle-button" onclick="toggleCustomerForm()">New Customer?</span>
            </div>
            <!-- New Customer (Initially Hidden) -->
            <div id="newCustomer" class="hidden">
                <label>New Customer Details:</label>
                <input type="text" name="newCustomerName" placeholder="Full Name">
                <input type="text" name="newCustomerPhone" placeholder="Phone Number">
                <input type="text" name="newCustomerNIC" placeholder="NIC">
                <input type="text" name="newCustomerAddress" placeholder="Address">
                <span class="toggle-button" onclick="toggleCustomerForm()">Use Existing Customer</span>
            </div>
        </div>

        <!-- Ride Details -->
        <div class="ride-details">
            <h3>Ride Information</h3>
            <label>Pickup Location:</label>
            <input type="text" name="pickup" value="${booking.pickup}" required>

            <label>Destination:</label>
            <input type="text" name="destination" value="${booking.destination}" required>

            <label>Distance (km):</label>
            <input type="number" name="distance" step="0.1" value="${booking.distance}" required>
        </div>

        <!-- Vehicle Selection -->
        <div class="vehicle-section">
            <h3>Vehicle Details</h3>
            <select name="fleetId" required>
                <option value="">-- Select Vehicle --</option>
                <c:forEach items="${vehicles}" var="vehicle">
                    <option value="${vehicle.id}" ${vehicle.id == booking.fleet.id ? 'selected' : ''}>
                            ${vehicle.vehicle_type} (${vehicle.plate_number})
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Pricing Strategy -->
        <div class="pricing-section">
            <label>Pricing Strategy:</label>
            <select name="pricingStrategy" required>
                <option value="standard" ${booking.pricingStrategy == 'standard' ? 'selected' : ''}>Standard</option>
                <option value="discount" ${booking.pricingStrategy == 'discount' ? 'selected' : ''}>Discount</option>
                <option value="peak" ${booking.pricingStrategy == 'peak' ? 'selected' : ''}>Peak</option>
            </select>
        </div>

        <!-- Form Actions -->
        <div class="form-actions">
            <button type="submit">${empty param.id ? 'Create Booking' : 'Update Booking'}</button>
            <c:if test="${not empty param.id}">
                <button type="button" onclick="confirmDelete(${booking.id})">Delete Booking</button>
            </c:if>
        </div>
    </form>
</div>

<script>
    function toggleCustomerForm() {
        const existing = document.getElementById('existingCustomer');
        const newCustomer = document.getElementById('newCustomer');
        existing.classList.toggle('hidden');
        newCustomer.classList.toggle('hidden');
    }

    function confirmDelete(bookingId) {
        if (confirm('Are you sure you want to delete this booking?')) {
            window.location.href = '${pageContext.request.contextPath}/protected/bookingManagement?action=delete&id=' + bookingId;
        }
    }
</script>
</body>
</html>
