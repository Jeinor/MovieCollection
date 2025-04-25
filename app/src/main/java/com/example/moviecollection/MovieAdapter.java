package com.example.moviecollection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // Список фильмов, отображаемых в RecyclerView
    private List<Movie> movieList = new ArrayList<>();

    /**
     * Устанавливает список фильмов и обновляет RecyclerView.
     * @param movies Список фильмов, полученных с сервера
     */
    public void setMovieList(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged(); // Уведомляем адаптер об изменениях
    }

    /**
     * Обновляет список фильмов новыми данными.
     * @param newMovies Новый список фильмов
     */
    public void updateMovies(List<Movie> newMovies) {
        this.movieList = newMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    // Создание нового элемента (ViewHolder) для списка
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false); // Подключаем макет одного элемента
        return new MovieViewHolder(view);
    }

    @Override
    // Привязываем данные к ViewHolder для отображения
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position); // Получаем фильм по позиции

        // Устанавливаем название фильма
        holder.title.setText(movie.getTitle());

        // Устанавливаем год фильма, если он есть
        int year = movie.getYear();
        if (year != 0) {
            holder.year.setText(String.valueOf(year));
            holder.year.setVisibility(View.VISIBLE);
        } else {
            holder.year.setVisibility(View.GONE); // Прячем, если года нет
        }

        // Получаем URL постера
        String posterUrl = movie.getPosterUrl();

        // Загружаем изображение через Glide или заглушку
        if (posterUrl != null && !posterUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(posterUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.poster);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.placeholder)
                    .into(holder.poster);
        }

        // При клике на фильм переходим на экран с деталями
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("movieId", String.valueOf(movie.getId())); // Передаём ID фильма

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.MovieDetailFragment, bundle); // Навигация к фрагменту деталей
        });
    }

    @Override
    // Возвращает количество фильмов в списке
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * ViewHolder — класс, который хранит элементы UI одного элемента списка
     */
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, year;
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            // Инициализация UI-элементов
            title = itemView.findViewById(R.id.textTitle);
            year = itemView.findViewById(R.id.textYear);
            poster = itemView.findViewById(R.id.imagePoster);
        }
    }
}
