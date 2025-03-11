package com.megacitycab.service;

import com.megacitycab.dao.BillingDAO;
import com.megacitycab.model.Billing;
import com.megacitycab.model.Booking;
import com.megacitycab.strategy.*;
import java.util.List;

public class BillingService {
    private final BillingDAO billingDao;
    private PricingStrategy pricingStrategy; // Allow dynamic changes
    private static final double TAX_RATE = 0.12; // 12% tax

    public BillingService(BillingDAO billingDao, PricingStrategy pricingStrategy) {
        this.billingDao = billingDao;
        this.pricingStrategy = pricingStrategy;
    }

    public void calculateTotal(Booking booking) {
        // 1. Calculate baseFare using strategy
        double baseFare = pricingStrategy.calculateTotal(booking);

        // 2. Apply tax
        double tax = baseFare * TAX_RATE;

        // 4. Final total
        double total = baseFare + tax;

        // 5. Create and save billing
        Billing billing = new Billing();
        billing.setBooking(booking); // Ensure the booking is set
        billing.setBaseFare(baseFare);
        billing.setTax(tax);
        billing.setTotal(total);
        // Set pricing type based on the strategy
        if (pricingStrategy instanceof PeakPricing) {
            billing.setPricingType("Peak");
        } else if (pricingStrategy instanceof DiscountPricing) {
            billing.setPricingType("Discount");
        } else {
            billing.setPricingType("Standard"); // Default
        }


        // Save billing to the database
        billingDao.save(billing);
    }

    // Get billing details for a given booking
    public Billing getBillingByBookingId(int bookingId) {
        return billingDao.findByBookingId(bookingId);
    }

    // Retrieve all billing records
    public List<Billing> getAllBillings() {
        return billingDao.findAll();
    }

    // Dynamically update the pricing strategy
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
}
