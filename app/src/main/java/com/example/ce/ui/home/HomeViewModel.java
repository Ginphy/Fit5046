package com.example.ce.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;


    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        //The setValue method updates the data, and LiveData will notify all observers that the data has been updated

    }


    public LiveData<String> getText() {
        return mText;
    }
    //LiveData is a data entity class that can temporarily store data and bind to the life cycle
}