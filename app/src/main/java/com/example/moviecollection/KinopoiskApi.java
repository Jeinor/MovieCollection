package com.example.moviecollection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KinopoiskApi {

    // Получение списка фильмов (например, популярных)
    @GET("movie")
    Call<MovieResponse> getPopularMovies(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("selectFields") List<String> selectFields,
            @Query("sortField") String sortField,
            @Query("sortType") int sortType // -1 = убывание, 1 = возрастание
    );

    // Получение детальной информации по ID фильма
    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(
            @Path("id") int id
    );

    // Поиск по названию, жанру или персоне
    @GET("movie/search")
    Call<MovieResponse> searchMovies(
            @Query("query") String query,
            @Query("limit") int limit,
            @Query("page") int page
    );
}
