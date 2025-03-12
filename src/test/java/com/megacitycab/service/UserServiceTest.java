package com.megacitycab.service;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.model.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    private UserService userService;
    private UserDAOStub userDAOStub;
    private NotificationServiceStub notificationServiceStub;

    // Manual stub for UserDAO
    static class UserDAOStub extends UserDAO {
        private List<User> users = new ArrayList<>();
        private User userToReturn;

        void setUserToReturn(User user) {
            this.userToReturn = user;
        }

        void setAllUsers(List<User> users) {
            this.users = users;
        }

        @Override
        public void save(User user) {
            user.setId(users.size() + 1); // Simulate auto-generated ID
            users.add(user); // Simulate saving to a list
        }

        @Override
        public boolean update(User user) {
            // Simulate updating a user
            return true;
        }

        @Override
        public boolean delete(int id) {
            // Simulate deleting a user
            return true;
        }

        @Override
        public User findById(int id) {
            return userToReturn; // Return the predefined user
        }

        @Override
        public List<User> findAll() {
            return users; // Return the predefined list of users
        }

        @Override
        public List<User> findByUsernameOrRole(String searchQuery) {
            return users; // Simulate search functionality
        }
    }

    // Manual stub for NotificationService
    static class NotificationServiceStub extends NotificationService {
        @Override
        public void notifyObservers(Object entity, String eventType) {
            // Simulate notification
        }
    }

    @Before
    public void setUp() {
        userDAOStub = new UserDAOStub();
        notificationServiceStub = new NotificationServiceStub();
        userService = new UserService(userDAOStub, notificationServiceStub);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        User user = new User(1, "admin", "password123", "admin");

        // Act
        User savedUser = userService.createUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(1, savedUser.getId()); // Verify auto-generated ID
        assertEquals("admin", savedUser.getUsername());
        assertEquals("password123", savedUser.getPassword());
        assertEquals("admin", savedUser.getRole());
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User user = new User(1, "admin", "password123", "admin");

        // Act
        boolean isUpdated = userService.updateUser(user);

        // Assert
        assertTrue(isUpdated);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        int userId = 1;

        // Act
        boolean isDeleted = userService.deleteUser(userId);

        // Assert
        assertTrue(isDeleted);
    }

    @Test
    public void testFindUserById() {
        // Arrange
        User user = new User(1, "admin", "password123", "admin");
        userDAOStub.setUserToReturn(user);

        // Act
        User foundUser = userService.findUserById(1);

        // Assert
        assertNotNull(foundUser);
        assertEquals("admin", foundUser.getUsername());
        assertEquals("password123", foundUser.getPassword());
        assertEquals("admin", foundUser.getRole());
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User(1, "admin", "password123", "admin");
        User user2 = new User(2, "employee", "password456", "employee");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        userDAOStub.setAllUsers(users);

        // Act
        List<User> result = userService.findAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("admin", result.get(0).getUsername());
        assertEquals("employee", result.get(1).getUsername());
    }

    @Test
    public void testSearchUsers() {
        // Arrange
        User user1 = new User(1, "admin", "password123", "admin");
        User user2 = new User(2, "employee", "password456", "employee");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        userDAOStub.setAllUsers(users);

        // Act
        List<User> result = userService.searchUsers("admin");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Simulated search returns all users
        assertEquals("admin", result.get(0).getUsername());
        assertEquals("employee", result.get(1).getUsername());
    }
}