package com.megacitycab.service;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.dao.FleetDAO;
import com.megacitycab.model.Booking;
import com.megacitycab.dto.BookingDTO;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Fleet;
import com.megacitycab.strategy.PricingStrategy;

import java.time.LocalDateTime;

public class BookingService {
    private final BookingDAO bookingDao;
    private final CustomerDAO customerDao;
    private final FleetDAO fleetDao;
    private BillingService billingService; // Make it non-final to allow dynamic changes
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
        // 1. Convert DTO to Entity
        Customer customer = customerDao.findById(bookingDTO.getCustomerId());
        Fleet fleet = fleetDao.findById(bookingDTO.getFleetId());

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setFleet(fleet);
        booking.setPickup(bookingDTO.getPickup());
        booking.setDestination(bookingDTO.getDestination());
        booking.setDistance(bookingDTO.getDistance()); // Ensure distance is set
        booking.setBookingDate(LocalDateTime.now());

        // 2. Save to Database
        bookingDao.save(booking);

        // 3. Update billing service with the selected pricing strategy
        billingService.setPricingStrategy(pricingStrategy);

        // 4. Calculate Billing
        billingService.calculateTotal(booking);

        // 5. Notify Observers (Driver/Customer)
        notificationService.notifyObservers(booking);
    }

    // Add a method to dynamically set the billing service
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
}