package com.example.drinkshopserver.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkshopserver.Models.Cart;
import com.example.drinkshopserver.Models.Order;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.utils.CartDiffCallBack;
import com.example.drinkshopserver.utils.SpaceItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ListAdapterOrders extends ListAdapter<Order, ListAdapterOrders.MyViewModel> {

    private Context context;
    private boolean isOpen;

    public ListAdapterOrders(@NonNull DiffUtil.ItemCallback<Order> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapterOrders.MyViewModel(LayoutInflater.from(context).inflate(R.layout.ordor_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, final int position) {
        holder.bind(getItem(position));
        holder.Check();

        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Chose An Option : ");
                menu.add(Menu.NONE, R.id.menu_canceled, Integer.valueOf(getItem(position).getOrderid()), "canceled");
                menu.add(Menu.NONE, R.id.menu_Placed, Integer.valueOf(getItem(position).getOrderid()), "Placed");
                menu.add(Menu.NONE, R.id.menu_Processed, Integer.valueOf(getItem(position).getOrderid()), "Processed");
                menu.add(Menu.NONE, R.id.menu_Shipping, Integer.valueOf(getItem(position).getOrderid()), "Shipping");
                menu.add(Menu.NONE, R.id.menu_Shipped, Integer.valueOf(getItem(position).getOrderid()), "Shipped");
            }
        });

    }

    class MyViewModel extends RecyclerView.ViewHolder {

        private TextView textViewID, textViewComment, textViewPrice, textViewDetail, textViewStatus;
        private ImageView imageView, moreOptions;
        private RecyclerView recyclerView;

        MyViewModel(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.text_view_order_id);
            textViewComment = itemView.findViewById(R.id.text_view_order_commentary);
            textViewPrice = itemView.findViewById(R.id.text_view_order_price);
            textViewDetail = itemView.findViewById(R.id.text_view_order_detail);
            textViewStatus = itemView.findViewById(R.id.text_view_order_status);
            imageView = itemView.findViewById(R.id.show_more);
            recyclerView = itemView.findViewById(R.id.recyclerView_orders_detail);
            moreOptions = itemView.findViewById(R.id.order_more_item);

        }


        void bind(Order order) {
            textViewID.setText("#" + order.getOrderid());
            textViewPrice.setText("$" + order.getPrice());
            textViewComment.setText(order.getComment());
            if (order.getStatus().equals("-1")) {
                textViewStatus.setText("Order Status : Canceled");
            }
            if (order.getStatus().equals("0")) {
                textViewStatus.setText("Order Status : Placed");
            }
            if (order.getStatus().equals("1")) {
                textViewStatus.setText("Order Status : Proccessing");
            }
            if (order.getStatus().equals("2")) {
                textViewStatus.setText("Order Status : Shipping");
            }
            if (order.getStatus().equals("3")) {
                textViewStatus.setText("Order Status : Shipped");
            }


            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.addItemDecoration(new SpaceItemDecoration(15));
            ListAdapterOrderDetail listAdapterOrderDetail = new ListAdapterOrderDetail(new CartDiffCallBack());
            recyclerView.setAdapter(listAdapterOrderDetail);

            List<Cart> orderDetail = new Gson().fromJson(order.getDetail(), new TypeToken<List<Cart>>() {
            }.getType());
            Log.i("bind", "bind: " + orderDetail.get(0).name);
            listAdapterOrderDetail.submitList(orderDetail);
        }

        void Check() {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen) {
                        recyclerView.setVisibility(View.GONE);
                        textViewDetail.setText("Show Details");
                        //textViewDetail.setVisibility(View.GONE);
                        //imageView.setImageResource(R.drawable.ic_expand_more);
                        // imageViewClose.setVisibility(View.GONE);
                        isOpen = false;
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        textViewDetail.setText("Close Details");
                        //textViewDetail.setVisibility(View.VISIBLE);
                        //imageViewClose.setVisibility(View.VISIBLE);
                        //imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        isOpen = true;
                    }
                }
            });

            /*imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isOpen){
                        textViewDetail.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        isOpen = !isOpen;
                    }
                    if(isOpen){
                        textViewDetail.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        isOpen = !isOpen;
                    }
                }
            });*/
        }

        void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            moreOptions.setOnCreateContextMenuListener(listener);
        }
    }

}
