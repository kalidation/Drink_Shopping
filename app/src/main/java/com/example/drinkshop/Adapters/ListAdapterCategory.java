package com.example.drinkshop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshop.Activities.DrinkActivity;
import com.example.drinkshop.Model.Category;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;

public class ListAdapterCategory extends ListAdapter<Category,ListAdapterCategory.MyViewModel> {


    private Context context;

    public ListAdapterCategory(@NonNull DiffUtil.ItemCallback diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ListAdapterCategory.MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ListAdapterCategory.MyViewModel(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_layout_item, parent,  false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterCategory.MyViewModel holder, int position) {
       holder.bind(getItem(position));
    }

    public class MyViewModel extends RecyclerView.ViewHolder{
        ImageView imageViewCategory;
        TextView textViewCategory;

        public MyViewModel(@NonNull View itemView) {
            super(itemView);
            imageViewCategory = itemView.findViewById(R.id.image_category_item);
            textViewCategory = itemView.findViewById(R.id.text_category_item);
        }

        public void bind(final Category category) {

            Glide.with(context).load(category.getLink()).into(imageViewCategory);
            textViewCategory.setText(category.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPrefManager.getInstance(context).saveCategory(category);
                    context.startActivity(new Intent(context, DrinkActivity.class));
                }
            });

        }
    }
}
