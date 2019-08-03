package com.elasdka2.zar3tycustomer;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.elasdka2.zar3tycustomer.Model.Chat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Navigation extends AppCompatActivity implements ComplaintBottomSheetDialog.BottomSheetListener,
                            LanguageBottomSheetDialog.BottomSheetListener, RequestBottomSheet.BottomSheetListener{
    @BindView(R.id.bottom_nav)
    BottomNavigationView navigation;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,ChatRef;
    String CurrentUserID, welcome_mail, welcome_name;
    public int NotificationID;
    NotificationManager notificationManager;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.Home :
                    Home fragment1 = new Home();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.Frame_Content,fragment1);
                    fragmentTransaction1.commit();

                   // Toast.makeText(getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.Categories :
                    SelectCategory fragment2 = new SelectCategory();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.Frame_Content,fragment2);
                    fragmentTransaction2.commit();
                    return true;

                case R.id.Personal :
                    Personal fragment3 = new Personal();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.Frame_Content,fragment3);
                    fragmentTransaction3.commit();
                    return true;
                case R.id.Setting :
                    Setting fragment4 = new Setting();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.Frame_Content,fragment4);
                    fragmentTransaction4.commit();
                    return true;
            }

            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_act);
        ButterKnife.bind(this);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }*/

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Home fragment1 = new Home();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.Frame_Content,fragment1);
        fragmentTransaction1.commit();

        ComplaintBottomSheetDialog bottomSheetDialog = new ComplaintBottomSheetDialog();
        bottomSheetDialog.setCancelable(false);

        LanguageBottomSheetDialog languageBottomSheetDialog = new LanguageBottomSheetDialog();
        languageBottomSheetDialog.setCancelable(false);

        RequestBottomSheet requestQuantityBottomSheet = new RequestBottomSheet();
        requestQuantityBottomSheet.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        ChatRef = FirebaseDatabase.getInstance().getReference("Chats");
        CurrentUserID = mAuth.getCurrentUser().getUid();

        UserRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot UsersSnap) {

                String user_name = UsersSnap.child("UserName").getValue(String.class);
                String user_mail = UsersSnap.child("Mail").getValue(String.class);


                welcome_mail = user_mail;
                welcome_name = user_name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int unread = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getTo().equals(CurrentUserID) && !chat.isIsseen()){
                        unread++;
                    }

                }
                if (unread != 0){
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                        String CHANNEL_ID = "my_channel_01";
                        CharSequence name = "my_channel";
                        String Description = "This is my channel";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                        mChannel.setDescription(Description);
                        mChannel.enableLights(true);
                        mChannel.setLightColor(Color.RED);
                        mChannel.enableVibration(true);
                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        mChannel.setShowBadge(false);
                        notificationManager.createNotificationChannel(mChannel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Navigation.this,"my_channel_01")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setVibrate(new long[]{1000, 1000})
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentTitle("New Message")
                            .setContentText("You Have New Message");

                    NotificationID = (int) System.currentTimeMillis();
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.notify(NotificationID, builder.build());
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
