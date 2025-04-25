package com.example.moviecollection;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moviecollection.databinding.FragmentMovieListBinding;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment {

    // ViewBinding для работы с разметкой fragment_movie_list.xml
    private FragmentMovieListBinding binding;

    // Адаптер для отображения списка фильмов в RecyclerView
    private MovieAdapter adapter = new MovieAdapter();

    // Интерфейс для запросов к API, создаётся через Retrofit
    private final KinopoiskApi api = ApiClient.getRetrofitInstance().create(KinopoiskApi.class);

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Привязываем макет к binding и возвращаем корневой View
        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Настройка RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        // Загружаем популярные фильмы при запуске экрана
        loadPopularMovies();

        // Отслеживаем изменения текста в поле поиска
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();

                // Показываем кнопку очистки, если поле не пустое
                binding.buttonClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);

                if (query.isEmpty()) {
                    // Если поле пустое — возвращаем популярные фильмы
                    loadPopularMovies();
                } else {
                    // Иначе — выполняем поиск по названию
                    searchMovies(query);
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Очистка поля поиска и возврат к популярным фильмам
        binding.buttonClearSearch.setOnClickListener(v -> {
            binding.editSearch.setText("");
            binding.editSearch.clearFocus();
            loadPopularMovies();
            binding.buttonClearSearch.setVisibility(View.GONE);
        });
    }

    /**
     * Загружает популярные фильмы из API.
     */
    private void loadPopularMovies() {
        // Список полей, которые мы хотим получить от API
        List<String> fields = Arrays.asList("id", "name", "alternativeName", "year", "poster", "rating");

        // Выполняем запрос на популярные фильмы
        api.getPopularMovies(
                1,              // Страница
                20,             // Кол-во фильмов
                fields,         // Нужные поля
                "rating.filmCritics", // Сортировка по рейтингу критиков
                -1              // Тип сортировки: -1 = убывание
        ).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Отображаем полученные фильмы
                    adapter.setMovieList(response.body().getDocs());
                } else {
                    Log.e("API", "Ошибка загрузки: код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API", "Ошибка сети: " + t.getMessage());
            }
        });
    }

    /**
     * Выполняет поиск фильмов по названию.
     * @param query строка поиска
     */
    private void searchMovies(String query) {
        // Выполняем GET-запрос с параметром name
        api.searchMovies(
                query,
                20,
                1
        ).enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setMovieList(response.body().getDocs());
                } else {
                    Toast.makeText(getContext(), "Фильмы не найдены", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Геттер/сеттер для адаптера, если понадобится снаружи
    public MovieAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MovieAdapter adapter) {
        this.adapter = adapter;
    }
}