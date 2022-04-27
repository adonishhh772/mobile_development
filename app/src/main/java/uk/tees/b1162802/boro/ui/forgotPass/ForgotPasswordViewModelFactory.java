package uk.tees.b1162802.boro.ui.forgotPass;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import uk.tees.b1162802.boro.data.ForgotPassDataSource;
import uk.tees.b1162802.boro.data.ForgotPassRepository;

public class ForgotPasswordViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPassViewModel.class)) {
            return (T) new ForgotPassViewModel(ForgotPassRepository.getInstance(new ForgotPassDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}