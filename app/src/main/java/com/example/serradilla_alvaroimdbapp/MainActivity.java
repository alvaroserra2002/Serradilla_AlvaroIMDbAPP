package com.example.serradilla_alvaroimdbapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.serradilla_alvaroimdbapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private GoogleSignInClient googleSignInClient; // Cliente de Google Sign-In para logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar Google Sign-In para logout
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show();
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configurar el encabezado del NavigationView
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImageView = headerView.findViewById(R.id.imageView);
        TextView userNameTextView = headerView.findViewById(R.id.userNameTextView);
        TextView userEmailTextView = headerView.findViewById(R.id.userEmailTextView);
        Button logoutButton = headerView.findViewById(R.id.logoutButton); // Botón de logout

        // Obtener datos enviados desde LoginActivity
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userPhoto = getIntent().getStringExtra("userPhoto");

        // Actualizar los datos en el encabezado
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


        // Configurar acción del botón de logout
        logoutButton.setOnClickListener(v -> logout());

        // Configurar navegación
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void logout() {
        // Cerrar sesión de Google
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            // Redirigir al LoginActivity
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

