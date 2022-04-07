package uk.tees.b1162802.boro.ui.forgotPass;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.data.ForgotPassRepository;
import uk.tees.b1162802.boro.data.Result;


public class ForgotPassViewModel extends ViewModel {
    private MutableLiveData<ForgotPassFormState> forgotFormState = new MutableLiveData<>();
    private MutableLiveData<ForgotPassResult> forgotPassResult = new MutableLiveData<>();
    private ForgotPassRepository forgotPassRepository;

    ForgotPassViewModel(ForgotPassRepository forgotPassRepository) {
        this.forgotPassRepository = forgotPassRepository;
    }

    LiveData<ForgotPassFormState> getForgotPassFormState() {
        return forgotFormState;
    }

    LiveData<ForgotPassResult> getForgotPassResult() {
        return forgotPassResult;
    }

    public void forgotPass(String username) {
        // can be launched in a separate asynchronous job
        Result<String> result = forgotPassRepository.forgotPass(username);

//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
    }

    public void forgotPassDataChanged(String username) {
        if (!isUserNameValid(username)) {
            forgotFormState.setValue(new ForgotPassFormState(R.string.invalid_username));
        }  else {
            forgotFormState.setValue(new ForgotPassFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false;
        }
    }
}
