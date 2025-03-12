package com.megacitycab.observer;

import com.megacitycab.model.Fleet;

public class FleetNotifier implements Observer<Fleet> {
    @Override
    public void notify(Fleet fleet, String message) {
        System.out.println("[SERVER LOG] Notification sent to fleet: " + message + " Driver: " + fleet.getDriverName());
    }
}