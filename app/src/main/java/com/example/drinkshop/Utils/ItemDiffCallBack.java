package com.example.drinkshop.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshop.Model.Category;

public class ItemDiffCallBack extends DiffUtil.ItemCallback<Category> {

    @Override
    public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
        return oldItem.equals(newItem);
    }

}
