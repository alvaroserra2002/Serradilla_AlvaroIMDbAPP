package com.example.serradilla_alvaroimdbapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serradilla_alvaroimdbapp.MovieDetailsActivity;
import com.example.serradilla_alvaroimdbapp.R;
import com.example.serradilla_alvaroimdbapp.api.ApiClient;
import com.example.serradilla_alvaroimdbapp.api.ApiService;
import com.example.serradilla_alvaroimdbapp.database.FavoritosDatabaseHelper;
import com.example.serradilla_alvaroimdbapp.models.MovieDetails;
import com.example.serradilla_alvaroimdbapp.models.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private final List<Movies> moviesList;
    private final Context context;

    private static final String API_KEY = "c2db7f170cmshc359159455b553fp1f3c95jsn36bf0b9aac41";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    public MoviesAdapter(Context context, List<Movies> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movies movie = moviesList.get(position);

        holder.textMovieTitle.setText(movie.getName());
        Picasso.get()
                .load(movie.getImageUrl())
                .into(holder.imageMovie);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getId());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            FavoritosDatabaseHelper dbHelper = new FavoritosDatabaseHelper(context);
            boolean isFavorito = false;

            // Verificar si la película ya está en favoritos
            List<Movies> favoritosList = dbHelper.getFavoritos();
            for (Movies favorito : favoritosList) {
                if (favorito.getId().equals(movie.getId())) {
                    isFavorito = true;
                    break;
                }
            }

            if (isFavorito) {
                // Eliminar de favoritos
                dbHelper.removeFavorito(movie.getId());
                Toast.makeText(context, "Eliminada de favoritos: " + movie.getName(), Toast.LENGTH_SHORT).show();
            } else {
                // Agregar a favoritos
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<MovieDetails> call = apiService.getMovieDetails(API_KEY, API_HOST, movie.getId());

                call.enqueue(new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            MovieDetails details = response.body();
                            MovieDetails.Title title = details.getData().getTitle();

                            String id = title.getId();
                            String name = title.getTitleText() != null ? title.getTitleText().getText() : "Sin título";
                            float rating = title.getRatingsSummary() != null ? title.getRatingsSummary().getAggregateRating() : 0.0f;
                            String imageUrl = title.getPrimaryImage() != null ? title.getPrimaryImage().getUrl() : null;
                            String plot = title.getPlot() != null && title.getPlot().getPlotText() != null
                                    ? title.getPlot().getPlotText().getPlainText() : "Sin argumento";

                            dbHelper.addFavorito(id, name, rating, imageUrl, plot);

                            Toast.makeText(context, "Agregada a favoritos: " + name, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al obtener detalles de la película", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetails> call, Throwable t) {
                        Toast.makeText(context, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return true;
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
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



