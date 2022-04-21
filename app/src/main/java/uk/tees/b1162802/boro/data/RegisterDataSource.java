package uk.tees.b1162802.boro.data;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.tees.b1162802.boro.data.model.LoggedInUser;
import uk.tees.b1162802.boro.ui.register.RegisterActivity;

public class RegisterDataSource {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    public Result<LoggedInUser> register(Map<String, String> register) {
        try {
            String phone = register.get("mobile");
            root.child(phone).setValue(register);
            LoggedInUser user =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            register.get("username"),
                            register.get("password"),
                            register.get("mobile"),
                            register.get("birthday"),
                            register.get("gender"),
                            register.get("isProvider") == "true" ? true : false,
                            register.get("age"),
                            register.get("fullname"));
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error Registering", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
