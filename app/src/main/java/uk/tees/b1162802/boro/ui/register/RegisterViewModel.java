package uk.tees.b1162802.boro.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.data.RegisterRepository;
import uk.tees.b1162802.boro.data.Result;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username) {
        // can be launched in a separate asynchronous job
        Result<String> result = registerRepository.register(username);

        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void registerDataChanged(String username, String name, String age, String gender, String date) {
        if (!isEmailValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null,null,null,null));
        } else if (!isNameValid(name)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_name,null,null,null));
        } else if (!isAgeValid(age)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,R.string.invalid_age,null));
        }else if (!isGenderValid(gender)) {
            registerFormState.setValue(new RegisterFormState(null,null,R.string.invalid_gender,null,null));
        }else if (!isDateValid(date)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,null,R.string.invalid_date));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder date validation check
    private boolean isDateValid(String date) {
        return date != null && !date.isEmpty();
    }

    // A placeholder gender validation check
    private boolean isGenderValid(String gender) {
        return gender != null && !gender.isEmpty();
    }

    // A placeholder age validation check
    private boolean isAgeValid(String age) {
        return age != null && !age.isEmpty();
    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    // A placeholder name validation check
    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
    }
}
