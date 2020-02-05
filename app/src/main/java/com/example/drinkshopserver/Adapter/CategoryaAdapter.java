package com.example.drinkshopserver.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.utils.onClickInterface;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryaAdapter extends RecyclerView.Adapter<CategoryaAdapter.MyViewHolder> {

    private int newcategory;
    private Context context;
    private List<Category> categories ;
    com.example.drinkshopserver.utils.onClickInterface onClickInterface;
    private List<String> names = new ArrayList<>();



    public CategoryaAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent , false));
        //RecyclerView.Adapter<CategoryaAdapter.MyViewHolder>
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        names.add(categories.get(position).getName());

    Glide.with(context).load(categories.get(position).getLink()).into(holder.imageCategory);
    holder.textNameCategory.setText(categories.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageCategory;
        private TextView textNameCategory;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCategory = itemView.findViewById(R.id.image_category_item);
            textNameCategory = itemView.findViewById(R.id.text_category_item);

        }
    }

    public void deletecategory(Category category){
        Log.d("category_adapter",category.getName()+category.getLink());
        newcategory = names.indexOf(category.getName());
        names.contains(category.getName());
        Log.d("index",String.valueOf(names.indexOf(names)));
        Log.d("index_ex",String.valueOf(names.contains(names)));
        categories.remove(names.indexOf(category.getName()));
        notifyItemRemoved(names.indexOf(category.getName()));
    }

    public void addcategory(Category category){

        categories.add(category);
        notifyItemInserted(newcategory+1);
    }

}
