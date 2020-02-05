package com.example.drinkshopserver.utils;

import android.widget.CheckBox;

import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.Models.Drink;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static final String BASE_URL = "http://www.drinkshop.com.eu.loclx.io/drinkShop/";
    public static final String FCM_URL = "https://fcm.googleapis.com/";
    public static List<String>  names = new ArrayList<>();
    public static List<String>  drinksNames = new ArrayList<>();

    public static List<Category> categories = new ArrayList<>();
    public static List<Drink> drinks  = new ArrayList<>();



}
