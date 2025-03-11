<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Management</title>
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
<h1>Customer Management</h1>

<!-- Display Success/Error Messages -->
<c:if test="${not empty success}">
    <div class="success-message">${success}</div>
</c:if>
<c:if test="${not empty error}">
    <div class="error-message">${error}</div>
</c:if>
<!-- Search Customers Section -->
<div class="form-section search-section">
    <h2>Search Customers</h2>
    <form action="${pageContext.request.contextPath}/protected/customerManagement" method="get">
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
                <th>Actions</th>
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
                    <td>
                        <a href="${pageContext.request.contextPath}/protected/customerManagement?action=edit&id=${customer.id}">Edit</a>
                        <a href="${pageContext.request.contextPath}/protected/customerManagement?action=delete&id=${customer.id}" onclick="return confirm('Are you sure you want to delete this customer?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<!-- Customer Form -->
<div class="form-section">
    <h2>${empty param.id ? 'Create New' : 'Edit'} Customer</h2>
    <form action="${pageContext.request.contextPath}/protected/customerManagement" method="post">
        <input type="hidden" name="customerId" value="${customer.id}">
        <input type="hidden" name="action" value="createOrUpdateCustomer">

        <!-- Customer Details -->
        <div class="customer-details">
            <label>Full Name:</label>
            <input type="text" name="name" value="${customer.name}" required>

            <label>Phone Number:</label>
            <input type="text" name="phone" value="${customer.phone}" required>

            <label>NIC:</label>
            <input type="text" name="nic" value="${customer.nic}" required>

            <label>Address:</label>
            <input type="text" name="address" value="${customer.address}" required>
        </div>

        <!-- Form Actions -->
        <div class="form-actions">
            <button type="submit">${empty param.id ? 'Create Customer' : 'Update Customer'}</button>
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
</script>
</body>
</html>