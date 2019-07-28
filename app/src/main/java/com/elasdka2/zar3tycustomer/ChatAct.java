package com.elasdka2.zar3tycustomer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.elasdka2.zar3tycustomer.Adapters.ChatAdapter;
import com.elasdka2.zar3tycustomer.Model.Chat;
import com.elasdka2.zar3tycustomer.Model.Tokens;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatAct extends AppCompatActivity {
    //-----------------------------------------
    private DatabaseReference MyRef, reference,NotificationRef;
    private FirebaseUser MyUser;
    Context context;
    String MyUserID, UserID, intent;
    ChatAdapter messageAdapter;
    ArrayList<Chat> mchat;
    boolean notify = false;

    //-----------------------------------------
    @BindView(R.id.message_message_edt)
    EditText Send_EditText;
    @BindView(R.id.message_linear_send_btn)
    ImageButton Send_btn;
    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;

    @OnClick(R.id.message_linear_send_btn)
    public void Send() {
        // notify = true;
        String msg = Send_EditText.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(context, "please enter your message !", Toast.LENGTH_LONG).show();
            Send_EditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UserID)) {
            Toast.makeText(context, "To User ID Is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        SendMessage(MyUserID, UserID, msg);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);
        ButterKnife.bind(this);
        context = ChatAct.this;
        NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        MyUser = FirebaseAuth.getInstance().getCurrentUser();
        if (MyUser != null) {
            MyUserID = MyUser.getUid();

        }

        if (getIntent() != null) {
            intent = Objects.requireNonNull(getIntent().getExtras()).getString("UniqueID");
            if (!TextUtils.isEmpty(intent)) {
                if (intent.equals("from_Item_Info")) {
                    UserID = getIntent().getExtras().getString("SellerID");
                } else if (intent.equals("from_UserAdapter")) {
                    UserID = getIntent().getExtras().getString("SellerID");
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setStackFromEnd(true);
                message_recycler.setLayoutManager(linearLayoutManager);

                ReadMessage(MyUserID, UserID);
                UpdateToken(FirebaseInstanceId.getInstance().getToken());

            }
        }

    }

    private void SendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> ChatMap = new HashMap<>();
        ChatMap.put("from", sender);
        ChatMap.put("to", receiver);
        ChatMap.put("message", message);
        reference.child("Chats").push().setValue(ChatMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Send_EditText.setText("");
               }
        });
    }

    private void ReadMessage(final String CustomerID, final String SellerID){
        mchat = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null && (chat.getTo().equals(CustomerID) && chat.getFrom().equals(SellerID) ||
                            chat.getTo().equals(SellerID) && chat.getFrom().equals(CustomerID))) {
                        mchat.add(chat);
                    }
                    messageAdapter = new ChatAdapter(mchat,context);
                    message_recycler.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(MyUser.getUid())
                .child(UserID);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("User_ID").setValue(UserID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(UserID)
                .child(MyUser.getUid());
        chatRefReceiver.child("User_ID").setValue(MyUser.getUid());
    }

    private void UpdateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Tokens token1 = new Tokens(token);
        reference.child(MyUserID).setValue(token1);
    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser(MyUserID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentUser("none");
    }
}
