package com.example.serradilla_alvaroimdbapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class FavoritosActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar el DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_drawer);

        // Habilitar el botón de menú (hamburger menu)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_view);

        // Configurar la selección de ítems del menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Acción para "Top 10"
                Toast.makeText(FavoritosActivity.this, "Navegando a Top 10", Toast.LENGTH_SHORT).show();
                finish(); // Regresar a la actividad principal
            } else if (id == R.id.fav) {
                // Acción para "Favoritos"
                Toast.makeText(FavoritosActivity.this, "Ya estás en Favoritos", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el menú lateral
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Abrir el menú lateral al pulsar el botón
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
}








