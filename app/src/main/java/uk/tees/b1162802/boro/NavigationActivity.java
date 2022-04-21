package uk.tees.b1162802.boro;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import uk.tees.b1162802.boro.databinding.ActivityNavigationBinding;
import uk.tees.b1162802.boro.features.BroadCastReceiver;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView viewProfile;
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
        ///broadcast intent filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(registerReceiver, intentFilter);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        viewProfile = binding.navView.getHeaderView(0).findViewById(R.id.showProfile);
        binding.appBarNavigation.fab.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_service, R.id.nav_favourites, R.id.nav_setting)
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

//                Log.i("TAG", "onDestinationChanged: "+navDestination.getLabel());
//
            }
        } );
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
        }
    }
}