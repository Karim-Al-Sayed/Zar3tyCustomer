package com.elasdka2.zar3tycustomer.Model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elasdka2.zar3tycustomer.ItemInfoFrag;
import com.elasdka2.zar3tycustomer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.mohammedalaa.valuecounterlib.ValueCounterView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestQuantityBottomSheet extends BottomSheetDialogFragment {

    // ValueCounterView valueCounter= (ValueCounterView) findViewById(R.id.valueCounter);


    private BottomSheetListener myListener;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, ComplaintRef;
    TextView Price;
    Button done, cancel;
    EditText quantity;
    TextWatcher txtListener;
    String ImgUser, ImgItem, UserName;
    Integer itemPrice, TotalPrice, quantity2;

    private DatabaseReference Ref_pending;


    private void CheckDataAndSend() {
        String str_quantity = quantity.getText().toString();
        if (TextUtils.isEmpty(str_quantity)) {
            Toast.makeText(getActivity(), "Type quantity you need here Please !", Toast.LENGTH_LONG).show();
        } else {
            if (getArguments() != null) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Item_Title", getArguments().getString("Item_Title"));
                map.put("Item_IMG", getArguments().getString("Item_IMG"));
                map.put("Request_Date", getArguments().getString("Request_Date"));
                map.put("CustomerID", getArguments().getString("CustomerID"));
                map.put("SellerID", getArguments().getString("SellerID"));
                map.put("Price", CalcSalary(itemPrice) + " EGP");
                map.put("UserName", UserName);
                map.put("UserImg", ImgUser);
                map.put("Quantity", str_quantity);
                Ref_pending.push().setValue(map);
                dismiss();

            }


        }
    }

    private Integer CalcSalary(Integer p) {
        itemPrice = p;
        quantity2 = Integer.parseInt(quantity.getText().toString());
        TotalPrice = itemPrice * quantity2;
        return TotalPrice;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.complaint_bottom_sheet, container, false);

        done = v.findViewById(R.id.Quantity_Accept);
        cancel = v.findViewById(R.id.Quantity_Reject);
        Price = v.findViewById(R.id.totalSalary);
        quantity = v.findViewById(R.id.Quantity);
        mAuth = FirebaseAuth.getInstance();

        Ref_pending = FirebaseDatabase.getInstance().getReference("Pending Requests");
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        UserRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserNamee = dataSnapshot.child("UserName").getValue(String.class);
                String imgg = dataSnapshot.child("ImgUri").getValue(String.class);
                UserName=UserNamee;
                ImgUser=imgg;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Price.setText(String.valueOf(getArguments().getInt("Price")));
        itemPrice = (getArguments().getInt("Price"));

        txtListener = new TextWatcher() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                Price.setText(CalcSalary(itemPrice) + " LE");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                Price.setText(getArguments().getString("Price"));

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                Price.setText(CalcSalary(itemPrice) + "LE");

            }
        };
        quantity.addTextChangedListener(txtListener);
        done.setOnClickListener(v1 -> {

            CheckDataAndSend();

        });
        cancel.setOnClickListener(v12 -> {


            dismiss();
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            myListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }


}
