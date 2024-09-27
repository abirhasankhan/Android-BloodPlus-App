package edu.ewubd.bloodplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDetail extends AppCompatActivity {

    TextView udSetName, udSetEmail, udSetPhone, udSetCountry, udSetCity, udSetPass, udSetBlood;
    ImageView udUserImg;

    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase dB;
    DatabaseReference dbRef;

    UserAdapter userAdapter;
    List<User> listUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);


        udUserImg = findViewById(R.id.udUserImg);
        udSetName = findViewById(R.id.udSetName);
        udSetEmail = findViewById(R.id.udSetEmail);
        udSetPhone = findViewById(R.id.udSetPhone);
        udSetCountry = findViewById(R.id.udSetCountry);
        udSetCity = findViewById(R.id.udSetCity);
        udSetPass = findViewById(R.id.udSetPass);
        udSetBlood = findViewById(R.id.udSetBloodType);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        dB = FirebaseDatabase.getInstance();
        dbRef = dB.getReference("Users");


        //now we will user Glide to load user image
        //Glide.with(this).load(currentUser.getPhotoUrl()).into(proUserImage);
        Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(udUserImg);

        //set user name and email
        //udSetName.setText(currentUser.getDisplayName());
        //udSetEmail.setText(currentUser.getEmail());

        readData(currentUser.getUid());

    }

    private void readData(String uid) {

        dbRef.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()){

                    DataSnapshot dataSnapshot = task.getResult();
                    String name = String.valueOf(dataSnapshot.child("name").getValue());
                    String blood = String.valueOf(dataSnapshot.child("bloodType").getValue());
                    String email = String.valueOf(dataSnapshot.child("email").getValue());
                    String phone = String.valueOf(dataSnapshot.child("phone").getValue());
                    String country = String.valueOf(dataSnapshot.child("country").getValue());
                    String city = String.valueOf(dataSnapshot.child("city").getValue());
                    String pass = String.valueOf(dataSnapshot.child("password").getValue());




                    udSetName.setText(name);
                    udSetBlood.setText(blood);
                    udSetEmail.setText(email);
                    udSetPhone.setText(phone);
                    udSetCountry.setText(country);
                    udSetCity.setText(city);
                    udSetPass.setText(pass);

                }else {
                    Toast.makeText(getApplicationContext(),"Faild to read", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}