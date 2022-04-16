package uk.tees.b1162802.boro.ui.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServiceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ServiceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Your Services");
    }

    public LiveData<String> getText() {
        return mText;
    }
}