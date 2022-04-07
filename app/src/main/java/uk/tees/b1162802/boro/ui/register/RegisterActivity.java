package uk.tees.b1162802.boro.ui.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Calendar;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.databinding.ActivityRegisterBinding;
import uk.tees.b1162802.boro.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextInputEditText emailEditText,ageEditText,fullnameEditText,mDateFormat,passwordEditText;
    TextInputLayout nameLayout,emailLayout,ageLayout,genderLayout,dateLayout,passwordEditLayout;
    TextView loginTextView;
    FloatingActionButton registerBtn;
    ProgressBar loadingProgress;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        loginTextView = binding.loginText;

        loginTextView.setOnClickListener(this);
        final String[] genders = getResources().getStringArray(R.array.gender_value);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.dropdown_item,genders);
        binding.gender.setAdapter(arrayAdapter);
        mDateFormat = binding.datepicker;
        mDateFormat.setOnClickListener(this);
        emailEditText = binding.username;
        ageEditText = binding.age;
        passwordEditText = binding.password;
        passwordEditLayout = binding.passwordTextField;
        fullnameEditText = binding.fullname;
        nameLayout = binding.fullNameTextField;
        emailLayout = binding.usernameTextField;
        ageLayout = binding.ageTextField;
        genderLayout = binding.genderTextField;
        dateLayout = binding.dateTextField;
        registerBtn = binding.register;
        loadingProgress = binding.loading;

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                mDateFormat.setText(date);
            }
        };

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                //Log.i(TAG, "onChanged: "+loginFormState.isDataValid());
                registerBtn.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getNameError() != null) {
                    nameLayout.setError(getString(registerFormState.getNameError()));
                }else{
                    nameLayout.setError(null);
                }
                if (registerFormState.getEmailError() != null) {
                    emailLayout.setError(getString(registerFormState.getEmailError()));
                }else{
                    emailLayout.setError(null);
                }
                if (registerFormState.getAgeError() != null) {
                    ageLayout.setError(getString(registerFormState.getAgeError()));
                }else{
                    ageLayout.setError(null);
                }
                if (registerFormState.getGenderError() != null) {
                    genderLayout.setError(getString(registerFormState.getGenderError()));
                }else{
                    genderLayout.setError(null);
                }
                if (registerFormState.getDateError() != null) {
                    dateLayout.setError(getString(registerFormState.getDateError()));
                }else{
                    dateLayout.setError(null);
                }


            }
        });

//        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
//            @Override
//            public void onChanged(@Nullable RegisterResult registerResult) {
//                if (registerResult == null) {
//                    return;
//                }
//                loadingProgress.setVisibility(View.GONE);
//                if (registerResult.getError() != null) {
//                    showRegisterFailed(registerResult.getError());
//                }
//                if (registerResult.getSuccess() != null) {
//                    updateUiWithUser(registerResult.getSuccess());
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
                registerViewModel.registerDataChanged(emailLayout.getEditText().getText().toString(),
                        nameLayout.getEditText().getText().toString(),ageLayout.getEditText().getText().toString(),genderLayout.getEditText().getText().toString(),dateLayout.getEditText().getText().toString());
            }
        };
        fullnameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        ageEditText.addTextChangedListener(afterTextChangedListener);
        mDateFormat.addTextChangedListener(afterTextChangedListener);

    }

    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), model, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loadingProgress.setVisibility(View.VISIBLE);
                registerBtn.setVisibility(View.INVISIBLE);
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    String welcome = "Already logged in";
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    finish();;
                }
                mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
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
                                    loadingProgress.setVisibility(View.GONE);
                                    registerBtn.setVisibility(View.VISIBLE);
//                                    showRegisterFailed("Login Failed.Could not find user or password");

                                }
                            }
                        });
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                break;
            case R.id.loginText:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.datepicker:
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                        onDateSetListener,year,month,day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }
}
