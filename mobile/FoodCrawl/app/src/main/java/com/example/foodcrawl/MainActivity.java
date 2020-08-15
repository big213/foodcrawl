package com.example.foodcrawl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import io.radar.sdk.Radar;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;

public class MainActivity extends AppCompatActivity {

    String publishableKey = "prj_test_pk_511ae1f1da16f29a85958b9c14a4661279d242ab";
    public static Location currentLocation;
    public static Location customLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int requestCode = 0;
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }, requestCode);

        customLocation = new Location("");
        customLocation.setLatitude(40.689298d);
        customLocation.setLongitude(-74.044504d);
        Radar.initialize(this, publishableKey);

        Radar.trackOnce(customLocation, new RadarCallback() {

            @Override
            public void onComplete(Radar.RadarStatus status, Location location, RadarEvent[] events, RadarUser user) {
                currentLocation = location;
                Log.i("LOCAL", "onComplete: " + location.toString());
                Log.i("USer Id.", "onComplete: " + user.get_id());

            }
        });

    }


    public void showMapActivity(View view) {
        Intent mapIntent = new Intent(this, MapActivity.class);
        if (currentLocation!=null)
            startActivity(mapIntent);
    }
}