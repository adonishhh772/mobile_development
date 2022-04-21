package uk.tees.b1162802.boro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import uk.tees.b1162802.boro.ui.login.LoginActivity;
import uk.tees.b1162802.boro.ui.register.RegisterActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "settingpref";
    ImageView logo;
    CardView service, provider;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;
    MaterialButton login, register;
    final Handler handler = new Handler();
    private FirebaseAuth mAuth;
    private String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        View splashLayout = findViewById(R.id.splash_scre);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        logo = findViewById(R.id.imageView);
        service = findViewById(R.id.cardView);
        provider = findViewById(R.id.cardView1);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        boolean isNightEnabled = sharedPreferences.getBoolean("night_mode",false);
        boolean isPrivacyEnabled = sharedPreferences.getBoolean("finger_print",false);
        if(isPrivacyEnabled){
            executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);

                    Snackbar.make(splashLayout,"Error while authenticating",Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                    Snackbar.make(splashLayout,"Failure while authenticating",Snackbar.LENGTH_LONG).show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Boro Authentication").setNegativeButtonText("Use Password").setConfirmationRequired(false).build();
            authenticateBtn(splashLayout);

        }
        if(isNightEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


        initializeSplash();
    }

    private void initializeSplash() {
        logo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.flickering));
        service.setAnimation(AnimationUtils.loadAnimation(this, R.anim.flickering));
        provider.setAnimation(AnimationUtils.loadAnimation(this, R.anim.flickering));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               changeRoute();
            }
        }, 5000);






    }

    public void authenticateBtn(View view){
        BiometricManager biometricManager = BiometricManager.from(this);
        if(biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS){
            Snackbar.make(view,"Biometrics not supported",Snackbar.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("finger_print",false);
            editor.apply();
            initializeSplash();
            return;
        }

        biometricPrompt.authenticate(promptInfo);
    }

    private void changeRoute() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent mainIntent = new Intent(this, NavigationActivity.class);
            startActivity(mainIntent);
            return;
        }
        logo.setAnimation(null);
        service.setAnimation(null);
        provider.setAnimation(null);
        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.register_btn:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            default:
                break;
        }

    }
}

