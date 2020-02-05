package com.example.drinkshopserver.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshopserver.Models.Cart;

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
