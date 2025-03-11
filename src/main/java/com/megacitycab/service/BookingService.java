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
import com.megacitycab.strategy.PricingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingService {
    private final BookingDAO bookingDao;
    private final CustomerDAO customerDao;
    private final FleetDAO fleetDao;
    private BillingService billingService; // Allow dynamic changes
    private final NotificationService notificationService;

    // Constructor Injection (DIP)
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

        // Update billing service with selected strategy
        billingService.setPricingStrategy(pricingStrategy);

        // Calculate Billing
        billingService.calculateTotal(booking);

        // Notify Observers (Driver/Customer)
        notificationService.notifyObservers(booking);
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
}
