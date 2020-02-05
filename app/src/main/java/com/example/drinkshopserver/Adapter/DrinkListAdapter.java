package com.example.drinkshopserver.Adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.Models.Drink;
import com.example.drinkshopserver.R;

public class DrinkListAdapter extends ListAdapter<Drink, DrinkListAdapter.MyViewHolder> {

    private Context context;

    public DrinkListAdapter(@NonNull DiffUtil.ItemCallback<Drink> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public DrinkListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DrinkListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkListAdapter.MyViewHolder holder, final int position) {
        holder.bind(getItem(position));
        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Chose An Option : ");
                menu.setHeaderIcon(R.drawable.ic_menu_manage);
                menu.add(Menu.NONE , R.id.action_delete,getItem(position).getId(),"Delete" );
                menu.add(Menu.NONE , R.id.action_update,getItem(position).getId(),"Update" );
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName, textViewPrice;
        private ImageView imageViewDrink, imageViewSetting;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_drink_item_name);
            textViewName.setMovementMethod(new ScrollingMovementMethod());
            textViewName.setSelected(true);
            textViewPrice = itemView.findViewById(R.id.text_drink_item_price);
            imageViewDrink = itemView.findViewById(R.id.image_drink_item);
        }

        public void bind(Drink drink) {
            textViewName.setText(drink.getName());
            textViewPrice.setText(drink.getPrice()+"$");
            Glide.with(context).load(drink.getLink()).into(imageViewDrink);
        }
        void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
