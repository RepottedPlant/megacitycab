<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>System Reports</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --background-color: #f8f9fa;
            --text-color: #2c3e50;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: var(--background-color);
            color: var(--text-color);
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
            margin: 0;
            font-size: 2.5rem;
        }

        h2 {
            color: var(--secondary-color);
            font-size: 1.5rem;
            margin-bottom: 15px;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: var(--secondary-color);
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .btn:hover {
            background-color: #2980b9;
        }

        .report-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
            margin-top: 20px;
        }

        .report-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 15px rgba(0,0,0,0.1);
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

        .highlight {
            color: var(--secondary-color);
            font-weight: 600;
        }

        @media (max-width: 768px) {
            .report-grid {
                grid-template-columns: 1fr;
            }

            h1 {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Reports Dashboard</h1>
        <a href="${pageContext.request.contextPath}/protected/dashboard" class="btn">Back to Dashboard</a>
    </div>

    <div class="report-grid">
        <!-- General Metrics Card -->
        <div class="report-card">
            <h2>📊 General Metrics</h2>
            <table>
                <tr>
                    <th>Metric</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>Total Bookings</td>
                    <td class="highlight">${totalBookings}</td>
                </tr>
                <tr>
                    <td>Total Revenue</td>
                    <td class="highlight">
                        LKR
                        <fmt:formatNumber
                                value="${totalRevenue}"
                                minFractionDigits="2"
                                maxFractionDigits="2" />
                    </td>
                </tr>
                <tr>
                    <td>Total Customers</td>
                    <td>${customerCount}</td>
                </tr>
                <tr>
                    <td>Total Drivers</td>
                    <td>${driverCount}</td>
                </tr>
            </table>
        </div>

        <!-- Pricing Strategy Card -->
        <div class="report-card">
            <h2>💰 Pricing Strategy</h2>
            <table>
                <tr>
                    <th>Strategy</th>
                    <th>Bookings</th>
                </tr>
                <c:forEach var="entry" items="${bookingsByPricing}">
                    <tr>
                        <td>${entry.key}</td>
                        <td>${entry.value}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <!-- Vehicle Type Card -->
        <div class="report-card">
            <h2>🚗 Vehicle Types</h2>
            <table>
                <tr>
                    <th>Vehicle</th>
                    <th>Bookings</th>
                </tr>
                <c:forEach var="entry" items="${bookingsByVehicle}">
                    <tr>
                        <td>${entry.key}</td>
                        <td>${entry.value}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <!-- Driver Performance Card -->
        <div class="report-card">
            <h2>🏆 Top Performers</h2>
            <table>
                <tr>
                    <th>Driver</th>
                    <th>Bookings</th>
                </tr>
                <c:forEach var="entry" items="${bookingsByDriver}">
                    <tr>
                        <td>${entry.key}</td>
                        <td>${entry.value}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <!-- User Roles Card -->
        <div class="report-card">
            <h2>👥 User Roles</h2>
            <table>
                <tr>
                    <th>Role</th>
                    <th>Count</th>
                </tr>
                <c:forEach var="entry" items="${userCountByRole}">
                    <tr>
                        <td>${entry.key}</td>
                        <td>${entry.value}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <!-- Revenue Leaders Card -->
        <div class="report-card">
            <h2>⭐ Revenue Leaders</h2>
            <div style="display: grid; gap: 15px;">
                <div>
                    <h3>Top Customer</h3>
                    <p>${highestRevenueCustomer.customerName}<br>
                        <small>Revenue: LKR ${highestRevenueCustomer.revenue}</small></p>
                </div>
                <div>
                    <h3>Top Driver</h3>
                    <p>${highestRevenueDriver.driverName}<br>
                        <small>Revenue: LKR ${highestRevenueDriver.revenue}</small></p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>