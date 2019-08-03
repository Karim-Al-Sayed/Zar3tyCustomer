package com.elasdka2.zar3tycustomer;

import android.annotation.SuppressLint;
import android.content.Context;
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

import androidx.annotation.Nullable;

import com.elasdka2.zar3tycustomer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.mohammedalaa.valuecounterlib.ValueCounterView;

import java.util.HashMap;

import edu.counterview.CounterListner;
import edu.counterview.CounterView;

public class RequestBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetListener myListener;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, ComplaintRef;
    TextView Price;
    Button done, cancel;
    CounterView cv;
    TextWatcher txtListener;
    String ImgUser, ImgItem, UserName;
    Integer itemPrice, TotalPrice, quantity2, q = 1;

    private DatabaseReference Ref_pending;


    private void CheckDataAndSend() {
        if (getArguments() != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemTitle", getArguments().getString("Item_Title"));
            map.put("ItemIMG", getArguments().getString("Item_IMG"));
            map.put("RequestDate", getArguments().getString("Request_Date"));
            map.put("State", "Pending");
            map.put("CustomerID", getArguments().getString("CustomerID"));
            map.put("SellerID", getArguments().getString("SellerID"));
            map.put("ItemPrice", String.valueOf(CalcSalary(itemPrice, q)));
            map.put("CustomerName", UserName);
            map.put("CustomerImg", ImgUser);
            map.put("ItemQuantity", q.toString());
            Ref_pending.push().setValue(map);
            dismiss();
        }


    }


    private Integer CalcSalary(Integer p, Integer q2) {
        itemPrice = p;
        quantity2 = q2;
        TotalPrice = itemPrice * quantity2;
        return TotalPrice;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.request_bottom_sheet, container, false);
        cv = (CounterView) v.findViewById(R.id.cv);
        Price = v.findViewById(R.id.totalSalary);

        cv.setStartCounterValue("1");
        cv.setColor(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);
        cv.setCounterListener(new CounterListner() {
            @Override
            public void onIncClick(String s) {
                q = q + 1;
                Price.setText(CalcSalary(itemPrice, q).toString());

            }

            @Override
            public void onDecClick(String s) {
                if (q == 1) return;
                else {
                    q = q - 1;
                    Price.setText(CalcSalary(itemPrice, q).toString());
                }
            }
        });


        done = v.findViewById(R.id.Quantity_Accept);
        cancel = v.findViewById(R.id.Quantity_Reject);
        mAuth = FirebaseAuth.getInstance();

        Ref_pending = FirebaseDatabase.getInstance().getReference("Pending Requests");
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        UserRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserNamee = dataSnapshot.child("UserName").getValue(String.class);
                String imgg = dataSnapshot.child("ImgUri").getValue(String.class);
                UserName = UserNamee;
                ImgUser = imgg;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Price.setText(String.valueOf(getArguments().getInt("Price")));
        itemPrice = (getArguments().getInt("Price"));


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
