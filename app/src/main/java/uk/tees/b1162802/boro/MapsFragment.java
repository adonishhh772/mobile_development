package uk.tees.b1162802.boro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import uk.tees.b1162802.boro.Adapter.NewsAdapter;
import uk.tees.b1162802.boro.data.model.News;
import uk.tees.b1162802.boro.features.Gestures;

public class MapsFragment extends Fragment {
    Location currentLocation;
    private GoogleMap mMap;
    final String weather = "https://api.openweathermap.org/data/2.5/weather";
    final String appId = "796b0a4413bdb03648e421f1378f4019";
    private Marker marker;
    DecimalFormat df = new DecimalFormat("#,##");
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    SharedPreferences sharedPreferences;
    LinearLayout bottomSheet;
    String username;
    ListView listView;
    TextView placesName, noNews, weatherNews;
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
            mMap = googleMap;
            LatLng latLng = new LatLng(40.7128,74.0060);
//            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(false).title("I am here!");
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            marker = mMap.addMarker(markerOptions);
            getGoecoderAddress(latLng);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    marker.remove();
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true).title("I am here!");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    marker = mMap.addMarker(markerOptions);
                    getGoecoderAddress(latLng);
                }
            });
        }
    };

    private void getGoecoderAddress(LatLng latLng){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            placesName.setText(add);
            getNewsOfLocality(add);
            getWeatherofLocality(latLng);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getWeatherofLocality(LatLng latLng) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = weather+"?lat="+latLng.latitude+"&lon="+latLng.longitude+"&appid="+appId;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject weatherObj = jsonArray.getJSONObject(0);
                            String description = weatherObj.getString("description");
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                            float pressure = jsonObjectMain.getInt("pressure");
                            int humidity = jsonObjectMain.getInt("humidity");
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            JSONObject cloudObj = jsonObject.getJSONObject("clouds");
                            String clouds = cloudObj.getString("all");

                            String output =
                                    "Temp: "+ df.format(temp) + " °C"
                                    +", Feels Like: "+df.format(feelsLike)+ " °C"
                                    +"\n Humidity: "+humidity+" %"
                                    +"\n Description: "+description
                                    +"\n Wind Speed: "+wind+" m/s (meters per second)"
                                    +"\n Cloudliness: "+clouds+" %"
                                    +", Pressure: "+ pressure + " hPa";
                            weatherNews.setText(output);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getNewsOfLocality(String address) throws UnsupportedEncodingException {
        String[] splitAddress = address.split(",");
        String query = URLEncoder.encode(splitAddress[0], "utf-8");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://news.google.com/rss/search?q="+query;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<News> arrayList= new ArrayList<>();
                            JSONObject jsonObject = XML.toJSONObject(response);
                            JSONObject jsonRss = jsonObject.getJSONObject("rss");
                            JSONObject jsonChannel = jsonRss.getJSONObject("channel");
                            JSONArray jsonArray = jsonChannel.getJSONArray("item");
                                for (int i = 0; i < jsonArray.length();i++){
                                   JSONObject itemObject = jsonArray.getJSONObject(i);
                                   String title = itemObject.getString("title");
                                   String url = itemObject.getString("link");
                                   arrayList.add(new News(title,url));

                                }

                                listView.setVisibility(View.VISIBLE);
                            noNews.setVisibility(View.GONE);
                            NewsAdapter newsAdapter = new NewsAdapter(getContext(),R.layout.news_row,arrayList);
                            listView.setAdapter(newsAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selected = ((TextView) view.findViewById(R.id.news_url)).getText().toString();
                                    Uri webpage = Uri.parse(selected);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//                                    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                        startActivity(intent);
//                                    }
                                }
                            });

                        } catch (JSONException e) {
                            listView.setVisibility(View.GONE);
                            noNews.setVisibility(View.VISIBLE);
                            e.printStackTrace();
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final TextView greetingText =  view.findViewById(R.id.greetings);
        final ImageView imageView = view.findViewById(R.id.iconGreeting);
        placesName = view.findViewById(R.id.placeName);
        listView = view.findViewById(R.id.newsList);
        noNews = view.findViewById(R.id.noNews);
        weatherNews = view.findViewById(R.id.weatherInfo);
        username = sharedPreferences.getString("username","Boro Service Provider");
        String[] splited = username.split("\\s+");
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


        // get the gesture detector
        mDetector = new GestureDetector(this.getActivity(), new Gestures());


            bottomSheet= getView().findViewById(R.id.bottomsheet);


            sheetBehavior = BottomSheetBehavior.from(bottomSheet);

            sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                    switch(newState){
//                        case BottomSheetBehavior.STATE_COLLAPSED:
//                            sheetBehavior.setPeekHeight(92);
//                            break;
//                         case BottomSheetBehavior.STATE_EXPANDED:
//                            sheetBehavior.setPeekHeight(400);
//                            //then do your other staff here
//                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(callback);
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
}
