package uk.tees.b1162802.boro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

import uk.tees.b1162802.boro.features.BroadCastReceiver;
import uk.tees.b1162802.boro.features.Gestures;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    Calendar c = Calendar.getInstance();
    private GestureDetector mDetector;
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        registerReceiver(registerReceiver, intentFilter);
        showEditDialog();

        // get the gesture detector
        mDetector = new GestureDetector(this, new Gestures());

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_activity, new MapsFragment()).commit();

    }



    private void showEditDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.search_modal_fragment,
                (LinearLayout) findViewById(R.id.bottomsheet)

        );
       final TextView greetingText = (TextView) bottomSheetView.findViewById(R.id.greetings);
       final ImageView imageView = (ImageView) bottomSheetView.findViewById(R.id.iconGreeting);


        if(timeOfDay >= 0 && timeOfDay < 12){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.daytime_logo_foreground));
            greetingText.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.daytime_logo_foreground));
            greetingText.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.night_logo_foreground));
            greetingText.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.night_logo_foreground));
            greetingText.setText("Good Night");
        }


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

