package com.example.serradilla_alvaroimdbapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.serradilla_alvaroimdbapp.models.Movies;

import java.util.ArrayList;
import java.util.List;

public class FavoritosDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritos.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_FAVORITOS = "favoritos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PLOT = "plot";

    private Context context;

    public FavoritosDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_FAVORITOS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_PLOT + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String addPlotColumnQuery = "ALTER TABLE " + TABLE_FAVORITOS + " ADD COLUMN " + COLUMN_PLOT + " TEXT";
            db.execSQL(addPlotColumnQuery);
        }
    }

    public void addFavorito(String id, String name, float rating, String image, String plot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_PLOT, plot);

        long result = db.insert(TABLE_FAVORITOS, null, values);
        if (result != -1) {
            Toast.makeText(context, "Agregada a favoritos: " + name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al agregar a favoritos", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void removeFavorito(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_FAVORITOS, COLUMN_ID + "=?", new String[]{id});
        if (rowsDeleted > 0) {
            Toast.makeText(context, "Eliminada de favoritos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }


    public List<Movies> getFavoritos() {
        List<Movies> favoritosList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITOS, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int ratingIndex = cursor.getColumnIndex(COLUMN_RATING);
            int imageIndex = cursor.getColumnIndex(COLUMN_IMAGE);
            int plotIndex = cursor.getColumnIndex(COLUMN_PLOT);

            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                float rating = cursor.getFloat(ratingIndex);
                String image = cursor.getString(imageIndex);
                String plot = cursor.getString(plotIndex);

                favoritosList.add(new Movies(id, name, rating, image, plot));
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return favoritosList;
    }
}

