package com.example.serradilla_alvaroimdbapp.api;

import com.example.serradilla_alvaroimdbapp.models.MovieDetails;
import com.example.serradilla_alvaroimdbapp.models.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    @GET("title/get-top-meter")
    Call<MoviesResponse> getTopMeter(
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost,
            @Query("topMeterTitlesType") String topMeterTitlesType,
            @Query("limit") int limit
    );

    @GET("title/get-overview")
    Call<MovieDetails> getMovieDetails(
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost,
            @Query("tconst") String tconst
    );



}


