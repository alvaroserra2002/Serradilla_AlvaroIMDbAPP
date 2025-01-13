package com.example.serradilla_alvaroimdbapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serradilla_alvaroimdbapp.adapters.SearchMoviesAdapter;
import com.example.serradilla_alvaroimdbapp.models.SearchMovieResponse;

import java.util.ArrayList;

public class MovieResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_results);

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);

        ArrayList<SearchMovieResponse.MovieResult> movies = getIntent().getParcelableArrayListExtra("movies");

        if (movies == null || movies.isEmpty()) {
            Toast.makeText(this, "No se encontraron películas.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SearchMoviesAdapter adapter = new SearchMoviesAdapter(this, movies);
        recyclerViewMovies.setAdapter(adapter);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
    }

}

