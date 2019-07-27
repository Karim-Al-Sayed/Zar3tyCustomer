package com.elasdka2.zar3tycustomer.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elasdka2.zar3tycustomer.ChatAct;
import com.elasdka2.zar3tycustomer.Model.Chat;
import com.elasdka2.zar3tycustomer.Model.Users;
import com.elasdka2.zar3tycustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayChatAdapter extends RecyclerView.Adapter<DisplayChatAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers;
    private String theLastMessage;
    public DisplayChatAdapter(Context context, List<Users> mUsers){
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_user_row,parent,false);

        return new DisplayChatAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = mUsers.get(position);
        holder.UserName.setText(user.getUserName());
        Glide.with(context).load(user.getImgUri()).into(holder.UserImg);
        lastMessage(user.getUser_ID(), holder.LastMsg);

        holder.ChatCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatAct.class);
            intent.putExtra("UniqueID","from_UserAdapter");
            intent.putExtra("SellerID",user.getUser_ID());
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
       return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView UserName,LastMsg;
            CircleImageView UserImg;
            CardView ChatCard;
            public ConstraintLayout view_foreground, view_background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.user_username_row);
            LastMsg = itemView.findViewById(R.id.user_last_msg_row);
            UserImg = itemView.findViewById(R.id.user_profile_img_row);
            ChatCard = itemView.findViewById(R.id.chat_card);
            view_foreground = itemView.findViewById(R.id.view_foreground);
            view_background = itemView.findViewById(R.id.view_background);
        }
    }
    //check for last message
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                if ("default".equals(theLastMessage)) {
                    last_msg.setText("No Message");
                } else {
                    last_msg.setText(theLastMessage);
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void RemoveChat(int position){
        mUsers.remove(position);
        notifyItemRemoved(position);
    }

    public void RestoreChat(Users user, int position){
        mUsers.add(position,user);
        notifyItemInserted(position);
    }
}
