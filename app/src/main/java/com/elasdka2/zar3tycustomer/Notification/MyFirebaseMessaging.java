package com.elasdka2.zar3tycustomer.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.elasdka2.zar3tycustomer.Model.Tokens;
import com.elasdka2.zar3tycustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    boolean IsCustomer;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String CustomerName;
    NotificationManager notificationManager;
    public int NotificationID;
    DatabaseReference reference;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.child("Customers").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        CustomerName = ds.child("UserName").getValue(String.class);
                    }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String SellerID = remoteMessage.getData().get("SellerId");

        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String currentUser = preferences.getString("CustomerID", "none");
        if (!currentUser.equals(SellerID)) {
            SendNotification(remoteMessage);
        }
    }
    public void SendNotification(RemoteMessage remoteMessage){
        if(remoteMessage.getData().size() > 0 ) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            String CustomerID = remoteMessage.getData().get("CustomerId");
            String SellerID = remoteMessage.getData().get("SellerId");
            String click_Action = remoteMessage.getData().get("click_action");

            Intent intent = new Intent(click_Action);
            intent.putExtra("message", message);
            intent.putExtra("from_user_id", CustomerID);
            intent.putExtra("to_user_id", SellerID);
            intent.putExtra("UniqueID", "from_Messaging");

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
            }else {
                if (!TextUtils.isEmpty(CustomerName)){
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setVibrate(new long[]{1000, 1000})
                            .setContentTitle(CustomerName)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(0);

                    NotificationID = (int) System.currentTimeMillis();
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(NotificationID, mBuilder.build());
                }else {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setVibrate(new long[]{1000, 1000})
                            .setContentTitle("New Message !")
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(0);

                    NotificationID = (int) System.currentTimeMillis();
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(NotificationID, mBuilder.build());
                }

            }

            /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setVibrate(new long[] { 1000, 1000 })
                    .setContentTitle("New Message !")
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);*/

        }
    }


    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Tokens token = new Tokens(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);

    }
}
