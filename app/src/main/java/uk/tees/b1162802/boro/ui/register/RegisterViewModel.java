package uk.tees.b1162802.boro.ui.register;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.data.RegisterRepository;
import uk.tees.b1162802.boro.data.Result;
import uk.tees.b1162802.boro.data.model.LoggedInUser;

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

    public void register(String uid,Map<String,String> _data) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = registerRepository.register(uid,_data);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisterUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.register_failed));
        }
    }

    public void registerDataChanged(String username,String password, String name, String age, String gender, String date, String address, String phone) {
        if (!isEmailValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null,null,null,null,null,null,null));
        }else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password,null,null,null,null,null,null));
        } else if (!isNameValid(name)) {
            registerFormState.setValue(new RegisterFormState(null,null, R.string.invalid_name,null,null,null,null,null));
        }else if (!isMobileValid(phone)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,null,null,null,null,R.string.invalid_number));
        } else if (!isAddressValid(address)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,null,null,R.string.invalid_address,null,null));
        }else if (!isAgeValid(age)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,null,R.string.invalid_age,null,null,null));
        }else if (!isGenderValid(gender)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,R.string.invalid_gender,null,null,null,null));
        }else if (!isDateValid(date)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,null,null,R.string.invalid_date,null,null));
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

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isMobileValid(String phone) {
        return phone != null && phone.trim().length() == 11;
    }

    // A placeholder name validation check
    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
    }
    // A placeholder name validation check
    private boolean isAddressValid(String address) {
        return address != null && !address.isEmpty();
    }
}
