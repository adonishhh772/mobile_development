package live.tees.B1162802.aBastola.labwork2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextActivity;
    private TextInputLayout fname,age,gender,email,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextActivity = (Button) findViewById(R.id.storeInformation);
        fname = (TextInputLayout) findViewById(R.id.nameText);
        age = (TextInputLayout) findViewById(R.id.ageText);
        gender = (TextInputLayout) findViewById(R.id.genderText);
        email = (TextInputLayout) findViewById(R.id.emailText);
        description = (TextInputLayout) findViewById(R.id.description);
        nextActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(fname.getEditText().getText().toString().isEmpty()){
            fname.setError("Hello");
            fname.requestFocus();
        }else{
            fname.setError(null);
        }

        Toast.makeText(this,"this is is",Toast.LENGTH_LONG).show();



    }
}