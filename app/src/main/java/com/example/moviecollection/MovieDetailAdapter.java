package com.example.moviecollection;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для отображения фильмов на основе объекта Movie (в т.ч. из MovieDetail).
 * Используется в WatchedFragment и PlanToWatchFragment.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieViewHolder> {

    // Список фильмов для отображения
    private List<Movie> movieList = new ArrayList<>();

    /**
     * Устанавливает список фильмов
     * @param movies список фильмов
     */
    public void setMovieList(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Инфлейтим макет для одного элемента фильма
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Название фильма
        holder.title.setText(movie.getTitle());

        // Год, если он указан
        int year = movie.getYear();
        if (year != 0) {
            holder.year.setText(String.valueOf(year));
            holder.year.setVisibility(View.VISIBLE);
        } else {
            holder.year.setVisibility(View.GONE);
        }

        // Постер
        String posterUrl = movie.getPosterUrl();
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

        // Без перехода — только отображение
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * ViewHolder — хранит ссылки на UI-элементы
     */
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, year;
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            year = itemView.findViewById(R.id.textYear);
            poster = itemView.findViewById(R.id.imagePoster);
        }
    }
}