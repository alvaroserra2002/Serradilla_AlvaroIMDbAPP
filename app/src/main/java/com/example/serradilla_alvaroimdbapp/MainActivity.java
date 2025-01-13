package com.example.serradilla_alvaroimdbapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serradilla_alvaroimdbapp.adapters.MoviesAdapter;
import com.example.serradilla_alvaroimdbapp.api.ApiClient;
import com.example.serradilla_alvaroimdbapp.api.ApiService;
import com.example.serradilla_alvaroimdbapp.database.FavoritosDatabaseHelper;
import com.example.serradilla_alvaroimdbapp.models.Movies;
import com.example.serradilla_alvaroimdbapp.models.MoviesResponse;
import com.example.serradilla_alvaroimdbapp.models.SearchMovieResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serradilla_alvaroimdbapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * MainActivity es la actividad principal de la aplicación que gestiona la navegación entre diferentes fragmentos
 * (Inicio, Favoritos, Buscar Películas, etc.) y funciones principales como la autenticación con Google,
 * la búsqueda de películas, la visualización de las mejores películas, y el manejo de favoritos.
 * Proporciona una barra de navegación y un diseño de menú lateral para facilitar la interacción del usuario.
 */

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private GoogleSignInClient googleSignInClient;

    private RecyclerView recyclerViewTop10, recyclerViewFavoritos;
    private View searchMoviesView;
    private Button buttonShare, btnSearch;
    private Spinner spGenre;
    private TextView etYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewTop10 = findViewById(R.id.recyclerView);
        recyclerViewFavoritos = findViewById(R.id.recyclerViewFavoritos);
        searchMoviesView = findViewById(R.id.searchMoviesLayout);
        buttonShare = findViewById(R.id.buttonShare);
        spGenre = findViewById(R.id.spGenre);
        etYear = findViewById(R.id.etYear);
        btnSearch = findViewById(R.id.btnSearch);

        setupGenreSpinner();
        setupSearchButton();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.toolbar.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.toolbar).show();
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        ImageView profileImageView = headerView.findViewById(R.id.imageView);
        TextView userNameTextView = headerView.findViewById(R.id.userNameTextView);
        TextView userEmailTextView = headerView.findViewById(R.id.userEmailTextView);
        Button logoutButton = headerView.findViewById(R.id.logoutButton);

        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userPhoto = getIntent().getStringExtra("userPhoto");

        userNameTextView.setText(userName != null ? userName : "Nombre no disponible");
        userEmailTextView.setText(userEmail != null ? userEmail : "Correo no disponible");
        if (userPhoto != null) {
            Uri photoUri = Uri.parse(userPhoto);
            Picasso.get()
                    .load(photoUri)
                    .fit()
                    .centerInside()
                    .into(profileImageView);
        }

        logoutButton.setOnClickListener(v -> logout());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.fav, R.id.nav_search_movies)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showTop10View();
            } else if (id == R.id.fav) {
                showFavoritesView();
            } else if (id == R.id.nav_search_movies) {
                showSearchMoviesView();
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupGenreSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.genres,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenre.setAdapter(adapter);
    }

    private void showTop10View() {
        recyclerViewTop10.setVisibility(View.VISIBLE);
        recyclerViewFavoritos.setVisibility(View.GONE);
        searchMoviesView.setVisibility(View.GONE);
        buttonShare.setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Top 10");
        }
        Toast.makeText(this, "Cargando Top 10", Toast.LENGTH_SHORT).show();
    }

    private void showFavoritesView() {
        recyclerViewTop10.setVisibility(View.GONE);
        searchMoviesView.setVisibility(View.GONE);

        FavoritosDatabaseHelper dbHelper = new FavoritosDatabaseHelper(this);
        List<Movies> favoritosList = dbHelper.getFavoritos();

        if (favoritosList.isEmpty()) {
            recyclerViewFavoritos.setVisibility(View.GONE);
            Toast.makeText(this, "No hay favoritos", Toast.LENGTH_SHORT).show();
        } else {
            recyclerViewFavoritos.setVisibility(View.VISIBLE);
            MoviesAdapter adapter = new MoviesAdapter(this, favoritosList);
            recyclerViewFavoritos.setAdapter(adapter);
            recyclerViewFavoritos.setLayoutManager(new LinearLayoutManager(this));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favoritos");
        }
    }

    private void setupSearchButton() {
        btnSearch.setOnClickListener(v -> {
            String yearText = etYear.getText().toString().trim();
            if (yearText.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un año.", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, introduce un año válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            int genreIndex = spGenre.getSelectedItemPosition();
            int[] genreIds = getResources().getIntArray(R.array.genres);
            if (genreIndex < 0 || genreIndex >= genreIds.length) {
                Toast.makeText(this, "Por favor, selecciona un género.", Toast.LENGTH_SHORT).show();
                return;
            }

            int genreId = genreIds[genreIndex];

            performMovieSearch(year, genreId);
        });
    }

    private void performMovieSearch(int year, int genreId) {
        ApiService apiService = ApiClient.getTmdbClient().create(ApiService.class);
        String apiKey = "TU_API_KEY";
        String language = "es-ES";
        int page = 1;
        Call<SearchMovieResponse> call = apiService.searchMovies(apiKey, year, String.valueOf(genreId), page, language);
        call.enqueue(new retrofit2.Callback<SearchMovieResponse>() {
            @Override
            public void onResponse(Call<SearchMovieResponse> call, Response<SearchMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SearchMovieResponse.MovieResult> movies = response.body().getResults();

                    if (movies.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No se encontraron películas.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, MovieResultsActivity.class);
                        intent.putParcelableArrayListExtra("movies", new ArrayList<>(movies));
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al buscar películas.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SearchMovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showSearchMoviesView() {
        recyclerViewTop10.setVisibility(View.GONE);
        recyclerViewFavoritos.setVisibility(View.GONE);
        searchMoviesView.setVisibility(View.VISIBLE);
        buttonShare.setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Buscar Película");
        }
    }

    private void logout() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}


