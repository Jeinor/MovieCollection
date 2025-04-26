package com.example.moviecollection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.moviecollection.databinding.FragmentMovieListBinding;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment {

    private FragmentMovieListBinding binding;
    private MovieAdapter adapter;                     // без инициализации
    private final KinopoiskApi api =
            ApiClient.getRetrofitInstance().create(KinopoiskApi.class);

    // life-cycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup c, Bundle s) {
        binding = FragmentMovieListBinding.inflate(inflater, c, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        // создаём адаптер, когда есть Context
        adapter = new MovieAdapter(requireContext());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        loadPopular();   // дефолтная загрузка

        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable e) {
                String q = e.toString().trim();
                binding.buttonClearSearch.setVisibility(q.isEmpty() ? View.GONE : View.VISIBLE);
                if (q.isEmpty()) loadPopular(); else search(q);
            }
            @Override public void beforeTextChanged(CharSequence s,int st,int c,int a) {}
            @Override public void onTextChanged(CharSequence s,int st,int b,int c)   {}
        });

        binding.buttonClearSearch.setOnClickListener(v1 -> {
            binding.editSearch.setText("");
            binding.editSearch.clearFocus();
            loadPopular();
        });
    }

    // загрузки из API
    private void loadPopular() {
        List<String> fields = Arrays.asList("id","name","alternativeName","year","poster","rating");
        api.getPopularMovies(1,20,fields,"rating.filmCritics",-1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override public void onResponse(Call<MovieResponse> c, Response<MovieResponse> r) {
                        adapter.setMovieList(r.isSuccessful() && r.body()!=null ? r.body().getDocs() : Arrays.asList());
                    }
                    @Override public void onFailure(Call<MovieResponse> c, Throwable t) { /* ignore */ }
                });
    }

    private void search(String q) {
        api.searchMovies(q,20,1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override public void onResponse(Call<MovieResponse> c, Response<MovieResponse> r) {
                        adapter.setMovieList(r.isSuccessful() && r.body()!=null ? r.body().getDocs() : Arrays.asList());
                    }
                    @Override public void onFailure(Call<MovieResponse> c, Throwable t) { /* ignore */ }
                });
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}