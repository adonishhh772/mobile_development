package uk.tees.b1162802.boro.ui.editProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.tees.b1162802.boro.R;

public class EditProfileViewModel extends ViewModel {
    private MutableLiveData<EditProfileFormState> editProfileFormState = new MutableLiveData<>();

    public LiveData<EditProfileFormState> getEditProfileFormState() {
        return editProfileFormState;
    }


    public void editProfileChanged(String mobile, String address, String age, String gender, String date) {
        if (!isMobileValid(mobile)) {
            editProfileFormState.setValue(new EditProfileFormState(R.string.invalid_number,null,null,null,null));
        } else if (!isAddressValid(address)) {
            editProfileFormState.setValue(new EditProfileFormState(null,R.string.invalid_address,null,null,null));
        } else if (!isAgeValid(age)) {
            editProfileFormState.setValue(new EditProfileFormState(null,null,R.string.invalid_age,null,null));
        }else if (!isGenderValid(gender)) {
            editProfileFormState.setValue(new EditProfileFormState(null,null,null,R.string.invalid_gender,null));
        }else if (!isDateValid(date)) {
            editProfileFormState.setValue(new EditProfileFormState(null,null,null,null,R.string.invalid_date));
        } else {
            editProfileFormState.setValue(new EditProfileFormState(true));
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

    //A placeholder mobile validation check
    private boolean isMobileValid(String phone) {
        return phone != null && phone.trim().length() == 11;
    }

    // A placeholder address validation check
    private boolean isAddressValid(String address) {
        return address != null && !address.isEmpty();
    }

}
