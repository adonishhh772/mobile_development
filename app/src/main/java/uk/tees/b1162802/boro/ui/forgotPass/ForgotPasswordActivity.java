package uk.tees.b1162802.boro.ui.forgotPass;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import uk.tees.b1162802.boro.R;

import uk.tees.b1162802.boro.databinding.ForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ForgotPassViewModel forgotPassViewModel;
    private ForgotPasswordBinding binding;
    TextInputEditText emailEditText;
    TextInputLayout emailLayout;
    FloatingActionButton resetButton;
    ProgressBar loadingProgressBar;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        binding = ForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        forgotPassViewModel = new ViewModelProvider(this, new ForgotPasswordViewModelFactory())
                .get(ForgotPassViewModel.class);
        emailEditText = binding.email;
        emailLayout = binding.emailTextField;
        resetButton = binding.reset;
        loadingProgressBar = binding.loading;
        forgotPassViewModel.getForgotPassFormState().observe(this, new Observer<ForgotPassFormState>() {
            @Override
            public void onChanged(@Nullable ForgotPassFormState forgotPassFormState) {
                if (forgotPassFormState == null) {
                    return;
                }
                //Log.i(TAG, "onChanged: "+loginFormState.isDataValid());
                resetButton.setEnabled(forgotPassFormState.isDataValid());
                if (forgotPassFormState.getUsernameError() != null) {
                    emailLayout.setError(getString(forgotPassFormState.getUsernameError()));
                }else{
                    emailLayout.setError(null);
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
                forgotPassViewModel.forgotPassDataChanged(emailLayout.getEditText().getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);


        resetButton.setOnClickListener(this);

    }

    private void updateUiWithUser(String model, View v) {
        // TODO : initiate successful logged in experience
        Snackbar.make(v,
                model,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void showResetFailed(String errorString, View v) {
        Snackbar.make(v,
                errorString,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        resetButton.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                resetButton.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    updateUiWithUser("Email to Password Reset has been sent Successfully",v);
                }else{
                    showResetFailed(task.getException().getMessage(),v);
                }
            }
        });
    }
}
