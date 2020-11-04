package com.example.project.ui.ui_for_main.worksites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorksitesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WorksitesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is worksites fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}