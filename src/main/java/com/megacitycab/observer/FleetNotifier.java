package com.megacitycab.observer;

import com.megacitycab.model.Fleet;

public class FleetNotifier implements Observer<Fleet> {
    @Override
    public void notify(Fleet fleet, String eventType) {
        switch (eventType) {
            case "CREATE":
                System.out.println("[SERVER LOG] Notification sent to fleet: A new fleet has been created. Driver: " + fleet.getDriverName());
                break;
            case "UPDATE":
                System.out.println("[SERVER LOG] Notification sent to fleet: Fleet details have been updated. Driver: " + fleet.getDriverName());
                break;
            case "DELETE":
                System.out.println("[SERVER LOG] Notification sent to fleet: Fleet has been deleted. Driver: " + fleet.getDriverName());
                break;
            default:
                System.out.println("[SERVER LOG] Unknown event type for fleet: " + eventType);
                break;
        }
    }
}