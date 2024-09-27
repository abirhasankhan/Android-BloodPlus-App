package edu.ewubd.bloodplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;

import edu.ewubd.bloodplus.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    FirebaseApp firebaseApp;

    BottomNavigationView bnView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bnView = findViewById(R.id.bnView);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nv_home){
                    loadFrag(new HomeFragment(), false);
                }
                else if (id == R.id.nv_service){
                    loadFrag(new ServiceFragment(), false);
                }
                else if (id == R.id.nv_profile){
                    loadFrag(new ProfileFragment(), false);
                }

                return true;
            }
        });

        bnView.setSelectedItemId(R.id.nv_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }

    private void loadFrag(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag){
            ft.add(R.id.container, fragment);
        } else {
            ft.replace(R.id.container, fragment);
        }
        ft.commit();
    }

    int count = 0;

    @Override
    public void onBackPressed() {
        count++;
        if (count == 2){
            super.onBackPressed();
            this.finishAffinity();
        } else if (count == 1){

        }

    }

}