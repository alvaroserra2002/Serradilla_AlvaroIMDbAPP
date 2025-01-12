package com.example.serradilla_alvaroimdbapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serradilla_alvaroimdbapp.adapters.MoviesAdapter;
import com.example.serradilla_alvaroimdbapp.database.FavoritosDatabaseHelper;
import com.example.serradilla_alvaroimdbapp.models.Movies;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private RecyclerView recyclerViewFavoritos;
    private Button buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        recyclerViewFavoritos = findViewById(R.id.recyclerViewFavoritos);
        buttonShare = findViewById(R.id.buttonShare);

        buttonShare.setOnClickListener(v -> {
            if (arePermissionsGranted()) {
                compartirFavoritos();
            } else {
                requestPermissions();
            }
        });

        cargarFavoritos();
    }

    private void cargarFavoritos() {
        FavoritosDatabaseHelper dbHelper = new FavoritosDatabaseHelper(this);
        List<Movies> favoritosList = dbHelper.getFavoritos();

        if (favoritosList.isEmpty()) {
            Toast.makeText(this, "No hay películas favoritas.", Toast.LENGTH_SHORT).show();
        } else {
            MoviesAdapter adapter = new MoviesAdapter(this, favoritosList);
            recyclerViewFavoritos.setAdapter(adapter);
            recyclerViewFavoritos.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private boolean arePermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "Permisos concedidos. Ahora puedes compartir tus favoritos.", Toast.LENGTH_SHORT).show();
                compartirFavoritos();
            } else {
                Toast.makeText(this, "Permisos denegados. No se puede compartir sin los permisos requeridos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void compartirFavoritos() {
        FavoritosDatabaseHelper dbHelper = new FavoritosDatabaseHelper(this);
        List<Movies> favoritosList = dbHelper.getFavoritos();

        if (favoritosList.isEmpty()) {
            Toast.makeText(this, "No hay películas favoritas para compartir.", Toast.LENGTH_SHORT).show();
            return;
        }

        String favoritosJson = generarJsonDeFavoritos(favoritosList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Películas Favoritas en JSON");

        TextView textView = new TextView(this);
        textView.setText(favoritosJson);
        textView.setPadding(16, 16, 16, 16);
        textView.setTextIsSelectable(true);
        builder.setView(textView);

        builder.setPositiveButton("CERRAR", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }



    private String generarJsonDeFavoritos(List<Movies> favoritosList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (Movies movie : favoritosList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", movie.getId());
                jsonObject.put("title", movie.getName());
                jsonObject.put("posterUrl", movie.getImageUrl());
                jsonObject.put("rating", movie.getRating());
                jsonObject.put("overview", movie.getPlot());
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
}












