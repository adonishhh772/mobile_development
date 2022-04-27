package uk.tees.b1162802.boro.data;

import android.content.Context;
import android.util.Log;
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
                            registerDetail.get("isProvider") == "true" ? true : false,
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
