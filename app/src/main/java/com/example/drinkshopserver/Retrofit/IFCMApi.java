package com.example.drinkshopserver.Retrofit;

import com.example.drinkshopserver.Models.DataMessage;
import com.example.drinkshopserver.Models.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization: key=AAAANsEOpqE:APA91bHl7f2OHf6UfhSGtPIfan_HwU6MH8XrfKjDYJEhqShZHkA5Hsz-gCZwB1Ex2eQRP9NdoYyAsOzBtLc9BVvXqtDuHAGLCyCQCYu2d8J5Ojz_kG4Z-1VrT4Q7p3I-lJpqLgT6U8Kg"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);

}
