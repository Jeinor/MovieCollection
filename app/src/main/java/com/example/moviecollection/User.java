package com.example.moviecollection;

// Класс-модель пользователя для работы с базой данных
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
