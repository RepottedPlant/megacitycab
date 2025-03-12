<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Management</title>
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
        .form-section {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 15px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        .form-section h2, .form-section h3 {
            color: var(--secondary-color);
            margin-bottom: 15px;
        }
        input[type="text"],
        input[type="number"],
        input[type="password"],
        select {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .form-actions button {
            padding: 10px 20px;
            background-color: var(--secondary-color);
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .form-actions button:hover {
            background-color: #2980b9;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th {
            background-color: var(--primary-color);
            color: white;
            padding: 12px;
            text-align: left;
        }
        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }
        tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        tr:hover {
            background-color: #f1f4f7;
        }
        .hidden {
            display: none;
        }
        /* Flex container for customer section headers */
        .customer-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
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
    <div class="header">
        <h1>Booking Management</h1>
        <a href="${pageContext.request.contextPath}/protected/dashboard" class="btn">Back to Dashboard</a>
    </div>

    <!-- Display Success/Error Messages -->
    <c:if test="${not empty success}">
        <div class="form-section">
            <p style="color: green;">${success}</p>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="form-section">
            <p style="color: red;">${error}</p>
        </div>
    </c:if>

    <!-- Search Bookings Section -->
    <div class="form-section">
        <h2>Search Bookings</h2>
        <form action="${pageContext.request.contextPath}/protected/bookingManagement" method="get">
            <input type="hidden" name="action" value="searchBookings">
            <input type="text" name="searchQuery" placeholder="Search Bookings" value="">
            <button type="submit" class="btn">Search Bookings</button>
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
                        <td>LKR ${dto.billing.baseFare}</td>
                        <td>LKR ${dto.billing.tax}</td>
                        <td>LKR ${dto.billing.total}</td>
                        <td>${dto.booking.bookingDate}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <!-- Booking Form -->
    <div class="form-section">
        <h2>Create New Booking</h2>
        <form action="${pageContext.request.contextPath}/protected/bookingManagement" method="post">
            <input type="hidden" name="bookingId" value="${booking.id}">

            <!-- Customer Section -->
            <div class="form-section">
                <div class="customer-header">
                    <h3>Customer Details</h3>
                    <button type="button" class="btn" onclick="toggleCustomerForm()" id="toggleCustomerButton">New Customer?</button>
                </div>
                <div id="existingCustomer">
                    <h3>Select Existing Customer</h3>
                    <select name="customerId">
                        <option value="">-- Select Customer --</option>
                        <c:forEach items="${customers}" var="customer">
                            <option value="${customer.id}" ${customer.id == booking.customer.id ? 'selected' : ''}>
                                    ${customer.name} (${customer.phone})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div id="newCustomer" class="hidden">
                    <h3>New Customer Details</h3>
                    <input type="text" name="newCustomerName" placeholder="Full Name">
                    <input type="text" name="newCustomerPhone" placeholder="Phone Number">
                    <input type="text" name="newCustomerNIC" placeholder="NIC">
                    <input type="text" name="newCustomerAddress" placeholder="Address">
                </div>
            </div>

            <!-- Ride Details Section -->
            <div class="form-section">
                <h3>Ride Information</h3>
                <label>Pickup Location:</label>
                <input type="text" name="pickup" value="${booking.pickup}" required>
                <label>Destination:</label>
                <input type="text" name="destination" value="${booking.destination}" required>
                <label>Distance (km):</label>
                <input type="number" name="distance" step="0.1" value="${booking.distance}" required>
            </div>

            <!-- Vehicle Section -->
            <div class="form-section">
                <h3>Available Fleets</h3>
                <table>
                    <thead>
                    <tr>
                        <th>Driver Name</th>
                        <th>Vehicle Type</th>
                        <th>Plate Number</th>
                        <th>Driver Contact</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="fleet" items="${fleets}">
                        <tr>
                            <td>${fleet.driverName}</td>
                            <td>${fleet.vehicleType}</td>
                            <td>${fleet.plateNumber}</td>
                            <td>${fleet.driverContact}</td>
                            <td>
                                <button type="button" class="btn" onclick="assignFleet(${fleet.id}, '${fleet.driverName}', '${fleet.vehicleType}', '${fleet.plateNumber}', '${fleet.driverContact}', '${fleet.vehicleType.basePrice}', '${fleet.vehicleType.ratePerKm}')">Assign</button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Hidden Input Fields for Fleet Assignment -->
            <input type="hidden" id="fleetId" name="fleetId" value="">
            <input type="hidden" id="fleetDetails" name="fleetDetails" value="">
            <input type="hidden" id="basePrice" name="basePrice" value="">
            <input type="hidden" id="ratePerKm" name="ratePerKm" value="">

            <!-- Pricing Strategy Section -->
            <div class="form-section">
                <label>Pricing Strategy:</label>
                <select name="pricingStrategy" required>
                    <option value="standard" ${booking.pricingStrategy == 'Standard' ? 'selected' : ''}>Standard</option>
                    <option value="discount" ${booking.pricingStrategy == 'Discount' ? 'selected' : ''}>Discount</option>
                    <option value="peak" ${booking.pricingStrategy == 'Peak' ? 'selected' : ''}>Peak</option>
                </select>
            </div>

            <!-- Billing Details Section -->
            <div class="form-section">
                <h3>Billing Details</h3>
                <table>
                    <tr>
                        <th>Base Fare</th>
                        <td>LKR <span id="displaySubtotal">0.00</span></td>
                    </tr>
                    <tr>
                        <th>Tax (12%)</th>
                        <td>LKR <span id="displayTax">0.00</span></td>
                    </tr>
                    <tr>
                        <th>Total</th>
                        <td>LKR <span id="displayTotal">0.00</span></td>
                    </tr>
                </table>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <button type="submit" class="btn">Create Booking</button>
            </div>
        </form>
    </div>
</div>

<script>
    function toggleCustomerForm() {
        const existing = document.getElementById('existingCustomer');
        const newCustomer = document.getElementById('newCustomer');
        const toggleButton = document.getElementById('toggleCustomerButton');

        if (existing.classList.contains('hidden')) {
            existing.classList.remove('hidden');
            newCustomer.classList.add('hidden');
            toggleButton.textContent = 'New Customer?';
        } else {
            existing.classList.add('hidden');
            newCustomer.classList.remove('hidden');
            toggleButton.textContent = 'Existing Customer?';
        }
    }

    // Track pricing strategy, base, rate, and distance
    let currentStrategy = "standard";
    let currentBase = 0;
    let currentRate = 0;
    let currentDistance = 0;

    function assignFleet(fleetId, driverName, vehicleType, plateNumber, driverContact, base, rate) {
        document.getElementById('fleetId').value = fleetId;
        document.getElementById('fleetDetails').value = `${driverName} - ${vehicleType} (${plateNumber})`;
        document.getElementById('basePrice').value = base;
        document.getElementById('ratePerKm').value = rate;

        currentBase = parseFloat(base);
        currentRate = parseFloat(rate);

        calculatePricing();
    }

    function calculatePricing() {
        currentDistance = parseFloat(document.querySelector('input[name="distance"]').value) || 0;
        const baseFare = currentBase;
        const distanceFare = currentDistance * currentRate;
        let subtotal = baseFare + distanceFare;

        switch (currentStrategy) {
            case 'discount':
                subtotal *= 0.9;
                break;
            case 'peak':
                subtotal *= 1.2;
                break;
            // 'standard' has no effect
        }
        const tax = subtotal * 0.12;
        let total = subtotal + tax;

        document.getElementById('displaySubtotal').textContent = subtotal.toFixed(2);
        document.getElementById('displayTax').textContent = tax.toFixed(2);
        document.getElementById('displayTotal').textContent = total.toFixed(2);
    }

    document.querySelector('input[name="distance"]').addEventListener('input', calculatePricing);
    document.querySelector('select[name="pricingStrategy"]').addEventListener('change', (e) => {
        currentStrategy = e.target.value;
        calculatePricing();
    });
</script>
</body>
</html>
