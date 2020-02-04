package com.example.drinkshop.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshop.Model.Drink;

public class DrinkDiffCallBack extends DiffUtil.ItemCallback<Drink> {
    @Override
    public boolean areItemsTheSame(@NonNull Drink oldItem, @NonNull Drink newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Drink oldItem, @NonNull Drink newItem) {
        return oldItem.equals(newItem);
    }
}
