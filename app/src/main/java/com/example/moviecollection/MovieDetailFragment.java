package com.example.moviecollection;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moviecollection.databinding.FragmentMovieDetailBinding;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {

    private FragmentMovieDetailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Привязка ViewBinding к макету
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false);

        // Получаем переданный ID фильма и загружаем его детали
        if (getArguments() != null) {
            String movieIdStr = getArguments().getString("movieId");
            if (movieIdStr != null) {
                int movieId = Integer.parseInt(movieIdStr);
                loadMovieDetail(movieId);
            }
        }

        return binding.getRoot();
    }

    /**
     * Загружает подробную информацию о фильме по его ID
     */
    private void loadMovieDetail(int movieId) {
        // Создаём экземпляр API-интерфейса
        KinopoiskApi api = ApiClient.getRetrofitInstance().create(KinopoiskApi.class);

        // Выполняем GET-запрос к эндпоинту /movie/{id}
        Call<MovieDetail> call = api.getMovieDetail(movieId);

        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetail movie = response.body();

                    // Заполняем данные на экране
                    binding.textTitle.setText(movie.getName());

                    if (!TextUtils.isEmpty(movie.getAlternativeName())) {
                        binding.textAltTitle.setText("(" + movie.getAlternativeName() + ")");
                    } else {
                        binding.textAltTitle.setVisibility(View.GONE);
                    }

                    binding.textDescription.setText(movie.getDescription());
                    binding.textYear.setText("Год: " + movie.getYear());
                    binding.textRating.setText("Рейтинг: " + movie.getRating().getKp());

                    // Загрузка постера через Glide
                    Glide.with(requireContext())
                            .load(movie.getPoster().getUrl())
                            .into(binding.imagePoster);

                    // Вывод жанров
                    if (movie.getGenres() != null) {
                        String genres = TextUtils.join(", ",
                                movie.getGenres().stream()
                                        .map(MovieDetail.Genre::getName)
                                        .collect(Collectors.toList()));
                        binding.textGenres.setText("Жанры: " + genres);
                    }

                } else {
                    Toast.makeText(getContext(), "Не удалось загрузить данные", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}