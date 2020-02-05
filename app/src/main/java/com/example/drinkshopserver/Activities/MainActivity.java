package com.example.drinkshopserver.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.example.drinkshopserver.Adapter.CategoryaAdapter;
import com.example.drinkshopserver.Adapter.ListAdapterseconde;
import com.example.drinkshopserver.Adapter.Listadapter;
import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.Models.CategoryResponse;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.Retrofit.RetrofitClient;
import com.example.drinkshopserver.utils.Common;
import com.example.drinkshopserver.utils.ItemDiffCallback;
import com.example.drinkshopserver.utils.OncClearChenBox;
import com.example.drinkshopserver.utils.SharedPrefManager;
import com.example.drinkshopserver.utils.SpaceItemDecoration;
import com.example.drinkshopserver.utils.onClickInterface;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hitomi.cmlibrary.CircleMenu;
import com.tomer.fadingtextview.FadingTextView;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Fts3;

import android.view.Menu;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, onClickInterface, NavigationView.OnNavigationItemSelectedListener {

    public static int PICK_FILE_REQUEST = 1111;
    public Uri fileUri;

    public ImageView imageViewAddCategory;
    public EditText textViewAddCategory, editTextDelete;
    private TextView title_recyvler_view;
    NavController navController;

    final Handler handler = new Handler();


    private AppBarConfiguration mAppBarConfiguration;
    private BroadcastReceiver broadcastReceiver;

    private RecyclerView recyclerView, recyclerViewseconde;
    private CompositeDisposable disposable;
    private List<Category> categoryList;
    private List<Category> categories1 = new ArrayList<>();
    public OncClearChenBox listner;


    FloatingActionButton fab;
    FloatingActionButton fabadd;
    FloatingActionButton fabdelete;
    FloatingActionButton fabupdate;
    FloatingActionButton fabimage;
    FloatingActionButton fabname;
    FloatingActionButton fabsubmit;
    FloatingActionButton fabcancel;

    MenuItem item;
    TextView textImage, textName;

    float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    private StorageReference storageReference;
    private StorageTask uploadTask;

    private CircleMenu circleMenu;

    private boolean isOpen, isOpen1;
    private FadingTextView FTV;

    CategoryaAdapter adapter;
    Listadapter listadapter;
    ListAdapterseconde listAdapterseconde;

    DrawerLayout drawer;
    private boolean isopen2;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title_recyvler_view = findViewById(R.id.text_select_item_to_delete);
        FTV = findViewById(R.id.fadingTextView);
        FTV.stop();
        FTV.setVisibility(View.GONE);


        fab = findViewById(R.id.open_menu);
        fabadd = findViewById(R.id.add_category);
        fabadd.hide();
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


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();


        //TODO : INIT RxJava


        disposable = new CompositeDisposable();

        //TODO : INIT FIREBASE

        storageReference = FirebaseStorage.getInstance().getReference("image_Category");


        //TODO : GATEGORY LIST

        recyclerView = findViewById(R.id.recycler_view_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        listadapter = new Listadapter(new ItemDiffCallback(), this);
        recyclerView.setAdapter(listadapter);

        listAdapterseconde = new ListAdapterseconde(new ItemDiffCallback());

        recyclerViewseconde = findViewById(R.id.recycler_view_second);
        recyclerViewseconde.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewseconde.setHasFixedSize(true);
        recyclerViewseconde.addItemDecoration(new SpaceItemDecoration(10));
        recyclerViewseconde.setAdapter(listAdapterseconde);

        getCateogory();
        //TODO : REFRESH WHEN WIFI OR MOBILE

        final NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();


        final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network1) {
                getCateogory();
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
        Context context = MainActivity.this;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback);

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
        listAdapterseconde = new ListAdapterseconde(new ItemDiffCallback());
        recyclerViewseconde.setAdapter(listAdapterseconde);
        listAdapterseconde.submitList(categories1);

    }

    private void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                getCateogory();
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);

    }

    private void deleteCategory(final List<String> names) {
        final AlertDialog dialog = new SpotsDialog(this);
        dialog.show();
        dialog.setMessage("Deleting Categories");

        for (int i = 0; i < names.size(); i++) {

            final int finalI = i;
            RetrofitClient.getInstance().getApi().deleteCategpry(names.get(i))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (finalI == names.size() - 1) {
                                if (response.code() == 200) {
                                    Toast.makeText(MainActivity.this, "Categories deleted", Toast.LENGTH_SHORT).show();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menu").child("Menu_deleted");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    reference.setValue(currentTime);
                                    getCateogory();
                                    Common.names.clear();
                                    Common.categories.clear();
                                    dialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error Network , Check Your Connexion" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error", "onFailure: " + t.getMessage());
                            dialog.dismiss();
                        }
                    });

        }
    }

    private void addCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_category_dialog, null);

        imageViewAddCategory = view.findViewById(R.id.add_image_category);
        textViewAddCategory = view.findViewById(R.id.add_name_category);

        imageViewAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select Your Image"), PICK_FILE_REQUEST);
            }
        });

        builder.setTitle("Add new Category");
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
                    uploadFile(fileUri, textViewAddCategory.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Image and Name are Requiered", Toast.LENGTH_SHORT).show();
                }
                closeMenu();
            }
        });
        builder.setView(view);
        builder.show();

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

    private void uploadFile(final Uri fileUri, final String name) {
        final AlertDialog dialog = new SpotsDialog(this);
        dialog.show();
        dialog.setTitle("Adding Category");
        dialog.setMessage("Please Wait until Adding ...");
        final StorageReference imageReference = storageReference.child("Category-" + name);
        uploadTask = imageReference.putFile(fileUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                            .addCategory(name, downUri)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(MainActivity.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                                    //adapter.addcategory(new Category(name,downUri));
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menu").child("Menu_Added");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    reference.setValue(currentTime);
                                    getCateogory();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }


    private void refresh(int milliseconde) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getCateogory();
                //Toast.makeText(MainActivity.this, "REFRESH", Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(runnable, milliseconde);
    }

    private void getCateogory() {

        /*disposable.add(RetrofitClient.getInstance().getApi().getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResponse>() {
                    @Override
                    public void accept(CategoryResponse response) throws Exception {
                    }
                }));*/

        RetrofitClient.getInstance().getApi().getCategory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(CategoryResponse response) {
                        FTV.stop();
                        FTV.setVisibility(View.INVISIBLE);
                        Log.e("rxjava",""+response.getCategories().size());
                        displayCateogory(response.getCategories());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(MainActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                        if (categories1.isEmpty()) {
                            FTV.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        refresh(10000);

    }

    private void displayCateogory(List<Category> categories) {
        categories1 = categories;
        //adapter = new CategoryaAdapter(this, categories);
        //recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        listadapter.submitList(categories);
        listAdapterseconde.submitList(categories);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
        disposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.clear();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_category:
                addCategory();
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
                    listAdapterseconde = new ListAdapterseconde(new ItemDiffCallback());
                    recyclerViewseconde.setAdapter(listAdapterseconde);
                    listAdapterseconde.submitList(categories1);
                    fabname.hide();
                    fabimage.hide();

                }
                fabname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common.names.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Select Items please", Toast.LENGTH_SHORT).show();
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerViewseconde.setVisibility(View.GONE);
                            openMenu();
                            isOpen1 = false;
                        } else {
                            //Toast.makeText(MainActivity.this, "Categories deleted", Toast.LENGTH_SHORT).show();
                            openMenu();
                            isOpen1 = false;
                            for (int i = 0; i < Common.names.size(); i++) {
                                Log.i("NAMES", "onClick: " + Common.names.get(i));
                            }
                            deleteCategory(Common.names);
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
                        Common.names.clear();
                        Common.categories.clear();
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerViewseconde.setVisibility(View.GONE);
                        title_recyvler_view.setVisibility(View.GONE);
                        listAdapterseconde = new ListAdapterseconde(new ItemDiffCallback());
                        recyclerViewseconde.setAdapter(listAdapterseconde);
                        listAdapterseconde.submitList(categories1);
                    }
                });
                break;
        }
    }


    @Override
    public void setClick(final Category category, int mode) {
        if (mode == 1) {
        } else {
            SharedPrefManager.getInstance(MainActivity.this).saveCategory(category);
            startActivity(new Intent(MainActivity.this, DrinkActivity.class));
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                RetrofitClient.getInstance().getApi().deleteOnecategory(item.getOrder())
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(MainActivity.this, "Item deleted" + response.message(), Toast.LENGTH_SHORT).show();
                                getCateogory();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                            }
                        });
            case R.id.action_update:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.update_category_item, null);
                final EditText editTextUpdateName = view.findViewById(R.id.edit_text_update_category);
                builder.setTitle("Update Category ");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final AlertDialog dialog1 = new SpotsDialog(MainActivity.this);
                        dialog1.show();
                        dialog1.setMessage("Updating ... ");
                        RetrofitClient.getInstance().getApi().updateCategory(item.getOrder(), editTextUpdateName.getText().toString())
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.code() == 200) {
                                            dialog1.dismiss();
                                            Toast.makeText(MainActivity.this, "Category Name Updated", Toast.LENGTH_SHORT).show();
                                            getCateogory();
                                        } else {
                                            dialog1.dismiss();
                                            Toast.makeText(MainActivity.this, "Category Not Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        dialog1.dismiss();
                                        Toast.makeText(MainActivity.this, "Error Network , Check Your Network Connexion", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setView(view);
                builder.show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_order :
                startActivity(new Intent(MainActivity.this,OrderActivity.class));
        }
        return true;
    }


}
