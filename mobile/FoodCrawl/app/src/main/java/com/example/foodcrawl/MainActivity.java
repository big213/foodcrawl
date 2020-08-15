package com.example.foodcrawl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.radar.sdk.Radar;
import io.radar.sdk.RadarTrackingOptions;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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

        Radar.startTracking(RadarTrackingOptions.EFFICIENT);

        post();


    }

    public void post() {
        OkHttpClient client = new OkHttpClient();

        String postBody = "{\n" +
                "    \"action\": \"getCurrentUser\",\n" +
                "    \"query\": {\n" +
                "        \"id\": null,\n" +
                "        \"name\": null,\n" +
                "        \"randomNearbyNewPlace\": null\n" +
                "    }\n" +
                "}";

        Request request = new Request.Builder()
                .url("https://us-central1-foodcrawl-c0abe.cloudfunctions.net/api/jql")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        Log.i("TAG", "onResponse: " +jObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void showMapActivity(View view) {
        Intent mapIntent = new Intent(this, MapActivity.class);
        if (currentLocation!=null)
            startActivity(mapIntent);
    }

    public void showRestaurantsClicked(View view) {
        Intent resIntent = new Intent(this, ListActivity.class);
        startActivity(resIntent);
    }


}