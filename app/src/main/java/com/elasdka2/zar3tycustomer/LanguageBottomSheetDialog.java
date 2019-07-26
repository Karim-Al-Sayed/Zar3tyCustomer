package com.elasdka2.zar3tycustomer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LanguageBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener myListener;

    Button arabic_btn,english_btn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_bottom_sheet, container, false);
      //  ButterKnife.bind(this,v);

         arabic_btn = v.findViewById(R.id.arabic_language_btn);
         english_btn = v.findViewById(R.id.english_language_btn);

        arabic_btn.setOnClickListener(v1 -> {
            myListener.onButtonClicked("Arabic Clicked");
            dismiss();

        });
        english_btn.setOnClickListener(v12 -> {
            myListener.onButtonClicked("English Clicked");
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
