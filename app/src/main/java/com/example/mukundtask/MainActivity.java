package com.example.mukundtask;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mukundtask.adapter.TableViewAdapter;
import com.example.mukundtask.databinding.ActivityMainBinding;
import com.example.mukundtask.databinding.LayoutEditContentBinding;
import com.example.mukundtask.network.ApiResponse;
import com.example.mukundtask.viewmodel.MainViewModel;
import com.google.gson.JsonObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements TableViewAdapter.ClickListener {
    private ActivityMainBinding mBinding;
    private TableViewAdapter tableViewAdapter;
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.executePendingBindings();
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        tableViewAdapter = new TableViewAdapter(this);
        mBinding.rv.setAdapter(tableViewAdapter);
        mBinding.setMainViewModel(mMainViewModel);
        mBinding.setLifecycleOwner(this);
        mMainViewModel.progressBar().observe(this, showProgressBar -> {
            if (showProgressBar)
                mBinding.progressBar.setVisibility(View.VISIBLE);
            else
                mBinding.progressBar.setVisibility(View.GONE);
        });
        mMainViewModel.getApiResponse().observe(this, apiResponses -> {
            if (apiResponses.size() > 0) {
                tableViewAdapter.submitList(apiResponses);
            }
            else{
                mBinding.rv.setVisibility(View.GONE);
                mBinding.txtError.setVisibility(View.VISIBLE);
                mBinding.txtError.setText("No data found. Click to retry");
                mBinding.txtError.setOnClickListener(v -> mMainViewModel.getEmails());
            }
        });
        mMainViewModel.getError().observe(this, errorText -> {
            if (errorText != null) {
                mBinding.rv.setVisibility(View.GONE);
                mBinding.txtError.setVisibility(View.VISIBLE);
                mBinding.txtError.setText(errorText);

            } else {
                mBinding.txtError.setVisibility(View.GONE);
                mBinding.rv.setVisibility(View.VISIBLE);
            }
        });

        mBinding.btnAddEmail.setOnClickListener(click -> showAddAndEditDialogue(null));

    }

    @Override
    public void performClickAction(TableViewAdapter.Actions action, ApiResponse apiResponse) {
        if (action == TableViewAdapter.Actions.DELETE) {
            askForDeleteConfirmation(apiResponse);
        } else {
            showAddAndEditDialogue(apiResponse);
        }
    }

    private void askForDeleteConfirmation(ApiResponse apiResponse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirmation_text));
        builder.setMessage(getString(R.string.delete_msgconfirmation, apiResponse.emailAddress));
        builder.setPositiveButton(R.string.yes, (d, w) -> mMainViewModel.deleteEntry(apiResponse.idTableEmail));
        builder.setNegativeButton(getString(R.string.no), (d, w) -> builder.setOnCancelListener(DialogInterface::dismiss));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAddAndEditDialogue(ApiResponse apiResponse) {

        /*If Api Response is null, we are adding a new Entry
         * If Api response is not null, we are updating an Existing entry
         * */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutEditContentBinding binding = LayoutEditContentBinding.inflate(inflater, null, false);
        builder.setView(binding.getRoot()).setNegativeButton("Cancel", (d, w) -> builder.setOnCancelListener(DialogInterface::dismiss));
        builder.setCancelable(false);
        if (apiResponse != null) {
            binding.edtEmail.setText(apiResponse.emailAddress);
        }
        binding.button.setText(apiResponse == null ? "Save" : "Update");
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        binding.button.setOnClickListener(click -> {
            String emailText = Objects.requireNonNull(binding.edtEmail.getText()).toString();
            if (emailText.length() > 0) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    alertDialog.dismiss();
                    if (apiResponse != null) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("tableEmailEmailAddress", emailText);
                        jsonObject.addProperty("tableEmailValidate", true);
                        mMainViewModel.updateEmailDetails(apiResponse.idTableEmail,jsonObject);
                    } else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("tableEmailEmailAddress", emailText);
                        jsonObject.addProperty("tableEmailValidate", true);
                        mMainViewModel.addEmailEntry(jsonObject);
                    }
                } else {
                    binding.edtEmail.setError("Please enter a valid email address");
                }
            } else {
                binding.edtEmail.setError("Email address can't be empty");
            }
        });
    }
}