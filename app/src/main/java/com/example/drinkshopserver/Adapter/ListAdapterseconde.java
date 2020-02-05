package com.example.drinkshopserver.Adapter;

import android.content.Context;
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
import com.example.drinkshopserver.Activities.MainActivity;
import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.utils.Common;
import com.example.drinkshopserver.utils.OncClearChenBox;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ListAdapterseconde extends ListAdapter<Category,ListAdapterseconde.MyViewHolder>  {

    private Context context;

    public ListAdapterseconde(@NonNull DiffUtil.ItemCallback diffCallback ) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ListAdapterseconde.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ListAdapterseconde.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout_seconde,  parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterseconde.MyViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageCategory;
        private TextView textNameCategory;
        private CheckBox checkBox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.image_category_item);
            textNameCategory = itemView.findViewById(R.id.text_category_item);
            checkBox = itemView.findViewById(R.id.select_item);
        }

        public void bind(final Category category){

            Glide.with(context).load(category.getLink()).into(imageCategory);
            textNameCategory.setText(category.getName());
            //checkBox.setEnabled(false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(checkBox.isChecked()){
                        Common.names.add(category.getName());
                        Common.categories.add(category);
                        Log.d("removed", "onClick: removed "+category.getName());
                    }else{
                        Common.names.remove(category.getName());
                        Log.d("add", "onClick: added "+category.getName());
                        Common.categories.remove(category);
                        for(int i=0 ; i<Common.names.size();i++){
                            Log.d("add", "onClick: NAMES :"+Common.names.get(i));
                        }
                    }
                }
            });

            /*checkBox.on(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked()){
                        Common.names.remove(category.getName());
                        Common.categories.remove(category);
                        Log.d("removed", "onClick: removed "+category.getName());
                        checkBox.setChecked(false);
                    }else{
                        Common.names.add(category.getName());
                        Log.d("add", "onClick: added "+category.getName());
                        Common.categories.add(category);
                        for(int i=0 ; i<Common.names.size();i++){
                            Log.d("add", "onClick: NAMES :"+Common.names.get(i));
                        }
                        checkBox.setChecked(true);
                    }
                }
            });*/
        }
    }
}
