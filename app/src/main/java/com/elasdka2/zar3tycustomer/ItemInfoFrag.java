package com.elasdka2.zar3tycustomer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemInfoFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemInfoFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //-----------------------------------------------
    private FirebaseAuth mAuth;
    private DatabaseReference MyRef;
    private Uri uri;
    private String MainCategory;
    private String DateToShow;
    private String intent_from;
    private String part1;
    private String part2;
    private String seller_id;
    Context context;
    //-----------------------------------------------
    @BindView(R.id.sales_item_info_img)
    ImageView item_info_img;
    @BindView(R.id.sales_item_info_title_value)
    TextView item_info_title;
    @BindView(R.id.sales_item_info_description_value)
    TextView item_info_description;
    @BindView(R.id.sales_item_info_price_value)
    TextView item_info_price;
    @BindView(R.id.sales_item_info_category_value)
    TextView item_info_category;
    @BindView(R.id.sales_item_info_quantity_value)
    TextView item_info_quantity;
    @BindView(R.id.sales_item_info_date_value)
    TextView item_info_date;
    @BindView(R.id.sales_item_info_contact_seller)
    Button contact_seller;
    private boolean doubleBackToExitPressedOnce = false;

    //-----------------------------------------------
    @OnClick(R.id.sales_item_info_contact_seller)
    public void Contact(){
        /*Bundle args = new Bundle();
        args.putString("SellerID",seller_id);
        args.putString("UniqueID","from_Item_Info");
        ChatFrag fragment = new ChatFrag();
        FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.constraint_nav, fragment);
        fragment.setArguments(args);
        fragmentTransaction1.commit();*/
        Intent intent = new Intent(context,ChatAct.class);
        intent.putExtra("SellerID",seller_id);
        intent.putExtra("UniqueID","from_Item_Info");
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    //-----------------------------------------------
    public ItemInfoFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment itemInfoFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemInfoFrag newInstance(String param1, String param2) {
        ItemInfoFrag fragment = new ItemInfoFrag();
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
        View v = inflater.inflate(R.layout.fragment_item_info, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();
        MyRef = FirebaseDatabase.getInstance().getReference("Sales");
        if (getArguments() != null){
            intent_from = getArguments().getString("UniqueID");

            if (!TextUtils.isEmpty(intent_from)){


                if (intent_from != null) {
                    switch (intent_from) {
                        case "from_AgricultureItemsAdapter": {
                            item_info_title.setText(getArguments().getString("ItemTitle"));
                            item_info_description.setText(getArguments().getString("ItemDescription"));
                            item_info_price.setText(getArguments().getString("ItemPrice"));
                            item_info_category.setText(getArguments().getString("ItemCategory"));
                            item_info_date.setText(getArguments().getString("ItemDate"));
                            MainCategory = getArguments().getString("ItemMainCategory");
                            DateToShow = getArguments().getString("ItemDate");
                            Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);

                            break;
                        }
                        case "from_ItemsAdapter": {
                            item_info_title.setText(getArguments().getString("ItemTitle"));
                            item_info_description.setText(getArguments().getString("ItemDescription"));
                            item_info_price.setText(getArguments().getString("ItemPrice"));
                            seller_id = getArguments().getString("SellerID");
                            item_info_category.setText(getArguments().getString("ItemCategory"));
                            item_info_date.setText(getArguments().getString("ItemDate"));
                            MainCategory = getArguments().getString("ItemMainCategory");
                            DateToShow = getArguments().getString("ItemDate");
                            Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);

                            break;
                        }
                        case "from_IrrigationItemsAdapter": {
                            item_info_title.setText(getArguments().getString("ItemTitle"));
                            item_info_description.setText(getArguments().getString("ItemDescription"));
                            item_info_price.setText(getArguments().getString("ItemPrice"));

                            String string = item_info_price.getText().toString();
                            String[] parts = string.split(" ");
                            part1 = parts[0]; // Money

                            part2 = parts[1]; // LE


                            item_info_category.setText(getArguments().getString("ItemCategory"));
                            item_info_date.setText(getArguments().getString("ItemDate"));
                            MainCategory = getArguments().getString("ItemMainCategory");
                            DateToShow = getArguments().getString("ItemDate");
                            Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);

                            break;
                        }
                        case "from_IndustrialAgricultureItemsAdapter": {
                            item_info_title.setText(getArguments().getString("ItemTitle"));
                            item_info_description.setText(getArguments().getString("ItemDescription"));
                            item_info_price.setText(getArguments().getString("ItemPrice"));

                            String string = item_info_price.getText().toString();
                            String[] parts = string.split(" ");
                            part1 = parts[0]; // Money

                            part2 = parts[1]; // LE


                            item_info_category.setText(getArguments().getString("ItemCategory"));
                            item_info_date.setText(getArguments().getString("ItemDate"));
                            MainCategory = getArguments().getString("ItemMainCategory");
                            DateToShow = getArguments().getString("ItemDate");
                            Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);

                            break;
                        }
                        case "from_UpdateItemData": {

                            item_info_title.setText(getArguments().getString("ItemTitle"));
                            item_info_description.setText(getArguments().getString("ItemDescription"));
                            item_info_price.setText(getArguments().getString("ItemPrice"));
                            String string = item_info_price.getText().toString();
                            String[] parts = string.split(" ");
                            part1 = parts[0]; // Money

                            part2 = parts[1]; // LE

                            item_info_category.setText(getArguments().getString("ItemCategory"));
                            item_info_date.setText(getArguments().getString("ItemDate"));
                            MainCategory = getArguments().getString("ItemMainCategory");
                            DateToShow = getArguments().getString("ItemDate");
                            Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);

                            break;
                        }
                    }
                }
            }
        }
        return v;
    }
    @Override
    public void onResume() {

        super.onResume();

        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                if (getArguments() != null){
                    switch (intent_from) {
                        case "from_AgricultureItemsAdapter": {
                            AgricultureItems fragment = new AgricultureItems();
                            FragmentTransaction fragmentTransaction1 = null;
                            if (getFragmentManager() != null) {
                                fragmentTransaction1 = getFragmentManager().beginTransaction();
                            }
                            if (fragmentTransaction1 != null) {
                                fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                                fragmentTransaction1.commit();
                            }
                            break;
                        }
                        case "from_IrrigationItemsAdapter": {
                            IrrigationItems fragment = new IrrigationItems();
                            FragmentTransaction fragmentTransaction1 = null;
                            if (getFragmentManager() != null) {
                                fragmentTransaction1 = getFragmentManager().beginTransaction();
                            }
                            if (fragmentTransaction1 != null) {
                                fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                                fragmentTransaction1.commit();
                            }
                            break;
                        }
                        case "from_IndustrialAgricultureItemsAdapter": {
                            IndustrialAgricultureItems fragment = new IndustrialAgricultureItems();
                            FragmentTransaction fragmentTransaction1 = null;
                            if (getFragmentManager() != null) {
                                fragmentTransaction1 = getFragmentManager().beginTransaction();
                            }
                            if (fragmentTransaction1 != null) {
                                fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                                fragmentTransaction1.commit();
                            }
                            break;
                        }
                        case "from_ItemsAdapter": {
                            Home fragment = new Home();
                            FragmentTransaction fragmentTransaction1 = null;
                            if (getFragmentManager() != null) {
                                fragmentTransaction1 = getFragmentManager().beginTransaction();
                            }
                            if (fragmentTransaction1 != null) {
                                fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                                fragmentTransaction1.commit();
                            }
                            break;
                        }
                    }

                    }else {
                    if (doubleBackToExitPressedOnce) {
                        ((Activity)context).moveTaskToBack(true);

                    }else {
                        this.doubleBackToExitPressedOnce = true;
                        Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                    }
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
