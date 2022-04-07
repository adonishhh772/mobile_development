package uk.tees.b1162802.boro.data;

import java.io.IOException;

import uk.tees.b1162802.boro.data.model.LoggedInUser;

public class RegisterDataSource {
    public String url = "https://boro-service-provider-default-rtdb.firebaseio.com/users/";

    public Result<String> register(String username) {

        try {
            // TODO: handle loggedInUser authentication
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
            return new Result.Success<>("REgister Success");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
