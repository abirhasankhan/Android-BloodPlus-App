package edu.ewubd.bloodplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;


    private EditText userEmail, userPass;
    private TextView signUpPage;
    private CheckBox remember;
    private Button btnLogin;
    private ProgressBar loginProgressBar;

    SharedPreferences myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        //for Retrieve SignUp page SharedPreferences DATA
        myPref = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);

        userEmail = findViewById(R.id.loginEmail);
        userPass = findViewById(R.id.loginPass);
        remember = findViewById(R.id.loginCheckBox);
        btnLogin = findViewById(R.id.btnLogin);
        signUpPage = findViewById(R.id.LoginTvSignUp);

        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginProgressBar.setVisibility(View.INVISIBLE);

        btnLogin.setOnClickListener(view -> funLogin());

        signUpPage.setOnClickListener(view -> funSignUp());


        //using SharedPreferences for getting CheckBox value
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Cheek", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Checked",Toast.LENGTH_SHORT).show();

                } else if (!buttonView.isChecked()){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Cheek", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();

                }
            }
        });

        //After registration it will automatic fill the email field
        final String s_email = userEmail.getText().toString().trim();
        final String s_pass = userPass.getText().toString().trim();

        if (s_email.isEmpty() && s_pass.isEmpty()){
            String strEmail = myPref.getString("email","");
            String strPass = myPref.getString("password","");

            userEmail.setText(strEmail);
            //userPass.setText(strPass);

        }

    }


    //Intent here to SignUp page
    private void funSignUp() {
        Intent i = new Intent(getApplicationContext(),SignUp.class);
        startActivity(i);
        finish();
    }

    //For any kind of Toast message
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }


    //Login function
    private void funLogin() {

        btnLogin.setVisibility(View.INVISIBLE);
        loginProgressBar.setVisibility(View.VISIBLE);

        final String email = userEmail.getText().toString().trim();
        final String pass = userPass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()){
            showMessage("Please Verify all fields");
            loginProgressBar.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        } else {
            login(email,pass);
        }
    }

    private void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    updateUI();
                } else {
                    showMessage(task.getException().getMessage());
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    //Intent here to homePage
    private void updateUI() {
        Intent it = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //using SharedPreferences for checking CheckBox value
        SharedPreferences pref = getSharedPreferences("Cheek", MODE_PRIVATE);
        String cheek = pref.getString("remember","");

        if (cheek.equals("true")){

            //user is already connected. so user will redirect to hoem page (using SharedPreferences)
            /*
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish()
            ;*/

            //user is already connected. so user will redirect to hoem page (using FirebaseUser)

            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                updateUI();
            }
        } else if (cheek.equals("false")) {
            Toast.makeText(LoginActivity.this,"Please Sign in",Toast.LENGTH_SHORT).show();
        }

    }

    //for back Pressed
    int count = 0;
    @Override
    public void onBackPressed() {
        count++;
        if (count == 2){
            super.onBackPressed();
            this.finishAffinity();
        }
    }
}