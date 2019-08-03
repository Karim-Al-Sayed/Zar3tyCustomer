package com.elasdka2.zar3tycustomer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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
    private DatabaseReference UserRef;
    String CurrentUserID, welcome_mail, welcome_name;

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

    }


    @Override
    public void onButtonClicked(String text) {

    }
}
