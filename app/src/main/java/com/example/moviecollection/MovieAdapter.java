package com.example.moviecollection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

// Универсальный адаптер, который сразу работает с чекбоксами и БД
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies = new ArrayList<>();
    private final DatabaseHelper db;
    private final String username;

    public MovieAdapter(Context ctx) {
        db = new DatabaseHelper(ctx.getApplicationContext());
        username = SessionManager.getInstance(ctx).getUsername();
    }

    public void setMovieList(List<Movie> newMovies) {
        movies.clear();
        movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup p,int vType){
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.movie_item,p,false);
        return new MovieViewHolder(v);
    }

    @Override public int getItemCount(){ return movies.size(); }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder h,int pos){
        Movie m = movies.get(pos);

        h.title.setText(m.getTitle());
        h.year.setText(m.getYear()!=0 ? String.valueOf(m.getYear()) : "");

        Glide.with(h.itemView).load(m.getPosterUrl())
                .placeholder(R.drawable.placeholder).into(h.poster);

        // сбрасываем старые слушатели
        h.cbViewed.setOnCheckedChangeListener(null);
        h.cbPlan  .setOnCheckedChangeListener(null);

        // актуальное состояние из БД
        boolean viewed = db.movieExists(username,m.getId(),"viewed");
        boolean planned= db.movieExists(username,m.getId(),"plan");

        h.cbViewed.setChecked(viewed);
        h.cbPlan  .setChecked(planned);

        // новые слушатели
        h.cbViewed.setOnCheckedChangeListener((b,checked)->{
            if(checked) db.addMovieForUser(username,m.getId(),"viewed");
            else        db.removeMovieForUser(username,m.getId(),"viewed");
        });

        h.cbPlan.setOnCheckedChangeListener((b,checked)->{
            if(checked) db.addMovieForUser(username,m.getId(),"plan");
            else        db.removeMovieForUser(username,m.getId(),"plan");
        });

        // переход к деталям (постер + описание)
        h.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("movieId", String.valueOf(m.getId()));
            NavController nav = Navigation.findNavController(v);
            nav.navigate(R.id.MovieDetailFragment,bundle);
        });
    }

    // ViewHolder
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title,year;
        ImageView poster;
        CheckBox cbViewed,cbPlan;

        MovieViewHolder(@NonNull View v){
            super(v);
            title = v.findViewById(R.id.textTitle);
            year  = v.findViewById(R.id.textYear );
            poster= v.findViewById(R.id.imagePoster);
            cbViewed = v.findViewById(R.id.checkboxViewed);
            cbPlan   = v.findViewById(R.id.checkboxPlan);
        }
    }
}
