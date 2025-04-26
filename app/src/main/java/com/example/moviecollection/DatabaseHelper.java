package com.example.moviecollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Имя БД и версия
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2; // Увеличили из-за добавления новой таблицы

    // Таблица пользователей
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Таблица фильмов пользователя
    private static final String TABLE_USER_MOVIES = "user_movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MOVIE_ID = "movieId";
    private static final String COLUMN_STATUS = "status"; // "viewed" или "plan"

    // SQL-запрос создания таблицы пользователей
    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    // SQL-запрос создания таблицы user_movies
    private static final String TABLE_CREATE_USER_MOVIES =
            "CREATE TABLE " + TABLE_USER_MOVIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Создание БД
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_USER_MOVIES);
    }

    // Обновление БД при изменении версии
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_MOVIES);
        onCreate(db);
    }

    // Регистрация пользователя
    public boolean registerUser(String username, String password) {
        if (userExists(username)) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Проверка существования пользователя
    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Проверка логина и пароля
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean isValid = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return isValid;
    }

    // Добавление фильма для пользователя (просмотрено или план)
    public boolean addMovieForUser(String username, int movieId, String status) {
        if (movieExists(username, movieId, status)) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_MOVIE_ID, movieId);
        values.put(COLUMN_STATUS, status);

        long result = db.insert(TABLE_USER_MOVIES, null, values);
        db.close();
        return result != -1;
    }

    // Проверка, добавлен ли фильм пользователем
    public boolean movieExists(String username, int movieId, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_MOVIES,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_MOVIE_ID + "=? AND " + COLUMN_STATUS + "=?",
                new String[]{username, String.valueOf(movieId), status},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Получение всех фильмов пользователя по статусу
    public List<Integer> getUserMovies(String username, String status) {
        List<Integer> movieIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER_MOVIES,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_STATUS + "=?",
                new String[]{username, status},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                movieIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movieIds;
    }

    // Удаление фильма из списка
    public void removeMovieForUser(String username, int movieId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_MOVIES,
                COLUMN_USERNAME + "=? AND " + COLUMN_MOVIE_ID + "=? AND " + COLUMN_STATUS + "=?",
                new String[]{username, String.valueOf(movieId), status});
        db.close();
    }

    // получение ID для просмотренных и планируемых фильмов
    public List<Integer> getMovieIdsByStatus(String username, String status) {
        List<Integer> ids = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(
                TABLE_USER_MOVIES,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_STATUS + "=?",
                new String[]{username, status},
                null, null, null)) {

            while (c.moveToNext()) {
                ids.add(c.getInt(c.getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
            }
        }
        return ids;
    }
}
