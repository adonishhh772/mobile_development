package uk.tees.b1162802.boro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.util.Calendar;

import uk.tees.b1162802.boro.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Menu menu;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextInputEditText pDateFormat;
    TextInputLayout pDateTextField;
    private ActivityProfileBinding binding;
    FloatingActionButton editProfile;
    boolean editAction = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final String[] genders = getResources().getStringArray(R.array.gender_value);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.dropdown_item,genders);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        pDateFormat = findViewById(R.id.editDatepicker);
        pDateTextField =findViewById(R.id.editDateTextField);
        pDateFormat.setOnClickListener(this);
        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);
        AutoCompleteTextView genderText = findViewById(R.id.editGender);
        genderText.setAdapter(arrayAdapter);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
//                    Log.i("TAG", "onOffsetChanged: show");
                    showOption(R.id.action_update);
                } else if (isShow) {
                    isShow = false;
//                    Log.i("TAG", "onOffsetChanged: hide");
                    hideOption(R.id.action_update);
                }
            }
        });

        editProfile = binding.editProfile;
        editProfile.setOnClickListener(this);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                pDateFormat.setText(date);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        hideOption(R.id.action_update);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.action_update) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        if(!editAction){
            item.setIcon(R.drawable.ic_baseline_check_24);
            item.setTitle(R.string.update);
        }else{
            item.setIcon(android.R.drawable.ic_menu_edit);
            item.setTitle(R.string.edit);
        }
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        if(!editAction){
            item.setIcon(R.drawable.ic_baseline_check_24);
            item.setTitle(R.string.update);
        }else{
            item.setIcon(android.R.drawable.ic_menu_edit);
            item.setTitle(R.string.edit);
        }
        item.setVisible(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_profile:
                if(editAction){
                    editAction = false;
                    LinearLayout viewProfile = findViewById(R.id.viewProfile);
                    viewProfile.setVisibility(View.GONE);
                    LinearLayout updateProfile = findViewById(R.id.editProfile);
                    updateProfile.setVisibility(View.VISIBLE);
                    editProfile.setImageResource(R.drawable.ic_baseline_check_24);
                }else{
                    editAction = true;
                    LinearLayout viewProfile = findViewById(R.id.viewProfile);
                    viewProfile.setVisibility(View.VISIBLE);
                    LinearLayout updateProfile = findViewById(R.id.editProfile);
                    updateProfile.setVisibility(View.GONE);
                    editProfile.setImageResource(android.R.drawable.ic_menu_edit);
                }

                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.editDatepicker:
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProfileActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                        onDateSetListener,year,month,day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }
}