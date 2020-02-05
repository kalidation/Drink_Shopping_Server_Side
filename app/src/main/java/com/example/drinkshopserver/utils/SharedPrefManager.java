package com.example.drinkshopserver.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.drinkshopserver.Models.Category;

public class SharedPrefManager {

    private static final String SHARED_PREF_ONE = "shared_category";
    private static SharedPrefManager mInstance;
    private Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveCategory(Category category){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_ONE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("ID",category.getiD());
        editor.putString("Name",category.getName());
        editor.putString("Linl",category.getLink());

        editor.apply();
    }

    public Category getCategory(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_ONE,Context.MODE_PRIVATE);

        Category category = new Category(
                sharedPreferences.getInt("ID",-1),
                sharedPreferences.getString("Name",""),
                sharedPreferences.getString("Link","")
        );
        return category;
    }

}
