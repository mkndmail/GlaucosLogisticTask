package com.example.mukundtask.network;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Mukund, mkndmail@gmail.com on 04-06-2020.
 */

//This is the POJO class of the ApiClient network calls
public class ApiResponse {
    @SerializedName("idtableEmail")
    public int idTableEmail;
    @SerializedName("tableEmailEmailAddress")
    public String emailAddress;
    @SerializedName("tableEmailValidate")
    public boolean isEmailValidate;
    /*0 for white background and 1 for grey background*/
    public int backGround = 1;

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
