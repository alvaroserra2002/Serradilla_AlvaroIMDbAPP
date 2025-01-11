package com.example.serradilla_alvaroimdbapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serradilla_alvaroimdbapp.R;
import com.example.serradilla_alvaroimdbapp.adapters.MoviesAdapter;
import com.example.serradilla_alvaroimdbapp.api.ApiClient;
import com.example.serradilla_alvaroimdbapp.api.ApiService;
import com.example.serradilla_alvaroimdbapp.models.Movies;
import com.example.serradilla_alvaroimdbapp.models.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movies> moviesList;

    private static final String API_KEY = "c2db7f170cmshc359159455b553fp1f3c95jsn36bf0b9aac41";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columnas

        moviesList = new ArrayList<>();

        moviesAdapter = new MoviesAdapter(getContext(), moviesList);
        recyclerView.setAdapter(moviesAdapter);

        loadMoviesFromApi();

        return root;
    }

    private void loadMoviesFromApi() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<MoviesResponse> call = apiService.getTopMeter(API_KEY, API_HOST, "ALL", 10);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MoviesResponse.Edge> edges = response.body().getData().getTopMeterTitles().getEdges();

                    for (MoviesResponse.Edge edge : edges) {
                        MoviesResponse.Node node = edge.getNode();

                        String id = node.getId(); // Extraer el ID de la película
                        String title = node.getTitleText().getText();
                        int releaseYear = node.getReleaseYear().getYear();
                        String imageUrl = node.getPrimaryImage().getUrl();

                        Movies movie = new Movies(id, title, imageUrl);
                        moviesList.add(movie);
                    }

                    moviesAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Error en la respuesta: " + response.message());
                    Toast.makeText(getContext(), "Error al cargar películas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Error en la llamada: " + t.getMessage());
                Toast.makeText(getContext(), "Error al cargar películas", Toast.LENGTH_SHORT).show();
            }
        });
    }


}




