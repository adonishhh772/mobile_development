package uk.tees.b1162802.boro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import uk.tees.b1162802.boro.features.Gestures;

public class MapsFragment extends Fragment implements View.OnClickListener {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    SharedPreferences sharedPreferences;
    LinearLayout bottomSheet;
    String username;
    RelativeLayout setHome, setWork;
    private static final String SHARED_PREF_NAME = "settingpref";
    Calendar c = Calendar.getInstance();
    BottomSheetBehavior sheetBehavior;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Category");
    private GestureDetector mDetector;
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            googleMap.addMarker(markerOptions);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final TextView greetingText =  view.findViewById(R.id.greetings);
        final ImageView imageView = view.findViewById(R.id.iconGreeting);
        final CardView searchProviders = view.findViewById(R.id.showProvider);
        username = sharedPreferences.getString("username","Boro Service Provider");
        String[] splited = username.split("\\s+");
        searchProviders.setOnClickListener(this);
       if (timeOfDay >= 0 && timeOfDay < 12) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.daytime_logo_foreground));
            String greeting = "Good Morning "+ splited[0];
            greetingText.setText(greeting);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.daytime_logo_foreground));
            String greeting = "Good Afternoon "+ splited[0] ;
            greetingText.setText(greeting);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.night_logo_foreground));
            String greeting = "Good Evening "+ splited[0];
            greetingText.setText(greeting);
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.night_logo_foreground));
            String greeting = "Good Night "+ splited[0];
            greetingText.setText(greeting);

        }

       setHome = view.findViewById(R.id.setHomeAddress);
       setWork = view.findViewById(R.id.setWorkAddress);

        // get the gesture detector
        mDetector = new GestureDetector(this.getActivity(), new Gestures());

        // Add a touch listener to the view
        // The touch listener passes all its events on to the gesture detector
        setHome.setOnTouchListener(touchListener);
        setWork.setOnTouchListener(touchListener);

            bottomSheet= getView().findViewById(R.id.bottomsheet);


            sheetBehavior = BottomSheetBehavior.from(bottomSheet);

            sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });

//            if (mapFragment != null) {
////            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                fetchLocation();
////            }
//
//            }


        }

    // This touch listener passes everything on to the gesture detector.
    // That saves us the trouble of interpreting the raw touch events
    // ourselves.
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mDetector.onTouchEvent(event);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        Toast.makeText(getContext(), "FFFFFF", Toast.LENGTH_SHORT).show();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i("TAG", "fetchLocation: "+location);
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment =
                            (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(callback);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.showProvider:
                break;
        }
    }
}
