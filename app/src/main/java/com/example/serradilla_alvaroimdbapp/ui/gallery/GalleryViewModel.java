package com.example.serradilla_alvaroimdbapp.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;



  //Clase ViewModel para la sección de la galería.
  //Gestiona los datos de la UI de manera independiente del ciclo de vida del fragmento o actividad.

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}