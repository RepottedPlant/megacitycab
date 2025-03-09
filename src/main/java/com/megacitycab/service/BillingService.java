package com.megacitycab.service;

import com.megacitycab.dao.BillingDAO;
import com.megacitycab.model.Billing;
import com.megacitycab.model.Booking;
import com.megacitycab.strategy.PricingStrategy;

public class BillingService {
    private final BillingDAO billingDao;
    private PricingStrategy pricingStrategy; // Make it non-final to allow dynamic changes
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

        // 3. Apply discount (if any, e.g., coupon codes)
        double discount = 0; // Can be fetched from a DiscountService

        // 4. Final total
        double total = baseFare + tax - discount;

        // 5. Save billing
        Billing billing = new Billing();
        billing.setBooking(booking);
        billing.setBaseFare(baseFare);
        billing.setTax(tax);
        billing.setDiscount(discount);
        billing.setTotal(total);
        billingDao.save(billing);
    }

    // Add a method to dynamically set the pricing strategy
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
}