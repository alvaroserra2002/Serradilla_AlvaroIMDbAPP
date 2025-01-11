package com.example.serradilla_alvaroimdbapp;

import android.os.Bundle;
import android.util.Log;
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

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = "c2db7f170cmshc359159455b553fp1f3c95jsn36bf0b9aac41";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        String movieId = getIntent().getStringExtra("MOVIE_ID");

        ImageView moviePoster = findViewById(R.id.moviePoster);
        TextView movieTitle = findViewById(R.id.movieTitle);
        TextView moviePlot = findViewById(R.id.moviePlot);
        TextView movieReleaseDate = findViewById(R.id.movieReleaseDate);
        TextView movieRating = findViewById(R.id.movieRating);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<MovieDetails> call = apiService.getMovieDetails(API_KEY, API_HOST, movieId);

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetails details = response.body();
                    MovieDetails.Title titleData = details.getData().getTitle();

                    String title = titleData.getTitleText() != null ? titleData.getTitleText().getText() : "Title not available";
                    String plot = titleData.getPlot() != null && titleData.getPlot().getPlotText() != null
                            ? titleData.getPlot().getPlotText().getPlainText() : "Plot not available";
                    String releaseDate = titleData.getReleaseDate() != null
                            ? titleData.getReleaseDate().getFormattedDate() : "Release date not available";
                    String rating = titleData.getRatingsSummary() != null
                            ? String.valueOf(titleData.getRatingsSummary().getAggregateRating()) : "Rating not available";
                    String posterUrl = titleData.getPrimaryImage() != null
                            ? titleData.getPrimaryImage().getUrl() : null;

                    movieTitle.setText(title);
                    moviePlot.setText(plot);
                    movieReleaseDate.setText("Release Date: " + releaseDate);
                    movieRating.setText("Rating: " + rating);

                    if (posterUrl != null) {
                        Picasso.get().load(posterUrl).into(moviePoster);
                    }
                } else {
                    Log.e("API_ERROR", "Response unsuccessful: " + response.message());
                }
            }


            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.e("API_FAILURE", "Request failed: " + t.getMessage());
            }
        });
    }

}


