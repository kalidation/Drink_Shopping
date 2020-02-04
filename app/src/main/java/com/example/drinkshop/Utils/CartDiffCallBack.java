package com.example.drinkshop.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;

public class CartDiffCallBack extends DiffUtil.ItemCallback<Cart> {
    @Override
    public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
        return oldItem.equals(newItem);
    }
}
