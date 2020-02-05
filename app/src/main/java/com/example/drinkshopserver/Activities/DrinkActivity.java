package com.example.drinkshopserver.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkshopserver.Adapter.DrinkListAdapter;
import com.example.drinkshopserver.Adapter.DrinkListAdapterSeconde;
import com.example.drinkshopserver.Adapter.ListAdapterseconde;
import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.Models.Drink;
import com.example.drinkshopserver.Models.DrinkResponse;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.Retrofit.RetrofitClient;
import com.example.drinkshopserver.utils.Common;
import com.example.drinkshopserver.utils.ItemDiffCallBackDrink;
import com.example.drinkshopserver.utils.ItemDiffCallback;
import com.example.drinkshopserver.utils.SharedPrefManager;
import com.example.drinkshopserver.utils.SpaceItemDecoration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import edmt.dev.afilechooser.utils.FileUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FILE_REQUEST = 1111;
    private RecyclerView recyclerView, recyclerViewseconde;
    private DrinkListAdapter adapter;
    private CompositeDisposable disposable;
    private List<Drink> drinkss = new ArrayList<>();
    private TextView textView, textImage, textName, title_recyvler_view, textViewAddCategory, textViewaddPrice ;
    private FadingTextView FTV;
    private int globalID;
    private Handler handler = new Handler();


    private StorageReference storageReference;
    private StorageTask uploadTask;

    private DrinkListAdapterSeconde listAdapterseconde;

    private Category category;

    private boolean isOpen, isOpen1, isopen2;


    FloatingActionButton fab;
    FloatingActionButton fabadd;
    FloatingActionButton fabdelete;
    FloatingActionButton fabupdate;
    FloatingActionButton fabimage;
    FloatingActionButton fabname;

    float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    private ImageView imageViewAddCategory;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        initFloatButton();

        storageReference = FirebaseStorage.getInstance().getReference("image_Drinks");

        textView = findViewById(R.id.text_category_name);
        title_recyvler_view = findViewById(R.id.text_select_item_to_delete);
        title_recyvler_view.setVisibility(View.GONE);
        FTV = findViewById(R.id.fadingTextView);
        FTV.stop();
        FTV.setVisibility(View.GONE);

        disposable = new CompositeDisposable();

        recyclerView = findViewById(R.id.recycler_view_Drink);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        adapter = new DrinkListAdapter(new ItemDiffCallBackDrink());
        recyclerView.setAdapter(adapter);

        recyclerViewseconde = findViewById(R.id.recycler_view_second);
        recyclerViewseconde.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewseconde.setHasFixedSize(true);
        recyclerViewseconde.addItemDecoration(new SpaceItemDecoration(10));
        listAdapterseconde = new DrinkListAdapterSeconde(new ItemDiffCallBackDrink());
        recyclerViewseconde.setAdapter(listAdapterseconde);


        category = SharedPrefManager.getInstance(this).getCategory();
        globalID = category.getiD();
        getDrink(globalID);
        textView.setText(category.getName());


        final NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();


        final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network1) {
                getDrink(globalID);
            }

            @Override
            public void onLost(Network network1) {
            }

            @Override
            public void onUnavailable() {
            }

            @Override
            public void onLosing(Network network, int maxMsToLive) {
            }
        };
        Context context = DrinkActivity.this;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback);


    }

    private void initFloatButton() {
        fab = findViewById(R.id.open_menu);
        fabadd = findViewById(R.id.add_category);
        fabupdate = findViewById(R.id.delete_category);
        fabimage = findViewById(R.id.update_category_image);
        fabname = findViewById(R.id.update_category_name);

        textImage = findViewById(R.id.text_image);
        textName = findViewById(R.id.text_Name);

        fabadd.setAlpha(0f);
        fabadd.hide();
        fabupdate.setAlpha(0f);
        fabname.setAlpha(0f);
        fabimage.setAlpha(0f);
        textImage.setAlpha(0f);
        textName.setAlpha(0f);


        fabadd.setTranslationY(translationY);
        fabupdate.setTranslationY(translationY);
        fabimage.setTranslationY(translationY);
        fabname.setTranslationY(translationY);

        textName.setTranslationY(translationY);
        textImage.setTranslationY(translationY);


        fabadd.setOnClickListener(this);
        fabupdate.setOnClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });
    }

    private void getDrink(int id) {

        Log.i("globalID", "Init:  " + id);
        /*disposable.add(RetrofitClient.getInstance().getApi().getDrink(String.valueOf(id))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<DrinkResponse>() {
            @Override
            public void accept(DrinkResponse drinkResponse) throws Exception {
                displayDrinks(drinkResponse.getDrinks());
                Log.i("check", "accept: "+drinkResponse.getError());
            }
        }));*/
        RetrofitClient.getInstance().getApi().getDrink(String.valueOf(id)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DrinkResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(DrinkResponse drinkResponse) {
                        FTV.stop();
                        FTV.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        displayDrinks(drinkResponse.getDrinks());
                    }

                    @Override
                    public void onError(Throwable e) {
                       // Toast.makeText(DrinkActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                        if (drinkss.isEmpty()) {
                            FTV.restart();
                            FTV.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onComplete() {
                        //disposable.clear();
                    }
                });
        refresh(10000);
    }

    private void refresh(int milliseconde) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getDrink(globalID);
                // Toast.makeText(MainActivity.this, "REFRESH", Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(runnable, milliseconde);
    }

    private void displayDrinks(List<Drink> drinks) {
        drinkss = drinks;
        adapter.submitList(drinks);
        listAdapterseconde.submitList(drinks);
    }

    private void closeMenu() {
        isOpen = !isOpen;

        fab.animate().setInterpolator(interpolator).rotationBy(45f).setDuration(300).start();
        Common.names.clear();

        fabadd.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabupdate.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabupdate.hide();
        fabadd.hide();

        if (isOpen1) {
            textImage.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            textName.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fabimage.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fabname.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fabimage.hide();
            fabname.hide();
        }

        recyclerViewseconde.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


    }


    private void openMenu() {
        isOpen = !isOpen;

        fab.animate().setInterpolator(interpolator).rotationBy(0f).setDuration(300).start();

        fabadd.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabupdate.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabupdate.show();
        fabadd.show();

        if (isOpen1) {
            textImage.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            textName.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fabimage.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fabname.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fabname.hide();
            fabimage.hide();
        }

        recyclerViewseconde.setVisibility(View.GONE);
        title_recyvler_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        listAdapterseconde = new DrinkListAdapterSeconde(new ItemDiffCallBackDrink());
        recyclerViewseconde.setAdapter(listAdapterseconde);
        listAdapterseconde.submitList(drinkss);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_category:
                addDrink();
                closeMenu();
                break;
            case R.id.delete_category:
                if (isOpen) {
                    isOpen = !isOpen;

                    isOpen1 = true;
                    isopen2 = false;

                    fab.animate().setInterpolator(interpolator).rotationBy(45f).setDuration(300).start();

                    fabadd.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

                    fabimage.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                    fabname.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                    textImage.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                    textName.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                    fabname.show();
                    fabimage.show();
                    recyclerView.setVisibility(View.GONE);
                    recyclerViewseconde.setVisibility(View.VISIBLE);
                    title_recyvler_view.setVisibility(View.VISIBLE);

                } else {
                    isOpen = !isOpen;
                    isOpen1 = false;

                    fab.animate().setInterpolator(interpolator).rotationBy(0f).setDuration(300).start();

                    fabadd.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

                    fabimage.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fabname.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    textImage.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    textName.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewseconde.setVisibility(View.GONE);
                    title_recyvler_view.setVisibility(View.GONE);
                    listAdapterseconde = new DrinkListAdapterSeconde(new ItemDiffCallBackDrink());
                    recyclerViewseconde.setAdapter(listAdapterseconde);
                    listAdapterseconde.submitList(drinkss);
                    fabname.hide();
                    fabimage.hide();

                }
                fabname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common.drinksNames.isEmpty()) {
                            Toast.makeText(DrinkActivity.this, "Select Items please", Toast.LENGTH_SHORT).show();
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerViewseconde.setVisibility(View.GONE);
                            openMenu();
                            isOpen1 = false;
                        } else {
                            //Toast.makeText(MainActivity.this, "Categories deleted", Toast.LENGTH_SHORT).show();
                            openMenu();
                            isOpen1 = false;
                            for (int i = 0; i < Common.names.size(); i++) {
                                Log.i("NAMES", "onClick: " + Common.drinksNames.get(i));
                            }
                            deleteDrinks(Common.drinksNames);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerViewseconde.setVisibility(View.GONE);
                            title_recyvler_view.setVisibility(View.GONE);
                        }
                    }
                });

                fabimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMenu();
                        Common.drinksNames.clear();
                        Common.drinks.clear();
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerViewseconde.setVisibility(View.GONE);
                        title_recyvler_view.setVisibility(View.GONE);
                        listAdapterseconde = new DrinkListAdapterSeconde(new ItemDiffCallBackDrink());
                        recyclerViewseconde.setAdapter(listAdapterseconde);
                        listAdapterseconde.submitList(drinkss);
                    }
                });
                break;
        }
    }

    private void deleteDrinks(final List<String> drinksNames) {
        final AlertDialog dialog = new SpotsDialog(this);
        dialog.show();
        dialog.setMessage("Deleting Categories");

        for (int i = 0; i < drinksNames.size(); i++) {

            final int finalI = i;
            RetrofitClient.getInstance().getApi().deleteDrinks(drinksNames.get(i))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (finalI == drinksNames.size() - 1) {
                                if (response.code() == 200) {
                                    Toast.makeText(DrinkActivity.this, "Drinks deleted", Toast.LENGTH_SHORT).show();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Drink").child("Drink_deleted");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    reference.setValue(currentTime);
                                    getDrink(category.getiD());
                                    Common.drinksNames.clear();
                                    Common.drinks.clear();
                                    dialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(DrinkActivity.this, "Error Network , Check Your Connexion" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error", "onFailure: " + t.getMessage());
                            dialog.dismiss();
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    Uri uri = data.getData();
                    fileUri = uri;
                    if (uri != null && !uri.getPath().isEmpty()) {
                        imageViewAddCategory.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "Can't Upload to server", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addDrink() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_drink_layout_item, null);

        imageViewAddCategory = view.findViewById(R.id.add_image_category);
        textViewAddCategory = view.findViewById(R.id.add_name_category);
        textViewaddPrice = view.findViewById(R.id.add_price_drink);

        imageViewAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select Your Image"), PICK_FILE_REQUEST);
            }
        });

        builder.setTitle("Add new Drink");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                closeMenu();
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (fileUri != null && !textViewAddCategory.getText().toString().isEmpty()) {
                    uploadFile(fileUri, textViewAddCategory.getText().toString(), textViewaddPrice.getText().toString());
                } else {
                    Toast.makeText(DrinkActivity.this, "Image and Name are Requiered", Toast.LENGTH_SHORT).show();
                }
                closeMenu();
            }
        });
        builder.setView(view);
        builder.show();

    }

    private void uploadFile(Uri fileUri, final String name, final String price) {

        final AlertDialog dialog = new SpotsDialog(this);
        dialog.show();
        dialog.setTitle("Adding Category");
        dialog.setMessage("Please Wait until Adding ...");
        final StorageReference imageReference = storageReference.child("Category-" + category.getName()).child(name + "-Drink");
        uploadTask = imageReference.putFile(fileUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(DrinkActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                return imageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    final String downUri = task.getResult().toString();
                    RetrofitClient
                            .getInstance()
                            .getApi()
                            .addDrink(name, downUri, Float.valueOf(price), category.getiD())
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(DrinkActivity.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                                    //adapter.addcategory(new Category(name,downUri));
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Drink").child("Drink_Added");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    reference.setValue(currentTime);
                                    getDrink(category.getiD());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(DrinkActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                RetrofitClient.getInstance().getApi().deleteOneDrink(item.getOrder())
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(DrinkActivity.this, "Item deleted" + response.message(), Toast.LENGTH_SHORT).show();
                                getDrink(globalID);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Drink").child("Drink_deleted");
                                Date currentTime = Calendar.getInstance().getTime();
                                reference.setValue(currentTime);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(DrinkActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                            }
                        });
                closeMenu();
                break;
            case R.id.action_update:
                AlertDialog.Builder builder = new AlertDialog.Builder(DrinkActivity.this);
                View view = LayoutInflater.from(DrinkActivity.this).inflate(R.layout.update_drink_item, null);
                final EditText editTextUpdateName = view.findViewById(R.id.edit_text_update_drink);
                final EditText editTextUpdatePrice = view.findViewById(R.id.edit_text_update_drink_price);
                builder.setTitle("Update Drink ");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        closeMenu();
                    }
                });
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        closeMenu();
                        final AlertDialog dialog1 = new SpotsDialog(DrinkActivity.this);
                        dialog1.show();
                        dialog1.setMessage("Updating ... ");
                        RetrofitClient.getInstance().getApi().updateDrink(item.getOrder(), editTextUpdateName.getText().toString() , editTextUpdatePrice.getText().toString())
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.code() == 200) {
                                            dialog1.dismiss();
                                            Toast.makeText(DrinkActivity.this, "Drink Name Updated", Toast.LENGTH_SHORT).show();
                                            getDrink(globalID);
                                        } else {
                                            dialog1.dismiss();
                                            Toast.makeText(DrinkActivity.this, "Drink Not Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        dialog1.dismiss();
                                        Toast.makeText(DrinkActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setView(view);
                builder.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDrink(globalID);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
        handler.removeCallbacksAndMessages(null);
    }
}
