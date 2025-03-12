package com.megacitycab.service;

import com.megacitycab.dao.BillingDAO;
import com.megacitycab.model.Billing;
import com.megacitycab.model.Booking;
import com.megacitycab.model.Fleet;
import com.megacitycab.model.VehicleType;
import com.megacitycab.strategy.PricingStrategy;
import com.megacitycab.strategy.StandardPricing;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class BillingServiceTest {

    private BillingService billingService;
    private BillingDAOStub billingDAOStub;
    private PricingStrategy pricingStrategy;

    @Before
    public void setUp() {
        billingDAOStub = new BillingDAOStub();
        pricingStrategy = new StandardPricing();
        billingService = new BillingService(billingDAOStub, pricingStrategy);
    }

    @Test
    public void testCalculateTotal_StandardPricing() {
        // Arrange
        Booking booking = new Booking();
        booking.setDistance(10.0);

        // Set a valid Fleet for the booking
        Fleet fleet = new Fleet(1, "Driver One", VehicleType.ZIP, "ABC123", "987-654-3210");
        booking.setFleet(fleet); // Ensure Fleet is not null

        // Act
        billingService.calculateTotal(booking);

        // Assert
        Billing savedBilling = billingDAOStub.findAll().get(0); // Retrieve the saved billing
        assertNotNull(savedBilling);
        assertEquals(650.0, savedBilling.getBaseFare(), 0.01); // Verify base fare
        assertEquals(78.0, savedBilling.getTax(), 0.01); // Verify tax (12% of 100)
        assertEquals(728.0, savedBilling.getTotal(), 0.01); // Verify total
        assertEquals("Standard", savedBilling.getPricingType()); // Verify pricing type
    }

    @Test
    public void testGetBillingByBookingId() {
        // Arrange
        Billing testBilling = new Billing();
        testBilling.setBooking(new Booking());
        testBilling.setBaseFare(650.0);
        testBilling.setTax(78.0);
        testBilling.setTotal(728.0);
        billingDAOStub.setBillingToReturn(testBilling);

        // Act
        Billing result = billingService.getBillingByBookingId(1);

        // Assert
        assertNotNull(result);
        assertEquals(650.0, result.getBaseFare(), 0.01);
        assertEquals(78.0, result.getTax(), 0.01);
        assertEquals(728.0, result.getTotal(), 0.01);
    }

    @Test
    public void testGetAllBillings() {
        // Arrange
        Billing testBilling1 = new Billing();
        testBilling1.setBooking(new Booking());
        testBilling1.setBaseFare(650.0);
        testBilling1.setTax(78.0);
        testBilling1.setTotal(728.0);

        Billing testBilling2 = new Billing();
        testBilling2.setBooking(new Booking());
        testBilling2.setBaseFare(1300.0);
        testBilling2.setTax(156.0);
        testBilling2.setTotal(1456.0);

        List<Billing> billings = new ArrayList<>();
        billings.add(testBilling1);
        billings.add(testBilling2);
        billingDAOStub.setAllBillings(billings);

        // Act
        List<Billing> result = billingService.getAllBillings();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(650.0, result.get(0).getBaseFare(), 0.01);
        assertEquals(1300.0, result.get(1).getBaseFare(), 0.01);
    }

    @Test
    public void testSetPricingStrategy() {
        // Arrange
        PricingStrategy newStrategy = new StandardPricing(); // Use a real strategy for simplicity

        // Act
        billingService.setPricingStrategy(newStrategy);

        // Assert
        // No direct assertion, but you can verify behavior in other tests
    }

    // Manual stub for BillingDAO
    static class BillingDAOStub extends BillingDAO {
        private Billing billingToReturn;
        private List<Billing> allBillings = new ArrayList<>();

        void setBillingToReturn(Billing billing) {
            this.billingToReturn = billing;
        }

        void setAllBillings(List<Billing> billings) {
            this.allBillings = billings;
        }

        @Override
        public void save(Billing billing) {
            allBillings.add(billing); // Simulate saving to a list
        }

        @Override
        public Billing findByBookingId(int bookingId) {
            return billingToReturn; // Return the predefined billing
        }

        @Override
        public List<Billing> findAll() {
            return allBillings; // Return the predefined list of billings
        }
    }
}