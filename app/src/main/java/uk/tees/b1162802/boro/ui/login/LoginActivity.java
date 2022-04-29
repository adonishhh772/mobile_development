package uk.tees.b1162802.boro.ui.login;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uk.tees.b1162802.boro.NavigationActivity;
import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.data.model.LoggedInUser;
import uk.tees.b1162802.boro.ui.forgotPass.ForgotPasswordActivity;
import uk.tees.b1162802.boro.databinding.ActivityLoginBinding;
import uk.tees.b1162802.boro.ui.register.RegisterActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    String userID;
    private FirebaseAuth mAuth;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "settingpref";
    TextInputEditText usernameEditText,passwordEditText;
    TextInputLayout passwordLayout,usernameLayout;
    TextView forgotTextView,registerTextView;
    FloatingActionButton loginButton;
    ProgressBar loadingProgressBar;
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = (TextInputEditText) binding.username;
        passwordEditText = (TextInputEditText) binding.password;
        passwordLayout = binding.passwordTextField;
        usernameLayout = binding.usernameTextField;
        forgotTextView = binding.forgotView;
        registerTextView = binding.registerUser;
        loginButton = (FloatingActionButton) binding.login;
        loginButton.setEnabled(false);
        loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                //Log.i(TAG, "onChanged: "+loginFormState.isDataValid());
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameLayout.setError(getString(loginFormState.getUsernameError()));
                }else{
                    usernameLayout.setError(null);
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordLayout.setError(getString(loginFormState.getPasswordError()));
                }else{
                    passwordLayout.setError(null);
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameLayout.getEditText().getText().toString(),
                        passwordLayout.getEditText().getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        loginButton.setOnClickListener(this);
        forgotTextView.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
    }

    private void getUserDetails() {
        final Query checkUser = reference.orderByChild("username").equalTo(usernameLayout.getEditText().getText().toString());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passwordFromDB = snapshot.child(userID).child("password").getValue(String.class);
                    if(passwordFromDB.equals(passwordLayout.getEditText().getText().toString())){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", snapshot.child(userID).child("fullname").getValue(String.class));
                        editor.apply();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome) + model;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", model);
        editor.putBoolean("isLogged", true);
        editor.apply();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(this, NavigationActivity.class);
        startActivity(mainIntent);
    }

    private void showLoginFailed(View view,String errorString) {
        Snackbar.make(view,
                errorString,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
                mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    userID = user.getUid();
                                    editor.putString("userID", user.getUid());
                                    editor.putString("username", user.getDisplayName());
                                    editor.apply();
                                    getUserDetails();
//                                    loginViewModel.login(user.getUid(),usernameEditText.getText().toString(),
//                                            passwordEditText.getText().toString());
                                    updateUiWithUser(user.getEmail());
                                } else {
                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    loadingProgressBar.setVisibility(View.GONE);
                                    loginButton.setVisibility(View.VISIBLE);
                                    showLoginFailed(v,"Login Failed.Could not find user or password");

                                }
                            }
                        });

                break;
            case R.id.forgotView:
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
                break;
            case R.id.registerUser:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            default:
                break;
        }
    }
}