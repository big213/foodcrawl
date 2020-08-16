package com.example.foodcrawl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListActivity extends AppCompatActivity implements MyAdapter.OnRestaurantListener {
    RecyclerView recyclerView;
    List<Restaurant> restaurantList = MainActivity.restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setFocusable(false);
        MyAdapter myAdapter = new MyAdapter(this, restaurantList, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        TextView recommendedTitle = findViewById(R.id.recommendedRestaurant);
        TextView recommendedAddress = findViewById(R.id.recommendedRestaurantAddress);
        TextView statusText = findViewById(R.id.statusTextView);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Random random = new Random();
        int n = restaurantList.size();
        final Restaurant rec_restaurant = restaurantList.get((random.nextInt() % n + n) % n);
        recommendedTitle.setText(rec_restaurant.title);
        recommendedAddress.setText(rec_restaurant.address);
        ratingBar.setRating((float) rec_restaurant.rating);
        if (rec_restaurant.open) statusText.setText("Open");
        else statusText.setText("Closed");

        CardView recCard = findViewById(R.id.reccard);
        recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("Restaurant", rec_restaurant);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRestaurantClick(int position) {
        Restaurant mRes = restaurantList.get(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Restaurant", mRes);
        startActivity(intent);
    }
}