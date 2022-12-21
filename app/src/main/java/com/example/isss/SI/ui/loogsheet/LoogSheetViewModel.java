package com.example.isss.SI.ui.loogsheet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoogSheetViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoogSheetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}