<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management</title>
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
        .form-section h2 {
            color: var(--secondary-color);
            margin-bottom: 15px;
        }
        .form-section input[type="text"],
        .form-section input[type="password"],
        .form-section select {
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
        <h1>User Management</h1>
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

    <!-- Search Users Section -->
    <div class="form-section">
        <h2>Search Users</h2>
        <form action="${pageContext.request.contextPath}/protected/userManagement" method="get">
            <input type="hidden" name="action" value="searchUsers">
            <input type="text" name="searchQuery" placeholder="Enter Username or Role">
            <button type="submit" class="btn">Search Users</button>
        </form>
    </div>

    <!-- Existing Users Table -->
    <c:if test="${not empty users}">
        <div class="form-section">
            <h2>Existing Users</h2>
            <table>
                <thead>
                <tr>
                    <th>User ID</th>
                    <th>Username</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.role}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/protected/userManagement?action=edit&id=${user.id}" class="btn" style="padding:5px 10px; margin-right:5px;">Edit</a>
                            <a href="${pageContext.request.contextPath}/protected/userManagement?action=delete&id=${user.id}" onclick="return confirm('Are you sure you want to delete this user?')" class="btn" style="padding:5px 10px;">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <!-- User Form -->
    <div class="form-section">
        <h2>${empty param.id ? 'Create New' : 'Edit'} User</h2>
        <form action="${pageContext.request.contextPath}/protected/userManagement" method="post">
            <input type="hidden" name="userId" value="${user.id != null ? user.id : 0}">
            <input type="hidden" name="action" value="createOrUpdateUser">

            <div>
                <label>Username:</label>
                <input type="text" name="username" value="${requestScope.user.username}" required>
            </div>
            <div>
                <label>Password:</label>
                <input type="password" name="password" value="${requestScope.user.password}" required>
            </div>
            <div>
                <label>Role:</label>
                <select name="role" required>
                    <option value="">-- Select User Role --</option>
                    <option value="admin" ${requestScope.user.role == 'admin' ? 'selected' : ''}>Admin</option>
                    <option value="employee" ${requestScope.user.role == 'employee' ? 'selected' : ''}>Employee</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit">${empty param.id ? 'Create User' : 'Update User'}</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
