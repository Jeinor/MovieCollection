package com.example.moviecollection;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERNAME = "username";

    private static SessionManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    // Приватный конструктор для singleton
    private SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Получение экземпляра SessionManager
    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    // Сохранение логина пользователя
    public void login(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // Получение текущего логина
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    // Удаление данных (logout)
    public void logout() {
        editor.remove(KEY_USERNAME);
        editor.apply();
    }

    // Проверка: вошёл ли пользователь
    public boolean isLoggedIn() {
        return getUsername() != null;
    }
}