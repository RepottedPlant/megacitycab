package com.megacitycab.service;

import com.megacitycab.model.Booking;
import com.megacitycab.observer.BookingObserver;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<BookingObserver> observers = new ArrayList<>();

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BookingObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Booking booking) {
        observers.forEach(observer -> observer.notify(booking));
    }
}