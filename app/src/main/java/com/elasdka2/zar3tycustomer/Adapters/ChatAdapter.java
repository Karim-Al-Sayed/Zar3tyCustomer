package com.elasdka2.zar3tycustomer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tycustomer.Model.Chat;
import com.elasdka2.zar3tycustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    private List<Chat> mChat;
    FirebaseUser MyUser;
    String id;


    public ChatAdapter(ArrayList<Chat> chatList, Context context) {
        this.mChat = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View ItemView = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new MyViewHolder(ItemView);
        }else {
            View ItemView = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MyViewHolder(ItemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MyUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getFrom().equals(MyUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.message.setText(chat.getMessage());

        if (position == mChat.size() - 1){

            if (chat.isIsseen()){
                holder.seen.setText("Seen");
            }else holder.seen.setText("Delivered");

        }else holder.seen.setVisibility(View.GONE);

}

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message, username,seen;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.show_message);
            username = itemView.findViewById(R.id.chat_item_left_user_name);
            seen = itemView.findViewById(R.id.txt_seen);

        }
    }
}
