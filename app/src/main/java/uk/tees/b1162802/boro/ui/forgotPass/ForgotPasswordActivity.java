package uk.tees.b1162802.boro.ui.forgotPass;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import uk.tees.b1162802.boro.R;

import uk.tees.b1162802.boro.databinding.ForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ForgotPassViewModel forgotPassViewModel;
    private ForgotPasswordBinding binding;
    TextInputEditText emailEditText;
    TextInputLayout emailLayout;
    FloatingActionButton resetButton;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        forgotPassViewModel.getForgotPassResult().observe(this, new Observer<ForgotPassResult>() {
            @Override
            public void onChanged(@Nullable ForgotPassResult forgotPassResult) {
                if (forgotPassResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (forgotPassResult.getError() != null) {
                    showResetFailed(forgotPassResult.getError());
                }
                if (forgotPassResult.getSuccess() != null) {
                    updateUiWithUser(forgotPassResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
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


    }

    private void updateUiWithUser(ForgotPasswordUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplay();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showResetFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
