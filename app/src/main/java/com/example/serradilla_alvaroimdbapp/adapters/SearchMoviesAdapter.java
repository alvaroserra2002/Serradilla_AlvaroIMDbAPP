package com.example.serradilla_alvaroimdbapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serradilla_alvaroimdbapp.R;
import com.example.serradilla_alvaroimdbapp.models.SearchMovieResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchMoviesAdapter extends RecyclerView.Adapter<SearchMoviesAdapter.MovieViewHolder> {

    private final Context context;
    private final List<SearchMovieResponse.MovieResult> movies;

    public SearchMoviesAdapter(Context context, List<SearchMovieResponse.MovieResult> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        SearchMovieResponse.MovieResult movie = movies.get(position);

        holder.textMovieTitle.setText(movie.getTitle());

        if (movie.getPosterPath() != null) {
            String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Picasso.get()
                    .load(imageUrl)
                    .into(holder.imageMovie);
        }

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageMovie;
        TextView textMovieTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.imageMovie);
            textMovieTitle = itemView.findViewById(R.id.textMovieTitle);
        }
    }
}

