package uk.tees.b1162802.boro.ui.editProfile;

import androidx.annotation.Nullable;

public class EditProfileFormState {

    @Nullable
    private Integer mobileError;
    @Nullable
    private Integer addressError;
    @Nullable
    private Integer ageError;
    @Nullable
    private Integer genderError;
    @Nullable
    private Integer birthdayError;
    @Nullable
    private boolean isDataValid;

    EditProfileFormState(@Nullable Integer mobileError, @Nullable Integer addressError, @Nullable Integer ageError, @Nullable Integer genderError, @Nullable Integer birthdayError) {
        this.mobileError = mobileError;
        this.addressError = addressError;
        this.ageError = ageError;
        this.genderError = genderError;
        this.birthdayError = birthdayError;
        this.isDataValid = false;
    }

    EditProfileFormState(boolean isDataValid) {
        this.mobileError = null;
        this.addressError = null;
        this.ageError = null;
        this.genderError = null;
        this.birthdayError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getMobileError() {
        return mobileError;
    }

    @Nullable
    public Integer getAddressError() {
        return addressError;
    }

    @Nullable
    public Integer getAgeError() {
        return ageError;
    }

    @Nullable
    public Integer getGenderError() {
        return genderError;
    }

    @Nullable
    public Integer getBirthdayError() {
        return birthdayError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
