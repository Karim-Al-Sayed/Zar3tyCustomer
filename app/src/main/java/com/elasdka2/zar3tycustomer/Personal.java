package com.elasdka2.zar3tycustomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Personal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Personal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //--------------------------------------------------
    Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    private String CurrentUserID;
    private String ImgUri, UserName, Phone, Country, Mail;

   /* @BindView(R.id.adView)
    AdView mAdView;*/
    @BindView(R.id.welcome_name_txt)
    TextView welcome_name;
    @BindView(R.id.email_txt)
    TextView welcome_mail;
    @BindView(R.id.sign_out_text)
    TextView sign_out;
    @BindView(R.id.UpdateDataCard)
    CardView UpdateCard;
    @BindView(R.id.FavoriteItemsCard)
    CardView FavoriteCard;
    @BindView(R.id.MyChatsCard)
    CardView MyChatsCard;
    @BindView(R.id.MyRatesCard)
    CardView MyRatesCard;
    @BindView(R.id.MyOrdersCard)
    CardView OrdersCard;
    private boolean doubleBackToExitPressedOnce = false;

    //--------------------------------------------------
    @OnClick(R.id.UpdateDataCard)
    public void GoToUpdateData() {
        UpdateUserData fragment = new UpdateUserData();
        Bundle args = new Bundle();
        args.putString("UserName", UserName);
        args.putString("UserPhone", Phone);
        args.putString("UserCountry", Country);
        args.putString("UserImg", ImgUri);
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction1 = null;
        if (getFragmentManager() != null) {
            fragmentTransaction1 = getFragmentManager().beginTransaction();
        }
        if (fragmentTransaction1 != null) {
            fragmentTransaction1.replace(R.id.Frame_Content, fragment);
            fragmentTransaction1.commit();
        }

    }
    @OnClick(R.id.FavoriteItemsCard)
    public void GoToFavorite() {
        FavoriteItems fragment = new FavoriteItems();
        FragmentTransaction fragmentTransaction1 = null;
        if (getFragmentManager() != null) {
            fragmentTransaction1 = getFragmentManager().beginTransaction();
        }
        if (fragmentTransaction1 != null) {
            fragmentTransaction1.replace(R.id.Frame_Content, fragment);
            fragmentTransaction1.commit();
        }
    }
    @OnClick(R.id.MyChatsCard)
    public void GoToMyChats() {
        MyChats fragment = new MyChats();
        FragmentTransaction fragmentTransaction1 = null;
        if (getFragmentManager() != null) {
            fragmentTransaction1 = getFragmentManager().beginTransaction();
        }
        if (fragmentTransaction1 != null) {
            fragmentTransaction1.replace(R.id.Frame_Content, fragment);
            fragmentTransaction1.commit();
        }
    }
    @OnClick(R.id.MyRatesCard)
    public void GoToMyRates(){
        Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.MyOrdersCard)
    public void GoToOrders(){
Intent intent=new Intent(context,followRequestRecycler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @OnClick(R.id.sign_out_text)
    public void SignOut(){
        UserRef.child(CurrentUserID).child("token_id").removeValue().addOnCompleteListener(task -> {
            mAuth.signOut();
            Intent intent = new Intent(context,Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            ((Activity)context).finish();
        });
    }
    //--------------------------------------------------

    public Personal() {
        // Required empty public constructor
    }


    public static Personal newInstance(String param1, String param2) {
        Personal fragment = new Personal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.personal_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        CurrentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("CurrentUser", MODE_PRIVATE);
        welcome_name.setText(pref.getString("UserName",""));
        welcome_mail.setText(pref.getString("UserMail",""));

       /* if (getArguments() != null){
            welcome_name.setText(getArguments().getString("CurrentUserName"));
            welcome_mail.setText(getArguments().getString("CurrentUserMail"));
        }*/

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*MobileAds.initialize(context, "ca-app-pub-9443216844473207~5221027236");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/



        UserRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot UsersSnap) {

                String user_name = UsersSnap.child("UserName").getValue(String.class);
                String user_phone = UsersSnap.child("Phone").getValue(String.class);
                String user_country = UsersSnap.child("Country").getValue(String.class);
                String user_img_uri = UsersSnap.child("ImgUri").getValue(String.class);

                UserName = user_name;
                Phone = user_phone;
                Country = user_country;
                ImgUri = user_img_uri;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onResume() {

        super.onResume();

        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

              /*  SelectCategory fragment = new SelectCategory();
                FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.Frame_Content, fragment, "SelectCategory");
                fragmentTransaction1.commit();*/
                if (doubleBackToExitPressedOnce) {
                    ((Activity)context).moveTaskToBack(true);

                }else {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                }


                return true;

            }

            return false;
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
