package com.example.drinkshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Context context;
    private List<Favorite> favorites;

    public FavoriteAdapter(Context context, List<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.favorite_layout_item , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textViewName.setText(favorites.get(position).name);
        holder.textViewPrice.setText("$"+favorites.get(position).price);

        Glide.with(context).load(favorites.get(position).link).into(holder.imageViewFavorite);

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

           TextView textViewName,textViewPrice;
           ImageView imageViewFavorite;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.txt_favorite_name);
            textViewPrice = itemView.findViewById(R.id.txt_favorite_price);

            imageViewFavorite = itemView.findViewById(R.id.image_favorite_product);
        }
    }

    public void removeItem(int position){
        favorites.remove(position);
        //notifyItemRemoved(position);
    }

    public void restoreItem(Favorite item , int position){
        favorites.add(position,item);
        //notifyItemInserted(position);
    }

}
