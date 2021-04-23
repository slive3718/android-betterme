package com.example.betterme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.betterme.model.UserInfo;
import com.google.gson.Gson;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String USERINFO = "USERINFO";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(UserInfo userInfo) {
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString(USERINFO, json);
        editor.putBoolean(LOGIN, true);
        editor.commit();
    }

    public UserInfo getCurrentUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USERINFO, "");
        if (!TextUtils.isEmpty(json))
            return gson.fromJson(json, UserInfo.class);
        return null;
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
            Intent i = new Intent(context, Login.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        AppApplication.getInstance().setCurrentUser(null);
    }
}
