package com.elasdka2.zar3tycustomer;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tycustomer.Adapters.DisplayChatAdapter;
import com.elasdka2.zar3tycustomer.Helper.ChatsCustomerRecylcerItemTouchHelper;
import com.elasdka2.zar3tycustomer.Helper.ChatsRecylcerItemTouchHelperListener;
import com.elasdka2.zar3tycustomer.Model.ChatList;
import com.elasdka2.zar3tycustomer.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyChats.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyChats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyChats extends Fragment implements ChatsRecylcerItemTouchHelperListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //----------------------------------------------
    Context context;
    @BindView(R.id.Chats_Recycler)
    RecyclerView recyclerView;
    @BindView(R.id.display_chat_customer_activity)
    ConstraintLayout constraintLayout;
    //----------------------------
    private DisplayChatAdapter userAdapter;
    private List<Users> mUsers;
    private List<ChatList> userList;
    private FirebaseUser fuser;
    private DatabaseReference reference,reference2;

    //----------------------------
    public MyChats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyChats.
     */
    // TODO: Rename and change types and number of parameters
    public static MyChats newInstance(String param1, String param2) {
        MyChats fragment = new MyChats();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.my_chats_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this,v);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //--------------------------------
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
        reference2 = FirebaseDatabase.getInstance().getReference("ChatList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.child("User_ID").getValue(String.class);
                    ChatList chatList = new ChatList();
                    chatList.setUser_ID(id);
                    userList.add(chatList);

                }
                //Toast.makeText(context,String.valueOf(userList.size()),Toast.LENGTH_LONG).show();

                chatList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }
    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String user_id = ds.child("User_ID").getValue(String.class);
                    String user_name = ds.child("UserName").getValue(String.class);
                    String user_img = ds.child("ImgUri").getValue(String.class);

                    Users users = new Users();
                    users.setUser_ID(user_id);
                    users.setUserName(user_name);
                    users.setImgUri(user_img);

                    for (ChatList chatList : userList){
                        //Toast.makeText(context,users.getUser_ID() + "\n" + chatList.getUser_ID(),Toast.LENGTH_SHORT).show();

                        if (users.getUser_ID().equals(chatList.getUser_ID())){
                            mUsers.add(users);
                        }
                    }
                }
                userAdapter = new DisplayChatAdapter(context,mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback
                = new ChatsCustomerRecylcerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DisplayChatAdapter.ViewHolder){
            String UserName = mUsers.get(viewHolder.getAdapterPosition()).getFirstName() + " " +
                    mUsers.get(viewHolder.getAdapterPosition()).getLastName();
            String UserID = mUsers.get(viewHolder.getAdapterPosition()).getUser_ID();

            final Users DeletedUser = mUsers.get(viewHolder.getAdapterPosition());
            final  int  DeleteIndex = viewHolder.getAdapterPosition();

            userAdapter.RemoveChat(DeleteIndex);
            reference2.child(fuser.getUid()).child(UserID).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    reference2.child(UserID).child(fuser.getUid()).removeValue().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){

                            Snackbar snackbar =
                                    Snackbar.make(constraintLayout,UserName + " Has Been Removed !",Snackbar.LENGTH_LONG);
                                   /*snackbar.setAction("UNDO", v -> userAdapter.RestoreChat(DeletedUser,DeleteIndex));
                                     snackbar.setActionTextColor(Color.YELLOW);*/
                            snackbar.show();
                        }else Toast.makeText(context, Objects.requireNonNull(task1.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    });

                }else Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            });
           /* Snackbar snackbar =
                    Snackbar.make(constraintLayout,UserName + " Has Been Removed !",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", v -> userAdapter.RestoreChat(DeletedUser,DeleteIndex));
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //--------------------------------
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.child("User_ID").getValue(String.class);
                    ChatList chatList = new ChatList();
                    chatList.setUser_ID(id);
                    userList.add(chatList);

                }
                //Toast.makeText(context,String.valueOf(userList.size()),Toast.LENGTH_LONG).show();

                chatList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Personal fragment = new Personal();
                FragmentTransaction fragmentTransaction1 = null;
                if (getFragmentManager() != null) {
                    fragmentTransaction1 = getFragmentManager().beginTransaction();
                }
                if (fragmentTransaction1 != null) {
                    fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                    fragmentTransaction1.commit();
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
