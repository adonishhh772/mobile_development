package live.tees.B1162802.aBastola.labwork2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextActivity;
    private TextInputEditText fname,age,gender,email;
    private EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextActivity = (Button) findViewById(R.id.storeInformation);
        fname = (TextInputEditText) findViewById(R.id.nameText);
        age = (TextInputEditText) findViewById(R.id.ageText);
        gender = (TextInputEditText) findViewById(R.id.genderText);
        email = (TextInputEditText) findViewById(R.id.emailText);
        description = (EditText) findViewById(R.id.description);
        nextActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        
    }
}