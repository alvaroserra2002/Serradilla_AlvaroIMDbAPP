package com.example.serradilla_alvaroimdbapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


  //Clase modelo para manejar la respuesta de la API de búsqueda de películas (TMDb).
  //La clase utiliza Json para mapear automáticamente los campos del JSON a objetos Java.


public class SearchMovieResponse {

    @SerializedName("results")
    private List<MovieResult> results;

    public List<MovieResult> getResults() {
        return results;
    }

    public static class MovieResult implements Parcelable {

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("overview")
        private String overview;

        @SerializedName("poster_path")
        private String posterPath;

        @SerializedName("vote_average")
        private float voteAverage;

        @SerializedName("release_date")
        private String releaseDate;

        protected MovieResult(Parcel in) {
            id = in.readInt();
            title = in.readString();
            overview = in.readString();
            posterPath = in.readString();
            voteAverage = in.readFloat();
            releaseDate = in.readString();
        }

        public static final Creator<MovieResult> CREATOR = new Creator<MovieResult>() {
            @Override
            public MovieResult createFromParcel(Parcel in) {
                return new MovieResult(in);
            }

            @Override
            public MovieResult[] newArray(int size) {
                return new MovieResult[size];
            }
        };

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }


        public String getPosterPath() {
            return posterPath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(title);
            dest.writeString(overview);
            dest.writeString(posterPath);
            dest.writeFloat(voteAverage);
            dest.writeString(releaseDate);
        }
    }
}

