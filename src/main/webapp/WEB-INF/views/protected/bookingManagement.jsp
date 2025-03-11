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
                    <td>${dto.billing.total}</td>
                    <td>${dto.booking.bookingDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>


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
                            <button type="button" onclick="assignFleet(${fleet.id}, '${fleet.driverName}', '${fleet.vehicleType}', '${fleet.plateNumber}', '${fleet.driverContact}', '${fleet.vehicleType.basePrice}',
                                    '${fleet.vehicleType.ratePerKm}')">Assign</button>
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

        <!-- Pricing Strategy -->
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
            <button type="submit">Create Booking</button>
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

    // Track pricing strategy, base, rate, and distance
    let currentStrategy = "standard";
    let currentBase = 0;
    let currentRate = 0;
    let currentDistance = 0;

    function assignFleet(fleetId, driverName, vehicleType, plateNumber, driverContact, base, rate) {
        // Update hidden fields
        document.getElementById('fleetId').value = fleetId;
        document.getElementById('fleetDetails').value = `${driverName} - ${vehicleType} (${plateNumber})`;
        document.getElementById('basePrice').value = base;
        document.getElementById('ratePerKm').value = rate;

        // Update JavaScript variables
        currentBase = parseFloat(base);
        currentRate = parseFloat(rate);

        // Recalculate pricing
        calculatePricing();
    }

    function calculatePricing() {
        // Get distance from input
        currentDistance = parseFloat(document.querySelector('input[name="distance"]').value) || 0;

        // Calculate base and distance fare
        const baseFare = currentBase;
        const distanceFare = currentDistance * currentRate;
        let subtotal = baseFare + distanceFare;



        // Apply pricing strategy
        switch (currentStrategy) {
            case 'discount':
                subtotal *= 0.9; // 10% discount
                break;
            case 'peak':
                subtotal *= 1.2; // 20% peak pricing
                break;
            // 'standard' has no effect
        }
        // Apply tax (12%)
        const tax = subtotal * 0.12;
        let total = subtotal + tax;

        // Update display
        document.getElementById('displaySubtotal').textContent = subtotal.toFixed(2);
        document.getElementById('displayTax').textContent = tax.toFixed(2);
        document.getElementById('displayTotal').textContent = total.toFixed(2);
    }

    // Attach event listeners to inputs
    document.querySelector('input[name="distance"]').addEventListener('input', calculatePricing);
    document.querySelector('select[name="pricingStrategy"]').addEventListener('change', (e) => {
        currentStrategy = e.target.value;
        calculatePricing();
    });
</script>
</body>
</html>
