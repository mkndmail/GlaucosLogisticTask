package com.example.mukundtask.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Mukund, mkndmail@gmail.com on 04-06-2020.
 */
public interface ApiService {

    @GET("email/")
    Call<List<ApiResponse>> getEmails();

    @POST("email/")
    Call<ApiResponse> addEmail(@Body JsonObject jsonObject);

    @PUT("email/{emailToUpdate}")
    Call<ApiResponse> updateEmail(@Path("emailToUpdate") String emailToUpdate, @Body JsonObject jsonObject);

    @DELETE("email/{emailToUpdate}")
    Call<JsonElement> deleteEmail(@Path("emailToUpdate") String emailToUpdate);
}
