package com.example.drinkshopserver.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.drinkshopserver.Models.Order;

public class OrderDiffCallBack extends DiffUtil.ItemCallback<Order> {
    @Override
    public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
        return oldItem.equals(newItem);
    }
}
