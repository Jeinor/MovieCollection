package com.example.moviecollection;

import com.google.gson.annotations.SerializedName;

/**
 * Модель для представления краткой информации о фильме.
 * Используется в списке фильмов (RecyclerView).
 */
public class Movie {

    public Movie(int id, String name, String alternativeName, int year, Poster poster) {
        this.id = id;
        this.name = name;
        this.alternativeName = alternativeName;
        this.year = year;
        this.poster = poster;
    }

    @SerializedName("id")
    private int id; // Уникальный ID фильма

    @SerializedName("name")
    private String name; // Название фильма

    @SerializedName("alternativeName")
    private String alternativeName; // Альтернативное название (например, оригинальное)

    @SerializedName("year")
    private Integer year; // Год выпуска фильма (может быть null)

    @SerializedName("poster")
    private Poster poster; // Объект постера с превью

    /**
     * Вложенный класс, представляющий постер фильма.
     */
    public static class Poster {
        @SerializedName("previewUrl")
        private String previewUrl;

        // Конструктор, принимающий URL постера
        public Poster(String previewUrl) {
            this.previewUrl = previewUrl;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }
    }

    public int getId() {
        return id;
    }

    /**
     * Возвращает название фильма. Если нет основного названия, берём альтернативное.
     * Если и его нет — "(без названия)".
     */
    public String getTitle() {
        return name != null ? name : alternativeName != null ? alternativeName : "(без названия)";
    }

    /**
     * Возвращает год выпуска. Если null — возвращаем 0.
     */
    public int getYear() {
        return year != null ? year : 0;
    }

    /**
     * Возвращает URL миниатюры постера, если доступен.
     */
    public String getPosterUrl() {
        return poster != null ? poster.getPreviewUrl() : null;
    }

}

