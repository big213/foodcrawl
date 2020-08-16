package com.example.foodcrawl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    Location location;

    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = getIntent();
        restaurant = (Restaurant)i.getSerializableExtra("Restaurant");
        Log.i("TAG", "onCreate: " + restaurant.title);
        TextView title = findViewById(R.id.restTitle);
        RatingBar rating = findViewById(R.id.detailsRatingBar);
        TextView address = findViewById(R.id.restAddress);
        TextView status = findViewById(R.id.statusText);

        title.setText(restaurant.title);
        rating.setRating((float)(restaurant.rating));
        address.setText(restaurant.address);
        if (restaurant.open) {
            status.setText("Open");
            status.setTextColor(Color.parseColor("#F44336"));
        }
        else {
            status.setText("Open");
            status.setTextColor(Color.parseColor("#2196F3"));
        }
        location = new Location("");
        location.setLatitude(restaurant.coordinate[1]);
        location.setLongitude(restaurant.coordinate[0]);

        mMapView = findViewById(R.id.mapView2);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(restaurant.title);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        googleMap.addMarker(markerOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}