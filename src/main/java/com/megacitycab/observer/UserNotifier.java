package com.megacitycab.observer;

import com.megacitycab.model.User;

public class UserNotifier implements Observer<User> {
    @Override
    public void notify(User user, String message) {
        System.out.println("[SERVER LOG] Notification sent to user: " + message + " Username: " + user.getUsername());
    }
}