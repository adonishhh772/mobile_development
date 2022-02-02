package uk.tees.b1162802.abastola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton start, stop, pause;
    private TextView hours,seconds,minutes;
    private CountDownTimer countTimer;
    private int hoursText = 0;
    private int secondsText = 0;
    private int minutesText = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (FloatingActionButton) findViewById(R.id.start);
        stop = (FloatingActionButton) findViewById(R.id.stop);
        pause = (FloatingActionButton) findViewById(R.id.pause);
        hours = (TextView) findViewById(R.id.hours);
        seconds = (TextView) findViewById(R.id.seconds);
        minutes = (TextView) findViewById(R.id.minutes);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                long totalSeconds = 30;
                long intervalSeconds = 1;

                countTimer= new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {
                    public void onTick(long millisUntilFinished) {
                        secondsText += 1;
                        if(secondsText >= 60){
                            minutesText +=1;
                            secondsText = 0;
                        }
                        if(minutesText >= 60){
                            hoursText += 1;
                            minutesText = 0;
                        }
                        seconds.setText(Integer.toString(secondsText));
                        minutes.setText(Integer.toString(minutesText));
                        hours.setText(Integer.toString(hoursText));
                    }

                    public void onFinish() {
                        start();
                    }
                };

                countTimer.start();
                start.setEnabled(false);
                stop.setEnabled(true);
                pause.setEnabled(true);
                // do something for button 1 click
                break;
            }

            case R.id.stop: {
                countTimer.cancel();
                secondsText = 0;
                hoursText = 0;
                minutesText = 0;
                hours.setText(Integer.toString(hoursText));
                minutes.setText(Integer.toString(minutesText));
                seconds.setText(Integer.toString(secondsText));
                start.setEnabled(true);
                stop.setEnabled(false);
                pause.setEnabled(false);
                // do something for button 2 click
                break;
            }

            case R.id.pause: {
                countTimer.cancel();
                start.setEnabled(true);
                stop.setEnabled(true);
                pause.setEnabled(false);
                break;
            }

            //.... etc
        }
    }




}