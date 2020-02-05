package com.example.drinkshopserver.Retrofit;

import com.example.drinkshopserver.Models.Category;
import com.example.drinkshopserver.Models.CategoryResponse;
import com.example.drinkshopserver.Models.DeleteCategoryResponse;
import com.example.drinkshopserver.Models.Drink;
import com.example.drinkshopserver.Models.DrinkResponse;
import com.example.drinkshopserver.Models.Order;
import com.example.drinkshopserver.Models.OrderResponse;
import com.example.drinkshopserver.Models.OrderResult;
import com.example.drinkshopserver.Models.Tocken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("addCategory.php")
    Call<String> addCategory(
            @Field("Name") String Name,
            @Field("Link") String Link
    );

    @GET("getMenu.php")
    Observable<CategoryResponse> getCategory();

    @FormUrlEncoded
    @POST("deleteCategory.php")
    Call<String> deleteCategpry(@Field("Name") String name);

    @FormUrlEncoded
    @POST("chekMenu.php")
    Call<CategoryResponse> chekMenu(@Field("Name") String name);

    @FormUrlEncoded
    @POST("updateCategory.php")
    Call<String> updateCategory(@Field("Name") int id, @Field("NewName") String newName);

    @FormUrlEncoded
    @POST("getDrink.php")
    Observable<DrinkResponse> getDrink(@Field("MenuID") String menuID);

    @FormUrlEncoded
    @POST("addDrink.php")
    Call<String> addDrink(
            @Field("Name") String name,
            @Field("Link") String link,
            @Field("Price") float price,
            @Field("MenuID") int MenuID
    );

    @FormUrlEncoded
    @POST("updateDrink.php")
    Call<String> updateDrink(@Field("Name") int id, @Field("NewName") String newName , @Field("Price") String newPrice);

    @FormUrlEncoded
    @POST("deleteDrink.php")
    Call<String> deleteDrinks(@Field("Name") String name);

    @FormUrlEncoded
    @POST("deleteOnecategory.php")
    Call<String> deleteOnecategory(@Field("Name") int name);

    @FormUrlEncoded
    @POST("deleteOneDrink.php")
    Call<String> deleteOneDrink(@Field("Name") int name);

    @FormUrlEncoded
    @POST("getOrdersByStatus.php")
    Observable<OrderResponse> getOrders(@Field("Status") String status);

    @FormUrlEncoded
    @POST("insertToken.php")
    Call<String> isertToken(
            @Field("Phone") String phone,
            @Field("Token") String Token,
            @Field("isServer") String isServer
    );

    @FormUrlEncoded
    @POST("updateOrder.php")
    Call<String> updateOrder(
            @Field("Status") String status,
            @Field("OrderID") String orderid
    );

    @FormUrlEncoded
    @POST("getToken.php")
    Call<Tocken> getToken(
            @Field("Phone") String phone
    );

    @FormUrlEncoded
    @POST("getOneOrder.php")
    Call<OrderResult> getOneOrder(
            @Field("OrderID") String orderID
    );

}
