package com.megacitycab.observer;

import com.megacitycab.model.User;

public class UserNotifier implements Observer<User> {
    @Override
    public void notify(User user, String eventType) {
        switch (eventType) {
            case "CREATED":
                System.out.println("[SERVER LOG] Notification sent to user: A new user has been created. Username: " + user.getUsername());
                break;
            case "UPDATED":
                System.out.println("[SERVER LOG] Notification sent to user: User details have been updated. Username: " + user.getUsername());
                break;
            case "DELETED":
                System.out.println("[SERVER LOG] Notification sent to user: User has been deleted. Username: " + user.getUsername());
                break;
            default:
                System.out.println("[SERVER LOG] Unknown event type for user: " + eventType);
                break;
        }
    }
}