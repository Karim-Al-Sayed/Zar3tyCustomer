package com.elasdka2.zar3tycustomer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.elasdka2.zar3tycustomer.Adapters.ChatAdapter;
import com.elasdka2.zar3tycustomer.Model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DatabaseReference MyRef;
    private FirebaseUser MyUser;
    Context context;
    String MyUserID,UserID,intent;
    ChatAdapter messageAdapter;
    ArrayList<Chat> mchat;


    //APIService apiService;
   // boolean notify = false;
    //----------------------------------


    @BindView(R.id.message_message_edt)
    EditText Send_EditText;
    @BindView(R.id.message_linear_send_btn)
    ImageButton Send_btn;
    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;

    @OnClick(R.id.message_linear_send_btn)
    public void Send(){
       // notify = true;
        String msg = Send_EditText.getText().toString();
        if (TextUtils.isEmpty(msg)){
            Toast.makeText(context, "please enter your message !", Toast.LENGTH_LONG).show();
            Send_EditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UserID)){
            Toast.makeText(context, "To User ID Is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        SendMessage(MyUserID,UserID,msg);

    }



    public ChatFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chat_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFrag newInstance(String param1, String param2) {
        ChatFrag fragment = new ChatFrag();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,v);
        context = getActivity();
        MyUser = FirebaseAuth.getInstance().getCurrentUser();
        MyUserID = MyUser.getUid();
       if(getArguments()!=null){
           intent=getArguments().getString("UniqueID");
           if(!TextUtils.isEmpty(intent)){
               if(intent.equals("from_Item_Info")){
                   UserID=getArguments().getString("SellerID");
               }

               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
               linearLayoutManager.setStackFromEnd(true);
               message_recycler.setLayoutManager(linearLayoutManager);

               ReadMessage(MyUserID,UserID);
              // UpdateToken(FirebaseInstanceId.getInstance().getToken());

           }
       }

        return v;
    }


    private void SendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> ChatMap = new HashMap<>();
        ChatMap.put("sender", sender);
        ChatMap.put("receiver", receiver);
        ChatMap.put("message", message);
        reference.child("Chats").push().setValue(ChatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Send_EditText.setText("");
                }
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

                    if (chat.getReceiver().equals(CustomerID) && chat.getSender().equals(SellerID) ||
                            chat.getReceiver().equals(SellerID) && chat.getSender().equals(CustomerID)){
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
                .child(MyUserID)
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
                .child(MyUserID);
        chatRefReceiver.child("User_ID").setValue(MyUserID);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
