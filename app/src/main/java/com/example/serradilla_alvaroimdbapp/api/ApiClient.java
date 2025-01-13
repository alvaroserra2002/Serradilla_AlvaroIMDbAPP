package com.example.serradilla_alvaroimdbapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



  //Clase ApiClient que proporciona instancias configuradas de Retrofit
  //para interactuar con diferentes APIs.


public class ApiClient {

    private static final String BASE_URL = "https://imdb-com.p.rapidapi.com/";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit;
    private static Retrofit tmdbRetrofit;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getTmdbClient() {
        if (tmdbRetrofit == null) {
            tmdbRetrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return tmdbRetrofit;
    }
}


