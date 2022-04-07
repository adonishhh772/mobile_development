package uk.tees.b1162802.boro.ui.forgotPass;

import androidx.annotation.Nullable;

class ForgotPassFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private boolean isDataValid;

    ForgotPassFormState(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
        this.isDataValid = false;
    }

    ForgotPassFormState(boolean isDataValid) {
        this.usernameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}