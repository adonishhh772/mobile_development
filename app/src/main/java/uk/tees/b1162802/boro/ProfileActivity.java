package uk.tees.b1162802.boro;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import uk.tees.b1162802.boro.databinding.ActivityProfileBinding;
import uk.tees.b1162802.boro.ui.editProfile.EditProfileFormState;
import uk.tees.b1162802.boro.ui.editProfile.EditProfileViewModel;
import uk.tees.b1162802.boro.ui.editProfile.EditProfileViewModelFactory;
import uk.tees.b1162802.boro.ui.login.LoginActivity;
import uk.tees.b1162802.boro.ui.updatePass.UpdatePassFormState;
import uk.tees.b1162802.boro.ui.updatePass.UpdatePassViewModel;
import uk.tees.b1162802.boro.ui.updatePass.UpdatePassViewModelFactory;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
     private ActivityProfileBinding binding;
    private Menu menu;
    FirebaseStorage storage;
    private Uri filePath;
    StorageReference storageReference;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "settingpref";
    private UpdatePassViewModel updatePassViewModel;
    private EditProfileViewModel editProfileViewModel;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextInputEditText pDateFormat,pEmail,pMobile,pAddress,pAge,oldPass, newPass, confirmPass;
    AutoCompleteTextView pGender;
    CollapsingToolbarLayout toolBarLayout;
    TextInputLayout pDateTextField, oldPassTextLayout, newPassTextLayout, confirmPassLayout,
            editMobileLayout, editAddressLayout, editAgeLayout, editGenderLayout, editDateLayout;
    TextView emailText, mobileText, addressText, genderText, ageText, birthdayText, passwordText;
    MaterialButton updatePassword;
    ImageView closeDiag, profilePicView;
    ProgressBar loadingBar;
    Dialog dialog;
    FloatingActionButton editProfile;
    String userID, username;
    boolean editAction = true;
