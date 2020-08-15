package com.example.foodcrawl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant("Alphonzo Foods", "Brooklyn, NY", "Wine bar from James Murphy of LCD Soundsystem serving small plates in snug digs with light woodwork."));
        restaurantList.add(new Restaurant("New York Italian", "Brooklyn, NY", "We Can Provide a Delicious Italian Meal for You. Call Fabio Cucina Italiana Now. Come Have a Great Meal Cooked by a Professional Chef. Visit in New York. "));
        restaurantList.add(new Restaurant("Norma", "Brooklyn, NY", "Norma is genuine food based on updated classic recipes, not alchemy, and the importance of using only the finest ingredients."));
        restaurantList.add(new Restaurant("Call Me Pasta!", " Pearl St, New York", "Intimate eatery serving brick oven pizzas & homemade Italian pastas such as gnocchi & linguini."));


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setFocusable(false);
        MyAdapter myAdapter = new MyAdapter(this, restaurantList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}