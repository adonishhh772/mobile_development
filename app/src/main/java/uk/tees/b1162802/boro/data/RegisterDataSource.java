package uk.tees.b1162802.boro.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.Map;

import uk.tees.b1162802.boro.data.model.LoggedInUser;

public class RegisterDataSource {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    public Result<LoggedInUser> register(String uid,Map<String, String> registerDetail) {
        try {
            root.child(uid).setValue(registerDetail);
            LoggedInUser user =
                    new LoggedInUser(
                            uid,
                            registerDetail.get("username"),
                            registerDetail.get("password"),
                            registerDetail.get("mobile"),
                            registerDetail.get("birthday"),
                            registerDetail.get("gender"),
                            registerDetail.get("age"),
                            registerDetail.get("fullname"));
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error Registering", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
