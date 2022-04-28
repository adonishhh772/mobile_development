package uk.tees.b1162802.boro.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uk.tees.b1162802.boro.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    LoggedInUser user_;
    String successMsg = "Error Logging In";
   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

    public Result<LoggedInUser> login(String userID,String username, String password) {
        try {
            final
            Query checkUser = reference.orderByChild("username").equalTo(username);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String passwordFromDB = snapshot.child(userID).child("password").getValue(String.class);
                        if(passwordFromDB.equals(password)){
                            user_ = new LoggedInUser(
                                    userID,
                                    snapshot.child(userID).child("username").getValue(String.class),
                                    snapshot.child(userID).child("password").getValue(String.class),
                                    snapshot.child(userID).child("mobile").getValue(String.class),
                                    snapshot.child(userID).child("birthday").getValue(String.class),
                                    snapshot.child(userID).child("gender").getValue(String.class),
                                    snapshot.child(userID).child("age").getValue(String.class),
                                    snapshot.child(userID).child("fullname").getValue(String.class)

                            );
//                            user_.put("uid",userID);
//                            user_.put("username",snapshot.child(userID).child("username").getValue(String.class));
//                            user_.put("password",snapshot.child(userID).child("password").getValue(String.class));
//                            user_.put("mobile",snapshot.child(userID).child("mobile").getValue(String.class));
//                            user_.put("birthday",snapshot.child(userID).child("birthday").getValue(String.class));
//                            user_.put("gender",snapshot.child(userID).child("gender").getValue(String.class));
//                            user_.put("provider",snapshot.child(userID).child("isProvider").getValue(String.class));
//                            user_.put("age",snapshot.child(userID).child("age").getValue(String.class));
//                            user_.put("name",snapshot.child(userID).child("fullname").getValue(String.class));
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    successMsg = error.getMessage();
                }
            });

            // TODO: handle loggedInUser authentication
            LoggedInUser user = new LoggedInUser(
                    userID,
                    username,
                    password,
                    "",
                    "",
                    "",
                    "",
                    ""
            );


            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException(successMsg, e));
        }
    }

}