package com.example.serradilla_alvaroimdbapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serradilla_alvaroimdbapp.adapters.MoviesAdapter;
import com.example.serradilla_alvaroimdbapp.database.FavoritosDatabaseHelper;
import com.example.serradilla_alvaroimdbapp.models.Movies;
import com.example.serradilla_alvaroimdbapp.models.MoviesResponse;
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

import android.util.Log;
import android.widget.Toast;

import com.example.serradilla_alvaroimdbapp.api.ApiClient;
import com.example.serradilla_alvaroimdbapp.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private GoogleSignInClient googleSignInClient;

    private static final String API_KEY = "c2db7f170cmshc359159455b553fp1f3c95jsn36bf0b9aac41";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        callTopMeterEndpoint();

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

        // Actualizar AppBarConfiguration con "Favoritos"
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.fav)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configurar el listener del NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Referencias a los elementos del diseño
            RecyclerView recyclerViewTop10 = findViewById(R.id.recyclerView);
            RecyclerView recyclerViewFavoritos = findViewById(R.id.recyclerViewFavoritos);
            TextView textViewEmpty = findViewById(R.id.textViewEmpty);

            if (id == R.id.nav_home) {
                // Mostrar el RecyclerView del Top 10
                recyclerViewTop10.setVisibility(View.VISIBLE);
                recyclerViewFavoritos.setVisibility(View.GONE);
                textViewEmpty.setVisibility(View.GONE);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Top 10");
                }
                Toast.makeText(this, "Cargando Top 10", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.fav) {
                // Mostrar el RecyclerView de Favoritos
                recyclerViewTop10.setVisibility(View.GONE);

                // Configurar el RecyclerView para Favoritos
                FavoritosDatabaseHelper dbHelper = new FavoritosDatabaseHelper(this);
                List<Movies> favoritosList = dbHelper.getFavoritos();

                if (favoritosList.isEmpty()) {
                    recyclerViewFavoritos.setVisibility(View.GONE);
                    textViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewFavoritos.setVisibility(View.VISIBLE);
                    textViewEmpty.setVisibility(View.GONE);

                    MoviesAdapter adapter = new MoviesAdapter(this, favoritosList);
                    recyclerViewFavoritos.setAdapter(adapter);

                    // Cambiar a un diseño de lista (una sola columna)
                    recyclerViewFavoritos.setLayoutManager(new LinearLayoutManager(this));
                }

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Favoritos");
                }
                Toast.makeText(this, "Mostrando Favoritos", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el menú lateral
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });




    }



    private void logout() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }

    private void callTopMeterEndpoint() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<MoviesResponse> call = apiService.getTopMeter(API_KEY, API_HOST, "ALL", 10);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MoviesResponse.Edge> edges = response.body().getData().getTopMeterTitles().getEdges();

                    for (MoviesResponse.Edge edge : edges) {
                        MoviesResponse.Node node = edge.getNode();

                        String title = node.getTitleText().getText();
                        int releaseYear = node.getReleaseYear().getYear();
                        String imageUrl = node.getPrimaryImage().getUrl();

                        Log.d("MOVIE", "Name: " + title +
                                ", Release Year: " + releaseYear +
                                ", Image URL: " + imageUrl);

                    }

                    Toast.makeText(MainActivity.this, "Películas recibidas correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Error en la respuesta: " + response.message());
                    Toast.makeText(MainActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Error en la llamada: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error en la llamada", Toast.LENGTH_SHORT).show();
            }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fav) {
            Intent intent = new Intent(this, FavoritosActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}

