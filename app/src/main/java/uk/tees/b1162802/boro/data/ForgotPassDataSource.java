package uk.tees.b1162802.boro.data;

import java.io.IOException;


public class ForgotPassDataSource {

    public Result<String> forgotPass(String username) {

        try {
            // TODO: handle loggedInUser authentication
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
            return new Result.Success<>("Send email to the email to recover the password");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while sending request", e));
        }
    }
}

