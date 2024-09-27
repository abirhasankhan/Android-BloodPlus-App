package edu.ewubd.bloodplus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;

    SharedPreferences myPref;

    ExecutorService e;
    Handler h;


    private EditText etName, etEmail, etPhone, etCountry, etCity, etPass, etRePass;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private ImageView userPhoto;
    private FloatingActionButton floatingButton;

    //protected Bitmap selectedImageBitmap;
    private Uri selectedImageUri;

    private static int PReqCode = 1;

    private final String[] items = {"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"};
    private String item;

    AutoCompleteTextView autoCompleteTxt;

    ArrayAdapter<String> adapterItems;

    ActivityResultLauncher<Intent> launchSomeActivity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        e = Executors.newCachedThreadPool();
        h = new Handler(Looper.getMainLooper());

        myPref = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);


        autoCompleteTxt = findViewById(R.id.auto_complete_text);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);

        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Blood Group: "+item, Toast.LENGTH_SHORT).show();
            }
        });


        userPhoto = findViewById(R.id.userPhoto);
        floatingButton = findViewById(R.id.floatingActionButton);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etphone);
        etCountry = findViewById(R.id.etCountry);
        etCity = findViewById(R.id.etCity);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.SignProgressBar);

        progressBar.setVisibility(View.INVISIBLE);

        btnSignUp.setOnClickListener(view -> funSignUp());

        floatingButton = findViewById(R.id.floatingActionButton);

        floatingButton.setOnClickListener(view -> funUpload());

        // choose photo
        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                result -> {
                    if (result.getResultCode()
                            == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // selected Image uri  operation  here....
                        if (data != null
                                && data.getData() != null) {
                            //Uri selectedImageUri = data.getData();
                                selectedImageUri = data.getData();
                            //Bitmap selectedImageBitmap;
                        /*    try {

                                selectedImageBitmap
                                        = MediaStore.Images.Media.getBitmap(
                                        this.getContentResolver(),
                                        selectedImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            //userPhoto.setImageBitmap(selectedImageBitmap);
                            userPhoto.setImageURI(selectedImageUri);

                        }
                    }
                });
    }

    // checking Photo validation error
    private boolean validatePhoto(){

        if(selectedImageUri == null) {
            Toast.makeText(SignUp.this,"Please Upload your photo",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    // checking Name validation error
    private boolean validateName(){
        String name = etName.getText().toString().trim();
        if(name.isEmpty()) {
            etName.setError("Field can not be empty");
            return false;
        }else {
            etName.setError(null);
            return true;
        }
    }

    // checking Email validation error
    private boolean validateEmail(){
        String email = etEmail.getText().toString().trim();
        if(email.isEmpty()) {
            etEmail.setError("Field can not be empty");
            return false;
        }else {
            etEmail.setError(null);
            return true;
        }
    }

    // checking Phone validation error
    private boolean validatePhone(){
        String phone = etPhone.getText().toString().trim();
        if(phone.isEmpty()) {
            etPhone.setError("Field can not be empty");
            return false;
        }else {
            etPhone.setError(null);
            return true;
        }
    }

    // checking Country validation error
    private boolean validateCountry(){
        String country = etCountry.getText().toString().trim();
        if(country.isEmpty()) {
            etCountry.setError("Field can not be empty");
            return false;
        }else {
            etCountry.setError(null);
            return true;
        }
    }

    // checking City validation error
    private boolean validateCity(){
        String city = etCity.getText().toString().trim();
        if(city.isEmpty()) {
            etCity.setError("Field can not be empty");
            return false;
        }else {
            etCity.setError(null);
            return true;
        }
    }
    // checking Password validation error
    private boolean validatePass(){
        String pass = etPass.getText().toString().trim();
        if(pass.isEmpty()) {
            etPass.setError("Field can not be empty");
            return false;
        }else {
            etPass.setError(null);
            return true;
        }
    }

    // checking Re-Password validation error
    private boolean validateRePass(){
        String rePass = etRePass.getText().toString().trim();
        if(rePass.isEmpty()) {
            etRePass.setError("Field can not be empty");
            return false;
        }else {
            etRePass.setError(null);
            return true;
        }
    }

    private boolean validateBloodType(){
        if(item == null) {
            Toast.makeText(SignUp.this,"Select your Blood type",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

    private void funSignUp() {

        //System.out.println(selectedImageUri);

        btnSignUp.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String country = etCountry.getText().toString().trim();
        final String city = etCity.getText().toString().trim();
        final String pass = etPass.getText().toString().trim();
        final String rePass = etRePass.getText().toString().trim();

        if (!validateName() || !validateEmail() || !validatePhone() || !validatePhoto() || !validateCountry() || !validateCity() || !validatePass() || !validateRePass() || !validateBloodType()){

            //for invalid field
            //error message
            showMessage("Please Verify all fields");
            btnSignUp.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        } else {
            if (pass.equals(rePass)){
                //for ok field
                createUser(name, email, pass, phone, country, city, item);
                myPref.edit().putString("email",email).apply();
                myPref.edit().putString("password",pass).apply();

            } else {
                showMessage("Password didn't match");
                btnSignUp.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

        }

    }

    private void createUser(String name, String email, String pass, String phone, String country, String city, String item) {

        //create user with uniq email and pass
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            //account created successfully
                            showMessage("Your account has been created. Please wait a few moments.");

                            //useing ExecutorService
                            e.execute(() -> {

                                // after created account, we need to update his/her profile picture and name
                                updateUserInfo(name, selectedImageUri, auth.getCurrentUser());

                                h.post(() -> {
                                    //upload user info in database
                                    User user = new User(name, email, phone, country, city, pass, item);
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(user);

                                });

                            });

                            //without useing ExecutorService
                            /*
                            User user = new User(name, email, phone, country, city, pass, item);

                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);

                            // after created account, we need to update his/her profile picture and name
                            updateUserInfo(name, selectedImageUri, auth.getCurrentUser());    */

                        } else {
                            //account creation failed
                            showMessage("Account creation failed" + task.getException().getMessage());
                            btnSignUp.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    //update user name, photo
    private void updateUserInfo(String name, Uri selectedImageUri, FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get uri
        StorageReference mSStorage = FirebaseStorage.getInstance().getReference().child("user_photo");
        StorageReference imageFilePath = mSStorage.child(selectedImageUri.getLastPathSegment());
        imageFilePath.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                //image uploaded successfully
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //uri contain user image url
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //user info update successfully
                                            showMessage("Register Complete");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    //display error message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


    private void funUpload() {

        if (Build.VERSION.SDK_INT >= 22){
            checkAndRequestForPermission();
        } else {
            openGallery();
        }

    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(SignUp.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SignUp.this,
                                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                        PReqCode);
            }
        } else {
            openGallery();
        }

    }

    private void openGallery() {

        Intent i = new Intent();

        i.setAction(Intent.ACTION_PICK); //pick image and open all app for choosing photo
        // i.setAction(Intent.ACTION_GET_CONTENT); // get content and open file manger

        // i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //open only photo gallery
        i.setType("image/*"); //open photo gallery and file manger for choosing photo

        launchSomeActivity.launch(i);
    }

    //for back Pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }
}
