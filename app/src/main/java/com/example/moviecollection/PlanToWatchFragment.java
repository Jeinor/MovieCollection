package com.example.moviecollection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviecollection.databinding.FragmentPlanToWatchBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanToWatchFragment extends Fragment {

    private FragmentPlanToWatchBinding binding;
    private MovieAdapter adapter;
    private DatabaseHelper db;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater in,@Nullable ViewGroup c,@Nullable Bundle s){
        binding = FragmentPlanToWatchBinding.inflate(in,c,false);
        return binding.getRoot();
    }

    @Override public void onViewCreated(@NonNull View v,@Nullable Bundle s){
        db = new DatabaseHelper(requireContext());
        adapter = new MovieAdapter(requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadMovies("plan");
    }

    private void loadMovies(String status){
        String user = SessionManager.getInstance(requireContext()).getUsername();
        List<Integer> ids = db.getMovieIdsByStatus(user,status);

        if(ids.isEmpty()){
            adapter.setMovieList(new ArrayList<>());
            return;
        }

        KinopoiskApi api = ApiClient.getRetrofitInstance().create(KinopoiskApi.class);
        List<Movie> result = new ArrayList<>();
        final int total = ids.size();
        final int[] done = {0};

        for(int id: ids){
            api.getMovieDetail(id).enqueue(new Callback<MovieDetail>() {
                @Override public void onResponse(Call<MovieDetail> c,Response<MovieDetail> r){
                    if(r.isSuccessful() && r.body()!=null){
                        MovieDetail d = r.body();
                        result.add(new Movie(
                                d.getId(), d.getName(), d.getAlternativeName(),
                                d.getYear(), new Movie.Poster(d.getPoster().getUrl())
                        ));
                    }
                    if(++done[0]==total) adapter.setMovieList(result);
                }
                @Override public void onFailure(Call<MovieDetail> c,Throwable t){
                    if(++done[0]==total) adapter.setMovieList(result);
                }
            });
        }
    }

    @Override public void onDestroyView(){ super.onDestroyView(); binding=null; }
}