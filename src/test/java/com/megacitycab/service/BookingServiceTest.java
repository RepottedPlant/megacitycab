package com.megacitycab.service;

import com.megacitycab.dao.BillingDAO;
import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.dao.FleetDAO;
import com.megacitycab.dto.BookingBillingDTO;
import com.megacitycab.dto.BookingDTO;
import com.megacitycab.model.Booking;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import com.megacitycab.model.Billing;
import com.megacitycab.strategy.PricingStrategy;
import com.megacitycab.strategy.StandardPricing;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceTest {

    private BookingService bookingService;
    private BookingDAOStub bookingDAOStub;
    private CustomerDAOStub customerDAOStub;
    private FleetDAOStub fleetDAOStub;
    private BillingServiceStub billingServiceStub;
    private NotificationServiceStub notificationServiceStub;

    // Manual stubs for DAOs and services
    static class BookingDAOStub extends BookingDAO {
        private List<Booking> bookings = new ArrayList<>();
        private Booking bookingToReturn;

        void setBookingToReturn(Booking booking) {
            this.bookingToReturn = booking;
        }

        void setAllBookings(List<Booking> bookings) {
            this.bookings = bookings;
        }

        @Override
        public void save(Booking booking) {
            bookings.add(booking);
        }

        @Override
        public Booking findById(int id) {
            return bookingToReturn;
        }

        @Override
        public List<Booking> findAll() {
            return bookings;
        }

        @Override
        public List<Booking> searchAllColumns(String searchQuery) {
            return bookings;
        }
    }

    static class CustomerDAOStub extends CustomerDAO {
        private Customer customerToReturn;

        void setCustomerToReturn(Customer customer) {
            this.customerToReturn = customer;
        }

        @Override
        public Customer findById(int id) {
            return customerToReturn;
        }
    }

    static class FleetDAOStub extends FleetDAO {
        private Fleet fleetToReturn;

        void setFleetToReturn(Fleet fleet) {
            this.fleetToReturn = fleet;
        }

        @Override
        public Fleet findById(int id) {
            return fleetToReturn;
        }

        @Override
        public void updateFleetInfo(Booking booking) {
            // Simulate updating fleet info
        }
    }

    static class BillingServiceStub extends BillingService {
        private Billing billingToReturn;

        public BillingServiceStub(BillingDAO billingDao, PricingStrategy pricingStrategy) {
            super(billingDao, pricingStrategy);
        }

        void setBillingToReturn(Billing billing) {
            this.billingToReturn = billing;
        }

        @Override
        public Billing getBillingByBookingId(int bookingId) {
            return billingToReturn;
        }
    }

    static class NotificationServiceStub extends NotificationService {
        @Override
        public void notifyObservers(Object entity, String eventType) {
            // Simulate notification
        }
    }

    @Before
    public void setUp() {
        bookingDAOStub = new BookingDAOStub();
        customerDAOStub = new CustomerDAOStub();
        fleetDAOStub = new FleetDAOStub();
        billingServiceStub = new BillingServiceStub(new BillingServiceTest.BillingDAOStub(), new StandardPricing());
        notificationServiceStub = new NotificationServiceStub();

        bookingService = new BookingService(
                bookingDAOStub,
                customerDAOStub,
                fleetDAOStub,
                billingServiceStub,
                notificationServiceStub
        );
    }

    @Test
    public void testCreateBooking() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerId(1);
        bookingDTO.setFleetId(1);
        bookingDTO.setPickup("Location A");
        bookingDTO.setDestination("Location B");
        bookingDTO.setDistance(10.0);

        customerDAOStub.setCustomerToReturn(customer);
        fleetDAOStub.setFleetToReturn(fleet);

        // Act
        bookingService.createBooking(bookingDTO, new StandardPricing());

        // Assert
        Booking savedBooking = bookingDAOStub.findAll().get(0);
        assertNotNull(savedBooking);
        assertEquals("Location A", savedBooking.getPickup());
        assertEquals("Location B", savedBooking.getDestination());
        assertEquals(10.0, savedBooking.getDistance(), 0.01);
        assertEquals(customer, savedBooking.getCustomer());
        assertEquals(fleet, savedBooking.getFleet());
    }

    @Test
    public void testGetAllBookings() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setPickup("Location A");
        booking1.setDestination("Location B");
        booking1.setCustomer(customer);
        booking1.setFleet(fleet);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setPickup("Location C");
        booking2.setDestination("Location D");
        booking2.setCustomer(customer);
        booking2.setFleet(fleet);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        bookingDAOStub.setAllBookings(bookings);

        // Act
        List<Booking> result = bookingService.getAllBookings();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Location A", result.get(0).getPickup());
        assertEquals("Location C", result.get(1).getPickup());
    }

    @Test
    public void testSearchBookings() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setPickup("Location A");
        booking1.setCustomer(customer);
        booking1.setFleet(fleet);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setPickup("Location B");
        booking2.setCustomer(customer);
        booking2.setFleet(fleet);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        bookingDAOStub.setAllBookings(bookings);

        // Act
        List<Booking> result = bookingService.searchBookings("Location A");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllBookingBillingDetails() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setPickup("Location A");
        booking.setCustomer(customer);
        booking.setFleet(fleet);

        Billing billing = new Billing();
        billing.setBaseFare(100.0);
        billing.setTax(12.0);
        billing.setTotal(112.0);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookingDAOStub.setAllBookings(bookings);
        billingServiceStub.setBillingToReturn(billing);

        // Act
        List<BookingBillingDTO> result = bookingService.getAllBookingBillingDetails();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Location A", result.get(0).getBooking().getPickup());
        assertEquals(112.0, result.get(0).getBilling().getTotal(), 0.01);
    }

    @Test
    public void testAssignFleetToBooking() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123456789V", "123-456-7890");
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setCustomer(customer);

        bookingDAOStub.setBookingToReturn(booking);
        fleetDAOStub.setFleetToReturn(fleet);

        // Act
        bookingService.assignFleetToBooking(1, 1);

        // Assert
        // No direct assertion, but you can verify behavior in other tests
    }
}