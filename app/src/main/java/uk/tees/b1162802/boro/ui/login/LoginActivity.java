package uk.tees.b1162802.boro.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.ui.forgotPass.ForgotPasswordActivity;
import uk.tees.b1162802.boro.ui.login.LoginViewModel;
import uk.tees.b1162802.boro.ui.login.LoginViewModelFactory;
import uk.tees.b1162802.boro.databinding.ActivityLoginBinding;
import uk.tees.b1162802.boro.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
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
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = (TextInputEditText) binding.username;
        passwordEditText = (TextInputEditText) binding.password;
        passwordLayout = binding.passwordTextField;
        usernameLayout = binding.usernameTextField;
        forgotTextView = binding.forgotView;
        registerTextView = binding.registerText;
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

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });

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

    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome) + model.toString();
        // TODO : initiate successful logged in experience
//        Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
//        startActivity(i);
//        finish();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
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
                                    updateUiWithUser(user.toString());
                                } else {
                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    loadingProgressBar.setVisibility(View.GONE);
                                    loginButton.setVisibility(View.VISIBLE);
                                    showLoginFailed("Login Failed.Could not find user or password");

                                }
                            }
                        });
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                break;
            case R.id.forgotView:
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
                break;
            case R.id.registerText:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            default:
                break;
        }
    }
}