package com.megacitycab.service;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AuthServiceTest {

    private AuthService authService;
    private UserDAOStub userDAOStub;

    @Before
    public void setUp() {
        userDAOStub = new UserDAOStub();
        authService = new AuthService(userDAOStub); // Inject the stub
    }

    @Test
    public void testLoginSuccess_AdminRole() {
        // Arrange
        User testUser = new User(1, "admin", "secure123", "admin");
        userDAOStub.setUserToReturn(testUser);

        // Act
        User result = authService.login("admin", "secure123");

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals("admin", result.getUsername());
        Assert.assertEquals("admin", result.getRole());
    }

    @Test
    public void testLoginFailure_WrongPassword() {
        // Arrange
        User testUser = new User(1, "admin", "secure123", "admin");
        userDAOStub.setUserToReturn(testUser);

        // Act
        User result = authService.login("admin", "wrongpassword");

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testLoginFailure_UserNotFound() {
        // Arrange
        userDAOStub.setUserToReturn(null);

        // Act
        User result = authService.login("unknown", "password");

        // Assert
        Assert.assertNull(result);
    }

    // Manual stub for UserDAO
    static class UserDAOStub extends UserDAO {
        private User userToReturn;

        void setUserToReturn(User user) {
            this.userToReturn = user;
        }

        @Override
        public User findByUsername(String username) {
            return userToReturn;
        }

        @Override
        public void save(User user) {
        }

        @Override
        public boolean update(User user) {
            return false;
        }

        @Override
        public boolean delete(int id) {
            return false;
        }

        @Override
        public List<User> findAll() {
            return null;
        }

        @Override
        public List<User> findByUsernameOrRole(String searchQuery) {
            return null;
        }

        @Override
        public User findById(int id) {
            return null;
        }
    }
}