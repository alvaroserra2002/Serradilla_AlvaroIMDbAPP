package com.example.serradilla_alvaroimdbapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.serradilla_alvaroimdbapp.models.Movies;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Movies>> moviesLiveData;

    public HomeViewModel() {
        moviesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Movies>> getMovies() {
        return moviesLiveData;
    }

    public void setMovies(List<Movies> movies) {
        moviesLiveData.setValue(movies);
    }
}
