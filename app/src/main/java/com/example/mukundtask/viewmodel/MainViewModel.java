package com.example.mukundtask.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mukundtask.network.ApiClient;
import com.example.mukundtask.network.ApiResponse;
import com.example.mukundtask.network.ApiService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mukund, mkndmail@gmail.com on 04-06-2020.
 */
public class MainViewModel extends ViewModel {
    ApiService apiService;

    public MainViewModel() {
        getEmails();
    }

    private MutableLiveData<List<ApiResponse>> mApiResponse = new MutableLiveData<>();
    private MutableLiveData<String> errorText = new MutableLiveData<>();
    private MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();

    public LiveData<Boolean> progressBar() {
        return showProgressBar;
    }

    public LiveData<String> getError() {
        return errorText;
    }

    public LiveData<List<ApiResponse>> getApiResponse() {
        return mApiResponse;
    }

    public void getEmails() {
        showProgressBar.postValue(true);
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.getEmails().enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ApiResponse>> call, @NotNull Response<List<ApiResponse>> response) {
                showProgressBar.postValue(false);
                errorText.setValue(null);
                if (response.isSuccessful()) {
                    mApiResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ApiResponse>> call, @NotNull Throwable t) {
                showProgressBar.postValue(false);
                errorText.postValue(t.getMessage());
            }
        });
    }

    public void deleteEntry(int idTableEmail) {
        showProgressBar.postValue(true);
        apiService.deleteEmail(String.valueOf(idTableEmail)).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
                showProgressBar.postValue(false);
                if (response.isSuccessful()){
                    JsonElement jsonResponse=response.body();
                    getEmails();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                showProgressBar.postValue(false);
                errorText.postValue(t.getMessage());
            }
        });
    }

    public void updateEmailDetails(int idTableEmail, JsonObject jsonObject) {
        showProgressBar.postValue(true);
        apiService.updateEmail(String.valueOf(idTableEmail),jsonObject).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                showProgressBar.postValue(false);
                errorText.postValue(null);
                getEmails();
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                showProgressBar.postValue(false);
                errorText.postValue(t.getMessage());
            }
        });
    }

    public void addEmailEntry(JsonObject jsonObject) {
        showProgressBar.postValue(true);
        apiService.addEmail(jsonObject).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                showProgressBar.postValue(false);
                errorText.postValue(null);
                getEmails();
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                showProgressBar.postValue(false);
                errorText.postValue(t.getMessage());
            }
        });
    }
}
