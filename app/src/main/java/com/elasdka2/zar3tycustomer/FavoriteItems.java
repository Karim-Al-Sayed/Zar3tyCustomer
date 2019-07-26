package com.elasdka2.zar3tycustomer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elasdka2.zar3tycustomer.Adapters.FavoritesItemsAdapter;
import com.elasdka2.zar3tycustomer.Adapters.IrrigationItemsAdapter;
import com.elasdka2.zar3tycustomer.Model.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteItems.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteItems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteItems extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //-----------------------------------------------
    Context context;
    private DatabaseReference Favorites_Ref;
    ArrayList<Items> itemslist;
    private FirebaseAuth mAuth;
    String CurrentUserID;

    //-----------------------------------------------
    @BindView(R.id.favorite_recycler_frag)
    RecyclerView recyclerView;
    //-----------------------------------------------
    public FavoriteItems() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteItems.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteItems newInstance(String param1, String param2) {
        FavoriteItems fragment = new FavoriteItems();
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
        View v = inflater.inflate(R.layout.favorite_items_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();

        Favorites_Ref = FirebaseDatabase.getInstance().getReference("Favorites");
        itemslist = new ArrayList<>();

        Favorites_Ref.child(CurrentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                    String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                    String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                    String id = dataSnapshot1.child("User_ID").getValue(String.class);
                    String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                    Items items = new Items();
                    items.setTitle(item_title);
                    items.setPrice(item_price);
                    items.setDescription(item_description);
                    items.setId(id);
                    items.setImg_uri(img_uri);
                    itemslist.add(items);

                }
                FavoritesItemsAdapter adapter = new FavoritesItemsAdapter(itemslist,getActivity());
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
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
