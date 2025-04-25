package com.example.moviecollection;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Модель для детальной информации о фильме.
 * Используется в MovieDetailFragment при запросе по ID.
 */
public class MovieDetail {

    private String name; // Название фильма

    @SerializedName("alternativeName")
    private String alternativeName; // Альтернативное (оригинальное) название

    private String description; // Описание фильма
    private int year;           // Год выпуска
    private Poster poster;      // Объект постера
    private Rating rating;      // Оценка
    private List<Genre> genres; // Список жанров

    public String getName() { return name; }
    public String getAlternativeName() { return alternativeName; }
    public String getDescription() { return description; }
    public int getYear() { return year; }
    public Poster getPoster() { return poster; }
    public Rating getRating() { return rating; }
    public List<Genre> getGenres() { return genres; }

    // Вложенный класс для постера
    public static class Poster {
        private String url; // URL постера
        public String getUrl() { return url; }
    }

    // Вложенный класс для рейтинга
    public static class Rating {
        @SerializedName("kp")
        private float kp; // Рейтинг по версии Кинопоиска
        public float getKp() { return kp; }
    }

    // Вложенный класс для жанров
    public static class Genre {
        private String name; // Название жанра
        public String getName() { return name; }
    }
}
