package uk.tees.b1162802.boro.ui.forgotPass;

import androidx.annotation.Nullable;

 class ForgotPassResult {
    @Nullable
    private ForgotPasswordUserView success;
    @Nullable
    private Integer error;

     ForgotPassResult(@Nullable Integer error) {
        this.error = error;
    }

     ForgotPassResult(@Nullable ForgotPasswordUserView success) {
        this.success = success;
    }

    @Nullable
    ForgotPasswordUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
