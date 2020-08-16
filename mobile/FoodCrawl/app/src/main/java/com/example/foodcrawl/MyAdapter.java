package com.example.foodcrawl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Restaurant> restaurantList;
    Context context;
    OnRestaurantListener onRestaurantListener;

    public MyAdapter(Context context, List<Restaurant> restaurants, OnRestaurantListener onRestaurantListener) {
        this.restaurantList = restaurants;
        this.context = context;
        this.onRestaurantListener = onRestaurantListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card, parent, false );
        return new MyViewHolder(view, onRestaurantListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(restaurantList.get(position).title);
        holder.address.setText(restaurantList.get(position).address);
        holder.image.setImageResource(restaurantList.get(position).image);
        holder.rating.setText(String.valueOf(restaurantList.get(position).rating));
        if (restaurantList.get(position).open) {
            holder.open.setText("Open");
            holder.open.setTextColor(Color.parseColor("#2196F3"));
        }
        else {
            holder.open.setText("Closed");
            holder.open.setTextColor(Color.parseColor("#F44336"));
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, address, description;
        TextView rating, open;
        ImageView image;
        OnRestaurantListener onRestaurantListener;

        public MyViewHolder(@NonNull View itemView, OnRestaurantListener onRestaurantListener) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            address = itemView.findViewById(R.id.addressTextView);
            rating = itemView.findViewById(R.id.rating);
            open = itemView.findViewById(R.id.Open_now);
            image = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            this.onRestaurantListener = onRestaurantListener;
        }

        @Override
        public void onClick(View v) {
            onRestaurantListener.onRestaurantClick(getAdapterPosition());
        }
    }

    public interface OnRestaurantListener{
        void onRestaurantClick(int position);
    }
}
