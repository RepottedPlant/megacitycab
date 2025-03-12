package com.megacitycab.service;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.dao.FleetDAO;
import com.megacitycab.dto.BookingBillingDTO;
import com.megacitycab.dto.BookingDTO;
import com.megacitycab.model.Booking;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.Billing;
import com.megacitycab.observer.CustomerNotifier;
import com.megacitycab.observer.FleetNotifier;
import com.megacitycab.strategy.PricingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final BookingDAO bookingDao;
    private final CustomerDAO customerDao;
    private final FleetDAO fleetDao;
    private BillingService billingService;
    private final NotificationService notificationService;

    public BookingService(
            BookingDAO bookingDao,
            CustomerDAO customerDao,
            FleetDAO fleetDao,
            BillingService billingService,
            NotificationService notificationService
    ) {
        this.bookingDao = bookingDao;
        this.customerDao = customerDao;
        this.fleetDao = fleetDao;
        this.billingService = billingService;
        this.notificationService = notificationService;

        // Register observers for Fleet and Customer
        notificationService.addObserver(Fleet.class, new FleetNotifier());
        notificationService.addObserver(Customer.class, new CustomerNotifier());
    }

    public void createBooking(BookingDTO bookingDTO, PricingStrategy pricingStrategy) {
        // Convert DTO to Entity
        Customer customer = customerDao.findById(bookingDTO.getCustomerId());
        Fleet fleet = fleetDao.findById(bookingDTO.getFleetId());

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setFleet(fleet);
        booking.setPickup(bookingDTO.getPickup());
        booking.setDestination(bookingDTO.getDestination());
        booking.setDistance(bookingDTO.getDistance());
        booking.setBookingDate(LocalDateTime.now());

        // Save to Database
        bookingDao.save(booking);
        System.out.println("Booking saved with ID: " + booking.getId());

        // Calculate billing with the selected strategy
        billingService.setPricingStrategy(pricingStrategy);
        billingService.calculateTotal(booking);

        // Notify observers for both Fleet and Customer
        notificationService.notifyObservers(fleet, "BOOKING_CREATED");
        notificationService.notifyObservers(customer, "BOOKING_CREATED");
    }

    // Setter for billingService (for dynamic updates)
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }

    public BillingService getBillingService() {
        return this.billingService;
    }

    public List<Booking> getAllBookings() {
        return bookingDao.findAll();
    }

    public List<Booking> searchBookings(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            // If search query is empty, return all bookings
            return bookingDao.findAll();
        } else {
            // Otherwise, search across all columns
            return bookingDao.searchAllColumns(searchQuery);
        }
    }

    // New method to combine Booking and Billing details
    public List<BookingBillingDTO> getAllBookingBillingDetails() {
        List<Booking> bookings = bookingDao.findAll();
        List<BookingBillingDTO> combinedList = new ArrayList<>();
        for (Booking b : bookings) {
            Billing billing = billingService.getBillingByBookingId(b.getId());
            combinedList.add(new BookingBillingDTO(b, billing));
        }
        return combinedList;
    }

    public List<BookingBillingDTO> searchBookingBillingDetails(String searchQuery) {
        List<Booking> bookings = searchBookings(searchQuery);
        List<BookingBillingDTO> combinedList = new ArrayList<>();
        for (Booking booking : bookings) {
            Billing billing = billingService.getBillingByBookingId(booking.getId());
            combinedList.add(new BookingBillingDTO(booking, billing));
        }
        return combinedList;
    }

    public void assignFleetToBooking(int bookingId, int fleetId) {
        // Fetch the booking and fleet from the database
        Booking booking = bookingDao.findById(bookingId);
        Fleet fleet = fleetDao.findById(fleetId);

        if (booking != null && fleet != null) {
            // Assign the fleet to the booking
            booking.setFleet(fleet);
            fleetDao.updateFleetInfo(booking); // Update the booking in the database
            System.out.println("[SERVER LOG] Fleet assigned to booking successfully.");
        } else {
            System.out.println("[SERVER LOG] Booking or fleet not found.");
        }
    }
}
