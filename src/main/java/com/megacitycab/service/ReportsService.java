package com.megacitycab.service;

import com.megacitycab.dao.ReportsDAO;
import com.megacitycab.dto.CustomerRevenueDTO;
import com.megacitycab.dto.DriverRevenueDTO;

import java.util.Map;

public class ReportsService {
    private ReportsDAO reportsDao;

    public ReportsService() {
        this.reportsDao = new ReportsDAO();
    }

    // Add a setter for reportsDao
    public void setReportsDao(ReportsDAO reportsDao) {
        this.reportsDao = reportsDao;
    }

    public int getTotalBookings() {
        return reportsDao.getTotalBookings();
    }

    public double getTotalRevenue() {
        return reportsDao.getTotalRevenue();
    }

    public Map<String, Integer> getBookingCountByPricingStrategy() {
        return reportsDao.getBookingCountByPricingStrategy();
    }

    public Map<String, Integer> getBookingCountByVehicleType() {
        return reportsDao.getBookingCountByVehicleType();
    }

    public int getDriverCount() {
        return reportsDao.getDriverCount();
    }

    public Map<String, Integer> getBookingCountByDriver() {
        return reportsDao.getBookingCountByDriver();
    }

    public int getCustomerCount() {
        return reportsDao.getCustomerCount();
    }

    public CustomerRevenueDTO getHighestRevenueCustomer() {
        return reportsDao.getHighestRevenueCustomer();
    }

    public DriverRevenueDTO getHighestRevenueDriver() {
        return reportsDao.getHighestRevenueDriver();
    }

    public Map<String, Integer> getUserCountByRole() {
        return reportsDao.getUserCountByRole();
    }
}