package uk.tees.b1162802.boro.ui.updatePass;

import androidx.annotation.Nullable;

public class UpdatePassFormState {
    @Nullable
    private Integer oldPassError;
    @Nullable
    private Integer newPassError;
    @Nullable
    private Integer confirmPassError;
    @Nullable
    private boolean isDataValid;

    UpdatePassFormState(@Nullable Integer oldPassError, @Nullable Integer newPassError, @Nullable Integer confirmPassError) {
        this.oldPassError = oldPassError;
        this.newPassError = newPassError;
        this.confirmPassError = confirmPassError;
        this.isDataValid = false;
    }

    UpdatePassFormState(boolean isDataValid) {
        this.oldPassError = null;
        this.newPassError = null;
        this.confirmPassError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getOldPassError() {
        return oldPassError;
    }

    @Nullable
    public Integer getNewPassError() {
        return newPassError;
    }

    @Nullable
    public Integer getConfirmPassError() {
        return confirmPassError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
