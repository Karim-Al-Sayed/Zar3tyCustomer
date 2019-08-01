package com.elasdka2.zar3tycustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.elasdka2.zar3tycustomer.Adapters.followReqAdapter;
import com.elasdka2.zar3tycustomer.Model.Follow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class followRequestRecycler extends AppCompatActivity {
    ArrayList<Follow> followArrayList;
    followReqAdapter adapter;
    private RecyclerView recyclerView;
    Context context;
    TextView sstate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request_recycler);
        context = followRequestRecycler.this;
        sstate=findViewById(R.id.Request_state);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orders_Ref = database.getReference("Orders");
        DatabaseReference pending_Ref = database.getReference("Pending Requests");
        followArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.followReq_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orders_Ref.orderByChild("CustomerID").equalTo(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String title = (String) dataSnapshot1.child("ItemTitle").getValue();
                    String state = (String) dataSnapshot1.child("State").getValue();
                    String img = (String) dataSnapshot1.child("ItemIMG").getValue();
                    Follow follow=new Follow();
                    follow.setTitle(title);
                    follow.setState(state);
                    follow.setImg(img);

                    followArrayList.add(follow);
                }
                adapter = new followReqAdapter(context,followArrayList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pending_Ref.orderByChild("CustomerID").equalTo(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String title = (String) dataSnapshot1.child("ItemTitle").getValue();
                    String state = (String) dataSnapshot1.child("State").getValue();
                    String img = (String) dataSnapshot1.child("ItemIMG").getValue();
                    Follow follow=new Follow();
                    follow.setTitle(title);
                    follow.setState(state);
                    follow.setImg(img);
                    followArrayList.add(follow);
                }
                adapter = new followReqAdapter(context,followArrayList);
                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }
}
