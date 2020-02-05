package com.example.drinkshopserver.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshopserver.Activities.MainActivity;
import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.utils.onClickInterface;

import io.reactivex.disposables.CompositeDisposable;

public class Listadapter extends ListAdapter<Category, Listadapter.MyViewHolder> {

    private Context context;
    private onClickInterface listner;



    public Listadapter(@NonNull DiffUtil.ItemCallback diffCallback, onClickInterface listner) {
        super(diffCallback);
        this.listner = listner;
    }

    @NonNull
    @Override
    public Listadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Listadapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Listadapter.MyViewHolder holder, final int position) {
        holder.bind(getItem(position));
        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Chose An Option : ");
                menu.setHeaderIcon(R.drawable.ic_menu_manage);
                menu.add(Menu.NONE , R.id.action_delete,getItem(position).getiD(),"Delete" );
                menu.add(Menu.NONE , R.id.action_update,getItem(position).getiD(),"Update" );
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageCategory , settingCategory;
        private TextView textNameCategory;
        private CompositeDisposable disposable;
        private ListAdapterseconde listAdapterseconde;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.image_category_item);
            textNameCategory = itemView.findViewById(R.id.text_category_item);
            disposable = new CompositeDisposable();

        }

        public void bind(final Category category) {
            Glide.with(context).load(category.getLink()).into(imageCategory);
            textNameCategory.setText(category.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.setClick(category,2);
                }
            });
        }
        void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