//    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        setContentView(binding.getRoot());
        final String[] genders = getResources().getStringArray(R.array.gender_value);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.dropdown_item,genders);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("email","Boro Service Provider");
        userID = sharedPreferences.getString("userID","Boro Service Provider");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getAllUserDetails();
        toolBarLayout = binding.toolbarLayout;
        editMobileLayout = findViewById(R.id.editPhoneTextField);
        editAddressLayout = findViewById(R.id.editAddressTextField);
        editAgeLayout = findViewById(R.id.editAgeTextField);
        editGenderLayout = findViewById(R.id.editGenderTextField);
        editDateLayout = findViewById(R.id.editDateTextField);
        toolBarLayout.setTitle(getTitle());
        pDateFormat = findViewById(R.id.editDatepicker);
        pEmail = findViewById(R.id.editUsername);
        pMobile = findViewById(R.id.editPhone);
        pAddress = findViewById(R.id.editAddress);
        pAge = findViewById(R.id.editAge);
        pGender = findViewById(R.id.editGender);
        updatePassword = findViewById(R.id.updatePassword);
        emailText = findViewById(R.id.profile_email);
        mobileText = findViewById(R.id.profile_number);
        addressText = findViewById(R.id.profile_address);
        genderText = findViewById(R.id.profile_gender);
        profilePicView = binding.profilePic;
        ageText = findViewById(R.id.profile_age);
        birthdayText = findViewById(R.id.profile_date);
        passwordText = findViewById(R.id.profile_password);
        pDateTextField =findViewById(R.id.editDateTextField);
        updatePassViewModel = new ViewModelProvider(this, new UpdatePassViewModelFactory())
                .get(UpdatePassViewModel.class);
        editProfileViewModel = new ViewModelProvider(this,new EditProfileViewModelFactory()).get(EditProfileViewModel.class);
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


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                pDateFormat.setText(date);
            }
        };


        editProfileViewModel.getEditProfileFormState().observe(this, new Observer<EditProfileFormState>() {
            @Override
            public void onChanged(@Nullable EditProfileFormState editProfileFormState) {
                if (editProfileFormState == null) {
                    return;
                }
//                Log.i("TAG", "onChanged: "+updatePassFormState.isDataValid());
                editProfile.setEnabled(editProfileFormState.isDataValid());
                if (editProfileFormState.getMobileError() != null) {
                    editMobileLayout.setError(getString(editProfileFormState.getMobileError()));
                }else{
                    editMobileLayout.setError(null);
                }
                if (editProfileFormState.getAddressError() != null) {
                    editAddressLayout.setError(getString(editProfileFormState.getAddressError()));
                }else{
                    editAddressLayout.setError(null);
                }
                if (editProfileFormState.getAgeError() != null) {
                    editAgeLayout.setError(getString(editProfileFormState.getAgeError()));
                }else{
                    editAgeLayout.setError(null);
                }
                if (editProfileFormState.getGenderError() != null) {
                    editGenderLayout.setError(getString(editProfileFormState.getGenderError()));
                }else{
                    editGenderLayout.setError(null);
                }
                if (editProfileFormState.getBirthdayError() != null) {
                    editDateLayout.setError(getString(editProfileFormState.getBirthdayError()));
                }else{
                    editDateLayout.setError(null);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                editProfileViewModel.editProfileChanged(pMobile.getText().toString(),
                        pAddress.getText().toString(),pAge.getText().toString(),
                        pGender.getText().toString(),pDateFormat.getText().toString());
            }
        };
        pMobile.addTextChangedListener(afterTextChangedListener);
        pAddress.addTextChangedListener(afterTextChangedListener);
        pAge.addTextChangedListener(afterTextChangedListener);
        pGender.addTextChangedListener(afterTextChangedListener);
        pDateFormat.addTextChangedListener(afterTextChangedListener);


    }

    private void getAllUserDetails() {
        final Query checkUser = root.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    getProfileImage();
                    emailText.setText(snapshot.child(userID).child("username").getValue(String.class));
                    pEmail.setText(snapshot.child(userID).child("username").getValue(String.class));
                    mobileText.setText(snapshot.child(userID).child("mobile").getValue(String.class));
                    pMobile.setText(snapshot.child(userID).child("mobile").getValue(String.class));
                    addressText.setText(snapshot.child(userID).child("address").getValue(String.class));
                    pAddress.setText(snapshot.child(userID).child("address").getValue(String.class));
                    genderText.setText(snapshot.child(userID).child("gender").getValue(String.class));
                    pGender.setText(snapshot.child(userID).child("gender").getValue(String.class));
                    ageText.setText(snapshot.child(userID).child("age").getValue(String.class));
                    pAge.setText(snapshot.child(userID).child("age").getValue(String.class));
                    birthdayText.setText(snapshot.child(userID).child("birthday").getValue(String.class));
                    pDateFormat.setText(snapshot.child(userID).child("birthday").getValue(String.class));
                    toolBarLayout.setTitle(snapshot.child(userID).child("fullname").getValue(String.class));
                    passwordText.setText(snapshot.child(userID).child("password").getValue(String.class));
                    updatePassword.setOnClickListener(ProfileActivity.this);
                    profilePicView.setOnClickListener(ProfileActivity.this);
                    editProfile.setOnClickListener(ProfileActivity.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
           if(editAction){
               editAction = false;
               LinearLayout viewProfile = findViewById(R.id.viewProfile);
               viewProfile.setVisibility(View.GONE);
               LinearLayout updateProfile = findViewById(R.id.editProfile);
               updateProfile.setVisibility(View.VISIBLE);
               editProfile.setImageResource(R.drawable.ic_baseline_check_24);
           }else{

               updateDatabaseProfile();

           }
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


    @RequiresApi(api = Build.VERSION_CODES.M)
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
//                    updateDatabaseProfile();
                }else{
                    updateDatabaseProfile();
                }

//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
            case R.id.updatePassword:
                showPasswordDialog();
                break;
            case R.id.profilePic:
                openImageOption();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openImageOption() {
        boolean isPicked = true;
        if(isPicked){
            if(!checkCameraPermission()){
                requestCameraPermission();
            }else {
                PickImage();
            }
        }else{
            if(!checkStoragePermission()){
                requestStoragePermission();
            }else{
                PickImage();
            }
        }
    }

    private void PickImage() {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePermission() {
        boolean storageResult = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return storageResult;
    }

    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storageResult = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return cameraResult && storageResult;

    }

    private void updateDatabaseProfile() {
        try {
            editProfile.setEnabled(false);
            final Query checkUser = root.orderByChild("username").equalTo(username);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        root.child(userID).child("mobile").setValue(pMobile.getText().toString());
                        root.child(userID).child("address").setValue(pAddress.getText().toString());
                        root.child(userID).child("age").setValue(pAge.getText().toString());
                        root.child(userID).child("gender").setValue(pGender.getText().toString());
                        root.child(userID).child("birthday").setValue(pDateFormat.getText().toString());
                        getAllUserDetails();
                        editAction = true;
                        LinearLayout viewProfile = findViewById(R.id.viewProfile);
                        viewProfile.setVisibility(View.VISIBLE);
                        LinearLayout updateProfile = findViewById(R.id.editProfile);
                        updateProfile.setVisibility(View.GONE);
                        editProfile.setImageResource(android.R.drawable.ic_menu_edit);
                        updateUiWithUser("Profile updated Successfully",false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showUpdateFailed("Opertaion Cancelled"+error.getMessage());
                }
            });

        } catch (Exception e) {
            showUpdateFailed("Error Updating "+e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        filePath = getImageUri(this,selectedImage);
                        profilePicView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        filePath = selectedImage;
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profilePicView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }

        uploadImage();
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+userID+"_profile_pic");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void showPasswordDialog() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        double height_value = (3.5 * metrics.heightPixels);
        int height = (int) Math.round(height_value);
        dialog = new Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_password);
        dialog.getWindow().setLayout((6 * width)/7, (height)/5);
        dialog.show();
        closeDiag = dialog.findViewById(R.id.closeDialog);
        loadingBar = dialog.findViewById(R.id.loading);
        oldPassTextLayout = dialog.findViewById(R.id.updatePasswordTextField);
        newPassTextLayout = dialog.findViewById(R.id.updateNewPasswordTextField);
        confirmPassLayout = dialog.findViewById(R.id.updateConfirmPasswordTextField);
        oldPass = dialog.findViewById(R.id.updateOldPassword);
        newPass = dialog.findViewById(R.id.updateNewPassword);
        confirmPass = dialog.findViewById(R.id.retypePassword);
        closeDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        MaterialButton updatePass = dialog.findViewById(R.id.updateButton);
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                updatePass.setVisibility(View.INVISIBLE);
                try {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updatePassword(confirmPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingBar.setVisibility(View.GONE);
                            updatePass.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()) {
                                final Query checkUser = root.orderByChild("username").equalTo(username);
                                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()){
                                            String passwordFromDB = snapshot.child(userID).child("password").getValue(String.class);
                                            if(passwordFromDB.equals(oldPass.getText().toString())){
                                                root.child(userID).child("password").setValue(confirmPass.getText().toString());

                                            }else{
                                                showUpdateFailed("Old Password did not match our records");
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        showUpdateFailed("Opertaion Cancelled"+error.getMessage());
                                    }
                                });
                                updateUiWithUser("Successfully updated password",true);
                            } else {
                                showUpdateFailed(task.getException().getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    showUpdateFailed("Error Updating "+e.getMessage());
                }
            }
        });

        updatePassViewModel.getUpdatePassFormState().observe(this, new Observer<UpdatePassFormState>() {
            @Override
            public void onChanged(@Nullable UpdatePassFormState updatePassFormState) {
                if (updatePassFormState == null) {
                    return;
                }
//                Log.i("TAG", "onChanged: "+updatePassFormState.isDataValid());
                updatePass.setEnabled(updatePassFormState.isDataValid());
                if (updatePassFormState.getOldPassError() != null) {
                    oldPassTextLayout.setError(getString(updatePassFormState.getOldPassError()));
                }else{
                    oldPassTextLayout.setError(null);
                }
                if (updatePassFormState.getNewPassError() != null) {
                    newPassTextLayout.setError(getString(updatePassFormState.getNewPassError()));
                }else{
                    newPassTextLayout.setError(null);
                }
                if (updatePassFormState.getConfirmPassError() != null) {
                    confirmPassLayout.setError(getString(updatePassFormState.getConfirmPassError()));
                }else{
                    confirmPassLayout.setError(null);
                }



            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                updatePassViewModel.passDataChanged(oldPassTextLayout.getEditText().getText().toString(),
                        newPassTextLayout.getEditText().getText().toString(),
                        confirmPassLayout.getEditText().getText().toString());
            }
        };
        oldPass.addTextChangedListener(afterTextChangedListener);
        newPass.addTextChangedListener(afterTextChangedListener);
        confirmPass.addTextChangedListener(afterTextChangedListener);


    }

    private void updateUiWithUser(String success, boolean isDialog) {
        if(isDialog){
            dialog.dismiss();
            sharedPreferences.edit().clear().apply();
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }else{
            Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
        }

    }

    private void showUpdateFailed(String errorString) {
        Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
        toast.show();
    }

}
