package com.example.drinkshopserver.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshopserver.Models.Drink;

public class ItemDiffCallBackDrink  extends DiffUtil.ItemCallback<Drink> {
    @Override
    public boolean areItemsTheSame(@NonNull Drink oldItem, @NonNull Drink newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Drink oldItem, @NonNull Drink newItem) {
        return oldItem.equals(newItem);
    }
}
