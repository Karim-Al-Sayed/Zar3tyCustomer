package com.elasdka2.zar3tycustomer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elasdka2.zar3tycustomer.Model.Follow;
import com.elasdka2.zar3tycustomer.Model.Items;
import com.elasdka2.zar3tycustomer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class followReqAdapter extends RecyclerView.Adapter<followReqAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Follow> followsList;
    Follow follow;
    String mImg;
    private DatabaseReference Ref;

    public followReqAdapter(Context context, ArrayList<Follow> followsList) {
        this.context = context;
        this.followsList = followsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.follow_request_row,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return new followReqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull followReqAdapter.ViewHolder holder, int position) {
        final Follow follow = followsList.get(position);
        holder.title.setText(follow.getTitle());
        holder.state.setText(follow.getState());
        Glide.with(context.getApplicationContext()).load(follow.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (followsList == null) return 0;
        return followsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,state;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.Request_itemTitle);
            state=itemView.findViewById(R.id.Request_state);
            img=itemView.findViewById(R.id.Request_itemImg);
        }
    }
}
