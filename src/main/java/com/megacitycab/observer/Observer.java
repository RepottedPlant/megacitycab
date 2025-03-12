package com.megacitycab.observer;

public interface Observer<T> {
    void notify(T entity, String eventType);
}
