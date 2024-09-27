package edu.ewubd.bloodplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HealthTips extends AppCompatActivity {

    private ImageView start, stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);
        // assigning ID of startButton
        // to the object start
        start = findViewById( R.id.startButton );

        // assigning ID of stopButton
        // to the object stop
        stop = findViewById( R.id.stopButton );

        // declaring listeners for the
        // buttons to make them respond
        // correctly according to the process
        start.setOnClickListener(v -> funStartService());
        stop.setOnClickListener(v -> funStopService());
    }

    private void funStopService() {
        stopService(new Intent( this, HealthTipsService.class ) );
    }

    private void funStartService() {
        startService(new Intent( this, HealthTipsService.class ) );
    }


}