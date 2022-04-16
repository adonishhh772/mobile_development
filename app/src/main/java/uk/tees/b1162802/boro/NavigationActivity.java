package uk.tees.b1162802.boro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

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

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView viewProfile;
    boolean isChildFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                }
                break;
            case R.id.showProfile:
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }
}