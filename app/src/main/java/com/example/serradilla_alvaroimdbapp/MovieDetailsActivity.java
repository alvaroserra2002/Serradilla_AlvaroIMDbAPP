package com.example.serradilla_alvaroimdbapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serradilla_alvaroimdbapp.api.ApiClient;
import com.example.serradilla_alvaroimdbapp.api.ApiService;
import com.example.serradilla_alvaroimdbapp.models.MovieDetails;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MovieDetailsActivity muestra los detalles de una película seleccionada.
 * Obtiene la información desde una API remota (usando Retrofit) e incluye una opción para compartir
 * la información de la película a través de un mensaje SMS.
 *
 * Funcionalidades:
 * - Obtener detalles como título, trama, fecha de lanzamiento, valoración y póster de una película usando su ID.
 * - Mostrar los detalles en una interfaz de usuario bien organizada.
 * - Proveer un botón para enviar un SMS con información básica de la película.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = "c2db7f170cmshc359159455b553fp1f3c95jsn36bf0b9aac41";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    private String movieName;
    private float movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        String movieId = getIntent().getStringExtra("MOVIE_ID");

        ImageView moviePoster = findViewById(R.id.moviePoster);
        TextView movieTitle = findViewById(R.id.movieTitle);
        TextView moviePlot = findViewById(R.id.moviePlot);
        TextView movieReleaseDate = findViewById(R.id.movieReleaseDate);
        TextView movieRatingTextView = findViewById(R.id.movieRating);
        Button btnSendSms = findViewById(R.id.btnSendSms);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<MovieDetails> call = apiService.getMovieDetails(API_KEY, API_HOST, movieId);

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetails details = response.body();
                    MovieDetails.Title titleData = details.getData().getTitle();

                    movieName = titleData.getTitleText() != null ? titleData.getTitleText().getText() : "Title not available";
                    String plot = titleData.getPlot() != null && titleData.getPlot().getPlotText() != null
                            ? titleData.getPlot().getPlotText().getPlainText() : "Plot not available";
                    String releaseDate = titleData.getReleaseDate() != null
                            ? titleData.getReleaseDate().getFormattedDate() : "Release date not available";
                    movieRating = titleData.getRatingsSummary() != null
                            ? titleData.getRatingsSummary().getAggregateRating() : 0;
                    String posterUrl = titleData.getPrimaryImage() != null
                            ? titleData.getPrimaryImage().getUrl() : null;

                    movieTitle.setText(movieName);
                    moviePlot.setText(plot);
                    movieReleaseDate.setText("Release Date: " + releaseDate);
                    movieRatingTextView.setText("Rating: " + movieRating);

                    if (posterUrl != null) {
                        Picasso.get().load(posterUrl).into(moviePoster);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });

        btnSendSms.setOnClickListener(v -> sendSms());
    }

    private void sendSms() {
        String message = "Esta película te gustará: " + movieName + ". Valoración: " + movieRating;
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("sms_body", message);

        try {
            startActivity(smsIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



