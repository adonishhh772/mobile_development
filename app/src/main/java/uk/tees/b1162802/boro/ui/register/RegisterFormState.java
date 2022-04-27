package uk.tees.b1162802.boro.ui.register;

import androidx.annotation.Nullable;
/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer genderError;
    @Nullable
    private Integer addressError;
    @Nullable
    private Integer ageError;
    @Nullable
    private Integer dateError;
    @Nullable
    private Integer mobileError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer nameError,
                      @Nullable Integer genderError, @Nullable Integer ageError, @Nullable Integer addressError,
                      @Nullable Integer dateError, @Nullable Integer mobileError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.nameError = nameError;
        this.genderError = genderError;
        this.ageError = ageError;
        this.addressError = addressError;
        this.dateError = dateError;
        this.mobileError = mobileError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.nameError = null;
        this.genderError = null;
        this.ageError = null;
        this.addressError = null;
        this.dateError = null;
        this.mobileError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getGenderError() {
        return genderError;
    }

    @Nullable
    Integer getMobileError() {
        return mobileError;
    }
    @Nullable
    Integer getAddressError() {
        return addressError;
    }

    @Nullable
    Integer getAgeError() {
        return ageError;
    }

    @Nullable
    Integer getDateError() {
        return dateError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
