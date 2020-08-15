package com.example.foodcrawl;

import java.util.concurrent.ThreadLocalRandom;

public class Restaurant {
    String title;
    String address;
    String description;
    int image;

    public static int image_id[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6};


    public Restaurant(String title, String address, String description) {
        this.title = title;
        this.address = address;
        this.description = description;
        int int_random = ThreadLocalRandom.current().nextInt() % 6;
        image = image_id[(int_random + 6)% 6];
    }
}
