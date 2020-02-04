package com.example.drinkshop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshop.Activities.DrinkActivity;
import com.example.drinkshop.Model.Category;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>  {

    private Context context;
    private List<Category> list ;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.category_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getLink()).into(holder.imageViewCategory);
        holder.textViewCategory.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(context).saveCategory(list.get(position));
                //Intent intent = new Intent(context,DrinkActivity.class);
                //Common.currentCategory = list.get(position);
                context.startActivity(new Intent(context,DrinkActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageViewCategory;
        TextView textViewCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewCategory = itemView.findViewById(R.id.image_category_item);
            textViewCategory = itemView.findViewById(R.id.text_category_item);

        }
    }
}
