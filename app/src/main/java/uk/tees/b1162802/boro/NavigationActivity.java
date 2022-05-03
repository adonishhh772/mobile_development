package uk.tees.b1162802.boro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.tees.b1162802.boro.databinding.ActivityNavigationBinding;
import uk.tees.b1162802.boro.features.BroadCastReceiver;
import uk.tees.b1162802.boro.ui.login.LoginActivity;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "settingpref";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Category");
    DrawerLayout drawer;
    FirebaseStorage storage;
    String userID;
    TextInputEditText placesLayout;
    String username;
    ProgressBar loadingBar;
    Dialog dialog;
    RelativeLayout logoutLayout;
    StorageReference storageReference;
    NavigationView navigationView;
    TextView viewProfile, showUsername;
    ImageView profilePicView;
    boolean isChildFragment = false;
    private BroadCastReceiver registerReceiver = new BroadCastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getBooleanExtra("state",false)){
                Toast.makeText(context,"Airplane Mode ON", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"Airplane Mode OFF", Toast.LENGTH_LONG).show();
            }
            //super.onReceive(context, intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getApplicationContext(),"AIzaSyBMStu_gyyaySZgZyoH3Xk-rXirVwTMdEA");
        ///broadcast intent filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(registerReceiver, intentFilter);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("email","Boro Service Provider");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userID = sharedPreferences.getString("userID","Boro Service Provider");
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        viewProfile = binding.navView.getHeaderView(0).findViewById(R.id.showProfile);
        showUsername = binding.navView.getHeaderView(0).findViewById(R.id.showUsername);
        profilePicView = binding.navView.getHeaderView(0).findViewById(R.id.showImage);
        logoutLayout = binding.navView.getHeaderView(0).findViewById(R.id.logout);
        getProfileImage();
        binding.appBarNavigation.fab.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
        showUsername.setText(username);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_favourites, R.id.nav_setting, R.id.nav_terms,R.id.nav_privacy)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        navController.addOnDestinationChangedListener( new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getLabel() != null){
                    isChildFragment = true;
                    binding.appBarNavigation.fab.setImageResource(R.drawable.ic_baseline_close_24);
                }
            }
        } );
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }



    private void getProfileImage() {
        try{
            final File localFile = File.createTempFile(userID+"_profile_pic","");
            storageReference.child("images/"+userID+"_profile_pic").getFile(localFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            updateUiWithUser("Retrieved",false);
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePicView.setImageBitmap(bitmap);
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showUpdateFailed(e.getMessage());
                }
            });
        }catch (IOException e){
            showUpdateFailed(e.getMessage());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:

                if(!isChildFragment){
                    drawer.openDrawer(Gravity.LEFT);
                }else{
                    Intent intent = new Intent(this, NavigationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                break;
            case R.id.showProfile:
                Intent intent = new Intent(this,ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                break;
            case R.id.logout:
                sharedPreferences.edit().clear().commit();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

    private void showUpdateFailed(String errorString) {
        Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
        toast.show();
    }
}