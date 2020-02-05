package com.example.drinkshopserver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.drinkshopserver.Adapter.ListAdapterOrders;
import com.example.drinkshopserver.Models.DataMessage;
import com.example.drinkshopserver.Models.MyResponse;
import com.example.drinkshopserver.Models.Order;
import com.example.drinkshopserver.Models.OrderResponse;
import com.example.drinkshopserver.Models.OrderResult;
import com.example.drinkshopserver.Models.Tocken;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.Retrofit.FCMClient;
import com.example.drinkshopserver.Retrofit.IFCMApi;
import com.example.drinkshopserver.Retrofit.RetrofitClient;
import com.example.drinkshopserver.utils.OrderDiffCallBack;
import com.example.drinkshopserver.utils.SpaceItemDecoration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tomer.fadingtextview.FadingTextView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private ListAdapterOrders adapterOrders;

    private FadingTextView FTV;
    int itemNow;

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        compositeDisposable = new CompositeDisposable();

        FTV = findViewById(R.id.fadingTextView);
        FTV.stop();
        FTV.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        recyclerView.setHasFixedSize(true);

        adapterOrders = new ListAdapterOrders(new OrderDiffCallBack(), OrderActivity.this);
        recyclerView.setAdapter(adapterOrders);

        navigationView = findViewById(R.id.bottom_navigation);
        itemNow = R.id.new_status;
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.new_status:
                        getOrders("0");
                        break;
                    case R.id.cancel_status:
                        getOrders("-1");
                        break;
                    case R.id.processing_status:
                        getOrders("1");
                        break;
                    case R.id.shipping_status:
                        getOrders("2");
                        break;
                    case R.id.shipped_status:
                        getOrders("3");
                        break;
                }
                return true;
            }
        });

        getOrders("0");
    }

    private void getOrders(String status) {

        RetrofitClient.getInstance().getApi().getOrders(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(OrderResponse orderResponse) {
                        FTV.stop();
                        FTV.setVisibility(View.INVISIBLE);
                        displayDrinks(orderResponse.getOrders());
                        Log.d("displayDrinks", "displayDrinks: " + orderResponse.getOrders().size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("displayDrinks", "displayDrinks: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void displayDrinks(List<Order> orders) {
        Log.d("displayDrinks", "displayDrinks: " + orders.size());
        Collections.reverse(orders);
        adapterOrders.submitList(orders);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_canceled:
                RetrofitClient.getInstance().getApi().updateOrder("-1", String.valueOf(item.getOrder()))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                RetrofitClient.getInstance().getApi().getOneOrder(String.valueOf(item.getOrder()))
                                        .enqueue(new Callback<OrderResult>() {
                                            @Override
                                            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                                                Log.i("Phone", "onResponse: " + response.body().getPhone());
                                                RetrofitClient.getInstance().getApi().getToken(response.body().getPhone())
                                                        .enqueue(new Callback<Tocken>() {
                                                            @Override
                                                            public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                                                Log.i("Phone", "onResponse: " + response.body().getToken());
                                                                Map<String, String> contenSend = new HashMap<>();
                                                                contenSend.put("title", "DrinkShop");
                                                                contenSend.put("message", "Your Order has been updated");

                                                                DataMessage dataMessage = new DataMessage();
                                                                dataMessage.setTo(response.body().getToken());
                                                                dataMessage.setData(contenSend);

                                                                FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                                                        .enqueue(new Callback<MyResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                                Log.i("Phone", "onResponse: " + response.body().success);
                                                                                Toast.makeText(OrderActivity.this, "", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                                                                Log.i("Phone", "MyResponse: " + t.getMessage());
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Tocken> call, Throwable t) {
                                                                Log.i("Phone", "Tocken: " + t.getMessage());
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(Call<OrderResult> call, Throwable t) {
                                                Log.i("Phone", "OrderResult: " + t.getMessage());
                                            }
                                        });

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                break;
            case R.id.menu_Placed:
                RetrofitClient.getInstance().getApi().updateOrder("0", String.valueOf(item.getOrder()))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                RetrofitClient.getInstance().getApi().getOneOrder(String.valueOf(item.getOrder()))
                                        .enqueue(new Callback<OrderResult>() {
                                            @Override
                                            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                                                Log.i("Phone", "onResponse: " + response.body().getPhone());
                                                RetrofitClient.getInstance().getApi().getToken(response.body().getPhone())
                                                        .enqueue(new Callback<Tocken>() {
                                                            @Override
                                                            public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                                                Log.i("Phone", "onResponse: " + response.body().getToken());
                                                                Map<String, String> contenSend = new HashMap<>();
                                                                contenSend.put("title", "DrinkShop");
                                                                contenSend.put("message", "Your Order has been updated");

                                                                DataMessage dataMessage = new DataMessage();
                                                                dataMessage.setTo(response.body().getToken());
                                                                dataMessage.setData(contenSend);

                                                                FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                                                        .enqueue(new Callback<MyResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                                Log.i("Phone", "onResponse: " + response.body().success);
                                                                                Toast.makeText(OrderActivity.this, "", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                                                                Log.i("Phone", "MyResponse: " + t.getMessage());
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Tocken> call, Throwable t) {
                                                                Log.i("Phone", "Tocken: " + t.getMessage());
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(Call<OrderResult> call, Throwable t) {
                                                Log.i("Phone", "OrderResult: " + t.getMessage());
                                            }
                                        });

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                break;
            case R.id.menu_Processed:
                RetrofitClient.getInstance().getApi().updateOrder("1", String.valueOf(item.getOrder()))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                RetrofitClient.getInstance().getApi().getOneOrder(String.valueOf(item.getOrder()))
                                        .enqueue(new Callback<OrderResult>() {
                                            @Override
                                            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                                                Log.i("Phone", "onResponse: "+response.body().getPhone());
                                                RetrofitClient.getInstance().getApi().getToken(response.body().getPhone())
                                                        .enqueue(new Callback<Tocken>() {
                                                            @Override
                                                            public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                                                Log.i("Phone", "onResponse: "+response.body().getToken());
                                                                Map<String, String> contenSend = new HashMap<>();
                                                                contenSend.put("title", "DrinkShop");
                                                                contenSend.put("message", "Your Order has been updated");

                                                                DataMessage dataMessage = new DataMessage();
                                                                dataMessage.setTo(response.body().getToken());
                                                                dataMessage.setData(contenSend);

                                                                FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                                                        .enqueue(new Callback<MyResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                                Log.i("Phone", "onResponse: "+response.body().success);
                                                                                Toast.makeText(OrderActivity.this, "", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                                                                Log.i("Phone", "MyResponse: "+t.getMessage());
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Tocken> call, Throwable t) {
                                                                Log.i("Phone", "Tocken: "+t.getMessage());
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(Call<OrderResult> call, Throwable t) {
                                                Log.i("Phone", "OrderResult: "+t.getMessage());
                                            }
                                        });
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                break;
            case R.id.menu_Shipping:
                RetrofitClient.getInstance().getApi().updateOrder("2", String.valueOf(item.getOrder()))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                int itemNow = navigationView.getSelectedItemId();
                                RetrofitClient.getInstance().getApi().getOneOrder(String.valueOf(item.getOrder()))
                                        .enqueue(new Callback<OrderResult>() {
                                            @Override
                                            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                                                Log.i("Phone", "onResponse: "+response.body().getPhone());
                                                RetrofitClient.getInstance().getApi().getToken(response.body().getPhone())
                                                        .enqueue(new Callback<Tocken>() {
                                                            @Override
                                                            public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                                                Log.i("Phone", "onResponse: "+response.body().getToken());
                                                                Map<String, String> contenSend = new HashMap<>();
                                                                contenSend.put("title", "DrinkShop");
                                                                contenSend.put("message", "Your Order has been updated");

                                                                DataMessage dataMessage = new DataMessage();
                                                                dataMessage.setTo(response.body().getToken());
                                                                dataMessage.setData(contenSend);

                                                                FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                                                        .enqueue(new Callback<MyResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                                Log.i("Phone", "onResponse: "+response.body().success);
                                                                                Toast.makeText(OrderActivity.this, "", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                                                                Log.i("Phone", "MyResponse: "+t.getMessage());
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Tocken> call, Throwable t) {
                                                                Log.i("Phone", "Tocken: "+t.getMessage());
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(Call<OrderResult> call, Throwable t) {
                                                Log.i("Phone", "OrderResult: "+t.getMessage());
                                            }
                                        });

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                break;
            case R.id.menu_Shipped:
                RetrofitClient.getInstance().getApi().updateOrder("3", String.valueOf(item.getOrder()))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                RetrofitClient.getInstance().getApi().getOneOrder(String.valueOf(item.getOrder()))
                                        .enqueue(new Callback<OrderResult>() {
                                            @Override
                                            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                                                Log.i("Phone", "onResponse: "+response.body().getPhone());
                                                RetrofitClient.getInstance().getApi().getToken(response.body().getPhone())
                                                        .enqueue(new Callback<Tocken>() {
                                                            @Override
                                                            public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                                                Log.i("Phone", "onResponse: "+response.body().getToken());
                                                                Map<String, String> contenSend = new HashMap<>();
                                                                contenSend.put("title", "DrinkShop");
                                                                contenSend.put("message", "Your Order has been updated");

                                                                DataMessage dataMessage = new DataMessage();
                                                                dataMessage.setTo(response.body().getToken());
                                                                dataMessage.setData(contenSend);

                                                                FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                                                        .enqueue(new Callback<MyResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                                Log.i("Phone", "onResponse: "+response.body().success);
                                                                                Toast.makeText(OrderActivity.this, "", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                                                                Log.i("Phone", "MyResponse: "+t.getMessage());
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Tocken> call, Throwable t) {
                                                                Log.i("Phone", "Tocken: "+t.getMessage());
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(Call<OrderResult> call, Throwable t) {
                                                Log.i("Phone", "OrderResult: "+t.getMessage());
                                            }
                                        });
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                break;
        }
        return super.onContextItemSelected(item);
    }
}
