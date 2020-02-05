package com.example.drinkshopserver.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshopserver.Models.Category;

public class ItemDiffCallback extends  DiffUtil.ItemCallback<Category> {
    @Override
    public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
        return oldItem.equals(newItem);
    }
}
