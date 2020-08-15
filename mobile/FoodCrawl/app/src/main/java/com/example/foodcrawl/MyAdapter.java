package com.example.foodcrawl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Restaurant> restaurantList;
    Context context;

    public MyAdapter(Context context, List<Restaurant> restaurants) {
        this.restaurantList = restaurants;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card, parent, false );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(restaurantList.get(position).title);
        holder.address.setText(restaurantList.get(position).address);
        holder.description.setText(restaurantList.get(position).description);
        holder.image.setImageResource(restaurantList.get(position).image);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, address, description;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            address = itemView.findViewById(R.id.addressTextView);
            description = itemView.findViewById(R.id.descTextview);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}
