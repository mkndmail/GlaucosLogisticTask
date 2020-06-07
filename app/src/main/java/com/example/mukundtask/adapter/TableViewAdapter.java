package com.example.mukundtask.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mukundtask.BR;
import com.example.mukundtask.databinding.TableEmailsListBinding;
import com.example.mukundtask.network.ApiResponse;

/**
 * Created by Mukund, mkndmail@gmail.com on 05-06-2020.
 */
public class TableViewAdapter extends ListAdapter<ApiResponse, RecyclerView.ViewHolder> {
    ClickListener clickListener;

    public TableViewAdapter(ClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TableEmailsListBinding tableEmailsListBinding = TableEmailsListBinding.inflate(layoutInflater, parent, false);
        return new TableViewHolder(tableEmailsListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApiResponse apiResponse = getItem(position);
        if (position % 2 == 0) {
            apiResponse.backGround = 0;
        }
        ((TableViewHolder) holder).bind(apiResponse,clickListener);
    }

    public static final DiffUtil.ItemCallback<ApiResponse> DIFF_CALLBACK = new DiffUtil.ItemCallback<ApiResponse>() {
        @Override
        public boolean areItemsTheSame(@NonNull ApiResponse oldItem, @NonNull ApiResponse newItem) {
            //if both the ids are the same they are the same element
            return oldItem.idTableEmail == newItem.idTableEmail;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ApiResponse oldItem, @NonNull ApiResponse newItem) {

            return oldItem.equals(newItem);
        }
    };

    static class TableViewHolder extends RecyclerView.ViewHolder {
        TableEmailsListBinding tableEmailsListBinding;

        public TableViewHolder(@NonNull TableEmailsListBinding binding) {
            super(binding.getRoot());
            tableEmailsListBinding = binding;
        }

        public void bind(ApiResponse apiResponse, ClickListener clickListener) {
            tableEmailsListBinding.setVariable(BR.apiResponse,apiResponse);
            tableEmailsListBinding.imgDelete.setOnClickListener(click -> clickListener.performClickAction(Actions.DELETE,apiResponse));
            tableEmailsListBinding.imgEdit.setOnClickListener(click -> clickListener.performClickAction(Actions.EDIT,apiResponse));
            tableEmailsListBinding.executePendingBindings();
        }

    }

    /*Enum for the Actions that user can perform in this Case Edit an entry or Delete an Entry*/
    public enum Actions {
        EDIT,
        DELETE
    }

//    Interface to handle click listener and pass the results back to the parent activity
    public interface ClickListener {
        void performClickAction(Actions action, ApiResponse apiResponse);
    }
}



