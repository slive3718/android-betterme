package com.example.betterme;

import android.app.Application;

import com.example.betterme.model.UserInfo;

public class AppApplication extends Application {
    private static AppApplication instance;

    public static AppApplication getInstance() {
        return instance;
    }

    private UserInfo currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public UserInfo getCurrentUser() {
        if (currentUser == null) {
            currentUser = new SessionManager(this).getCurrentUser();
        }
        return currentUser;
    }

    public void setCurrentUser(UserInfo currentUser) {
        this.currentUser = currentUser;
    }
}
