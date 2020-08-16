package com.example.foodcrawl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    public static JSONObject jObject;
    public static List<Restaurant> restaurantList;
    public boolean animationFinished = false;
    public boolean dataLoaded = false;


    public String apikey = "AIzaSyCLkTDuQtBR-rs-layuF1QMsHExMUAOvLI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restaurantList = new ArrayList<>();



        int requestCode = 0;
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }, requestCode);

        customLocation = new Location("");
        customLocation.setLatitude(22.376524422807385d);
        customLocation.setLongitude(91.83158386561064d);
        Radar.initialize(this, publishableKey);

        Radar.trackOnce(customLocation, new RadarCallback() {

            @Override
            public void onComplete(Radar.RadarStatus status, Location location, RadarEvent[] events, RadarUser user) {
                currentLocation = location;
                Log.i("LOCAL", "onComplete: " + location.toString());
                //Log.i("USer Id.", "onComplete: " + user.get_id());

            }
        });

        Radar.startTracking(RadarTrackingOptions.EFFICIENT);
        post();

        ImageView appIconImage = findViewById(R.id.appLogoImage);
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                animationFinished = true;
                Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                if (dataLoaded) {
                    startActivity(intent);
                    finish();
                }

            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        appIconImage.startAnimation(animation);

    }

    public Restaurant getDetails(String name, final double x, final double y) {
        name = name.trim();
        name  = name.replaceAll("\\s", "%20");


        final OkHttpClient client = new OkHttpClient();
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?"+
                "input="+ name + "&inputtype=textquery"+
                "&fields=photos,formatted_address,name,opening_hours,rating"+
                "&locationbias=circle:2000@"+x+","+y+
                "&key=AIzaSyCLkTDuQtBR-rs-layuF1QMsHExMUAOvLI";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    //Log.i("TAG", "onResponse: " + myResponse);
                    try {
                        JSONObject reqObj = new JSONObject(myResponse);
                        JSONArray canditates = reqObj.getJSONArray("candidates");
                        JSONObject item = canditates.getJSONObject(0);
                        Log.i("TAG", "onResponse: " + item.getString("name") + " " + item.getDouble("rating"));
                        Restaurant restaurant = new Restaurant(item.getString("name"), item.getString("formatted_address"), "N/A");
                        restaurant.rating = item.getDouble("rating");
                        JSONObject openingHours = item.getJSONObject("opening_hours");
                        Log.i("TAG", "onResponse: "+openingHours.getBoolean("open_now"));
                        restaurant.coordinate[0] = x;
                        restaurant.coordinate[1] = y;
                        restaurant.open = openingHours.getBoolean("open_now");
                        restaurantList.add(restaurant);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return null;
    }

    public void post() {
        OkHttpClient client = new OkHttpClient();

        String postBody = "{\n" +
                "    \"action\": \"getCurrentUser\",\n" +
                "    \"query\": {\n" +
                "        \"id\": null,\n" +
                "        \"name\": null,\n" +
                "        \"randomNearbyNewPlaces\": null\n" +
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
                        jObject = new JSONObject(response.body().string());
                        Log.i("TAG", "onResponse: " +jObject.getString("message"));
                        JSONObject  data = jObject.getJSONObject("data");
                        JSONArray newPlaces = data.getJSONArray("randomNearbyNewPlaces");
                        Log.i("TAG", "onresponse" + newPlaces.getJSONObject(0).getString("name"));

                        for (int i=0; i<newPlaces.length(); i++) {
                            JSONObject place = newPlaces.getJSONObject(i);
                            String name =  place.getString("name");
                            JSONObject location = place.getJSONObject("location");
                            JSONArray coordinate = location.getJSONArray("coordinates");
                            //name = name.replaceAll("[^\\x20-\\x7e]", "");
                            Log.i("TAG", "Response ->" + name + "  =" + coordinate.get(0));

                            Restaurant restaurant = new Restaurant(name, "N/A", "N/A");
                            restaurant.setCoordinate((double)coordinate.get(0), (double)coordinate.get(1));

                            getDetails(name, restaurant.coordinate[0], restaurant.coordinate[1]);

                        }
                        dataLoaded = true;
                        if (animationFinished == true) {
                            Intent i = new Intent(getApplicationContext(), DrawerActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




}