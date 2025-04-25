package com.example.moviecollection;

import java.util.List;

/**
 * Модель ответа от API для списка фильмов.
 * Используется для парсинга результата запроса к /movie.
 */
public class MovieResponse {

    // Список фильмов, полученный с сервера
    private List<Movie> docs;

    /**
     * Возвращает список фильмов.
     */
    public List<Movie> getDocs() {
        return docs;
    }

    /**
     * Устанавливает список фильмов.
     */
    public void setDocs(List<Movie> docs) {
        this.docs = docs;
    }
}
