package uk.tees.b1162802.boro.ui.updatePass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.tees.b1162802.boro.R;

public class UpdatePassViewModel extends ViewModel {

    private MutableLiveData<UpdatePassFormState> updatePassFormState = new MutableLiveData<>();

    public LiveData<UpdatePassFormState> getUpdatePassFormState() {
        return updatePassFormState;
    }


    public void passDataChanged(String oldPass, String newPass, String confirmPass) {
       if (!isPasswordValid(oldPass)) {
            updatePassFormState.setValue(new UpdatePassFormState(R.string.invalid_password,null,null));
        } else if (!isNewPasswordValid(newPass)) {
            updatePassFormState.setValue(new UpdatePassFormState(null,R.string.invalid_password,null));
        } else if (!isconfirmPasswordValid(newPass,confirmPass)) {
            updatePassFormState.setValue(new UpdatePassFormState(null,null,R.string.retype_invalid));
        } else {
            updatePassFormState.setValue(new UpdatePassFormState(true));
        }
    }

    private boolean isNewPasswordValid(String newPass) {
        return newPass != null && newPass.trim().length() > 5;
    }

    private boolean isconfirmPasswordValid(String newPass, String confirmPass) {
        if(confirmPass.equals(newPass)){
            return true;
        }else{
            return false;
        }

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
