package uk.tees.b1162802.boro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.tees.b1162802.boro.ui.login.LoginActivity;
import uk.tees.b1162802.boro.ui.register.RegisterActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView logo;
    CardView service, provider;
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
        logo = findViewById(R.id.imageView);
        service = findViewById(R.id.cardView);
        provider = findViewById(R.id.cardView1);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

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

    private void changeRoute() {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
            Intent mainIntent = new Intent(this, NavigationActivity.class);
            startActivity(mainIntent);
            return;
//        }
//        logo.setAnimation(null);
//        service.setAnimation(null);
//        provider.setAnimation(null);
//        login.setVisibility(View.VISIBLE);
//        register.setVisibility(View.VISIBLE);

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

