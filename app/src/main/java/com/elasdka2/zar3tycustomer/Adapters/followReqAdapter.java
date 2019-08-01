package com.elasdka2.zar3tycustomer.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tycustomer.Model.Follow;
import com.elasdka2.zar3tycustomer.Model.Items;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class followReqAdapter extends RecyclerView.Adapter<followReqAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Follow> followsList;
    Follow follow;
    private DatabaseReference Ref;




    @NonNull
    @Override
    public followReqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull followReqAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
