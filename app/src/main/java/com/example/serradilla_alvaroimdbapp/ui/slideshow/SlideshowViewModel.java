package com.example.serradilla_alvaroimdbapp.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


  //El ViewModel `SlideshowViewModel` es responsable de gestionar los datos del fragmento
  //de presentación de diapositivas (`SlideshowFragment`) de forma reactiva.

public class SlideshowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}