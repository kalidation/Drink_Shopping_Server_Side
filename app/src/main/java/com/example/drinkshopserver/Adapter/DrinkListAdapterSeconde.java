package com.example.drinkshopserver.Adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshopserver.Models.Drink;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.utils.Common;

public class DrinkListAdapterSeconde extends ListAdapter<Drink , DrinkListAdapterSeconde.MyViewHolder> {

    private Context context;

    public DrinkListAdapterSeconde(@NonNull DiffUtil.ItemCallback<Drink> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DrinkListAdapterSeconde.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.drink_layout_item_seconde , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.bind(getItem(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView textViewName;
        private ImageView imageViewDrink;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_drink_item_name);
            checkBox = itemView.findViewById(R.id.select_item);
            imageViewDrink = itemView.findViewById(R.id.image_drink_item);
        }

        public void bind(final Drink drink) {
            textViewName.setText(drink.getName());
            textViewName.setMovementMethod(new ScrollingMovementMethod());
            textViewName.setSelected(true);
            Glide.with(context).load(drink.getLink()).into(imageViewDrink);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(checkBox.isChecked()){
                        Common.drinksNames.add(drink.getName());
                        Common.drinks.add(drink);
                        Log.d("removed", "onClick: removed "+drink.getName());
                    }else{
                        Common.drinksNames.remove(drink.getName());
                        Log.d("add", "onClick: added "+drink.getName());
                        Common.drinks.remove(drink);
                        for(int i=0 ; i<Common.names.size();i++){
                            Log.d("add", "onClick: NAMES :"+Common.drinksNames.get(i));
                        }
                    }
                }
            });

        }
    }
}
