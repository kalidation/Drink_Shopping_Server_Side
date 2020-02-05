package com.example.drinkshopserver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.drinkshopserver.R;
import com.example.drinkshopserver.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    CircleMenu circleMenu;

    boolean isWifiConn = false;
    boolean wifi = false;
    boolean isMobileConn = false;
    boolean mobile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        newToken();

        circleMenu = findViewById(R.id.menu);
        circleMenu.setMainMenu(R.color.textcolor, R.drawable.ic_more_vert_black_24dp, R.drawable.ic_cancel)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.ic_delete)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.ic_update)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if (index == 0) {
                            startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        }
                    }
                });


        /*final NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network1) {
                Context context = HomeActivity.this;
                ConnectivityManager connMgr =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    for (Network network : connMgr.getAllNetworks()) {
                        NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            isWifiConn |= networkInfo.isConnected();
                            Toast.makeText(context, "Connected To Wifi", Toast.LENGTH_SHORT).show();
                            wifi = true;
                        }
                        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            isMobileConn |= networkInfo.isConnected();
                            Toast.makeText(context, "Connected To Mobile Network", Toast.LENGTH_SHORT).show();
                            mobile = true;
                        }
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Network network = connMgr.getActiveNetwork();
                    NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(network);

                    if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Toast.makeText(context, "Connected To Wifi", Toast.LENGTH_SHORT).show();
                        wifi = true;
                    } else if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Toast.makeText(context, "Connected To Mobile Network", Toast.LENGTH_SHORT).show();
                        mobile = true;
                    }

                }
            }

            @Override
            public void onLost(Network network1) {
                if (wifi) {
                    wifi = false;
                    Toast.makeText(HomeActivity.this, "Disconnected From Wifi", Toast.LENGTH_SHORT).show();
                }
                if (mobile) {
                    mobile = false;
                    Toast.makeText(HomeActivity.this, "Disconnected From mobile Network", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnavailable() {
                Log.i("Internet", "onUnavailable");
            }

            @Override
            public void onLosing(Network network, int maxMsToLive) {
                Log.i("Internet", "onLosing");
            }
        };
        Context context = HomeActivity.this;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback);

        isOnline();*/
    }

    public void isOnline() {
        Context context = HomeActivity.this;
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            for (Network network : connMgr.getAllNetworks()) {
                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn |= networkInfo.isConnected();
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn |= networkInfo.isConnected();
                } else {
                    Toast.makeText(context, "Wifi Or Mobile Network Are Disable", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Network network = connMgr.getActiveNetwork();
            NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(network);
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            } else if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            } else {
                Toast.makeText(context, "Wifi Or Mobile Network Are Disable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void newToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        RetrofitClient.getInstance().getApi().isertToken(
                                "server_app_01",
                                token,
                                "1"
                        ).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.d("onResponse", "onResponse: " + response.toString());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("onResponse", "onResponse: " + t.getMessage());
                            }
                        });
                    }
                });

    }

}
