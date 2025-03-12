package com.megacitycab.service;

import com.megacitycab.observer.Observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class NotificationService {
    private final Map<Class<?>, List<Observer<?>>> observers = new HashMap<>();

    public <T> void addObserver(Class<T> type, Observer<T> observer) {
        observers.computeIfAbsent(type, k -> new ArrayList<>()).add(observer);
    }

    public <T> void removeObserver(Class<T> type, Observer<T> observer) {
        List<Observer<?>> typeObservers = observers.get(type);
        if (typeObservers != null) {
            typeObservers.remove(observer);
        }
    }

    public <T> void notifyObservers(T entity, String eventType) {
        List<Observer<?>> typeObservers = observers.get(entity.getClass());
        if (typeObservers != null) {
            for (Observer<?> observer : typeObservers) {
                ((Observer<T>) observer).notify(entity, eventType);
            }
        }
    }
}