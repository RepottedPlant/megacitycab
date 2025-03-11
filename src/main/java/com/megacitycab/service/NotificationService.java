package com.megacitycab.service;

import com.megacitycab.observer.Observer;
import java.util.ArrayList;
import java.util.List;

public class NotificationService<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    // Add an observer
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    // Remove an observer
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    // Notify all observers
    public void notifyObservers(T entity) {
        for (Observer<T> observer : observers) {
            observer.notify(entity);
        }
    }
}