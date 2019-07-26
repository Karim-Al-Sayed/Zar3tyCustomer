package com.elasdka2.zar3tycustomer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elasdka2.zar3tycustomer.Adapters.AllItemsAdapter;
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
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Context context;
    String CurrentUserID;

    private DatabaseReference Sales_Ref;

    private FirebaseAuth SalesAuth;

    ArrayList<Items> itemlist;

    @BindView(R.id.Home_Recycler)
    RecyclerView home_recycler;


    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();
        ButterKnife.bind(this, view);
        itemlist = new ArrayList<>();
        SalesAuth = FirebaseAuth.getInstance();
        Sales_Ref = FirebaseDatabase.getInstance().getReference("Sales");
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        home_recycler.setLayoutManager(linearLayoutManager);

        Sales_Ref.child("Agriculture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                    String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                    String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                    String category = dataSnapshot1.child("Category").getValue(String.class);
                    // String item_quantity = dataSnapshot1.child("Item_Quantity").getValue().toString();
                    String id = dataSnapshot1.child("User_ID").getValue(String.class);
                    String upload_date = dataSnapshot1.child("Upload_Date").getValue(String.class);
                    String upload_date_show = dataSnapshot1.child("Upload_Date_To_Show").getValue(String.class);
                    String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                    Items items = new Items();
                    items.setTitle(item_title);
                    items.setPrice(item_price);
                    items.setDescription(item_description);
                    items.setCategory(category);
                    items.setMaincategory("Industrial_Agriculture");
                    items.setId(id);
                    items.setDate(upload_date);
                    items.setDate_to_show(upload_date_show);
                    items.setImg_uri(img_uri);
                    itemlist.add(items);

                }
                AllItemsAdapter adapter = new AllItemsAdapter(itemlist,getActivity());
                home_recycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Sales_Ref.child("Irrigation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                    String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                    String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                    String category = dataSnapshot1.child("Category").getValue(String.class);
                    // String item_quantity = dataSnapshot1.child("Item_Quantity").getValue().toString();
                    String id = dataSnapshot1.child("User_ID").getValue(String.class);
                    String upload_date = dataSnapshot1.child("Upload_Date").getValue(String.class);
                    String upload_date_show = dataSnapshot1.child("Upload_Date_To_Show").getValue(String.class);
                    String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                    Items items = new Items();
                    items.setTitle(item_title);
                    items.setPrice(item_price);
                    items.setDescription(item_description);
                    items.setCategory(category);
                    items.setMaincategory("Industrial_Agriculture");
                    items.setId(id);
                    items.setDate(upload_date);
                    items.setDate_to_show(upload_date_show);
                    items.setImg_uri(img_uri);
                    itemlist.add(items);

                }
                AllItemsAdapter adapter = new AllItemsAdapter(itemlist,getActivity());
                home_recycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Sales_Ref.child("Industrial_Agriculture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                    String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                    String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                    String category = dataSnapshot1.child("Category").getValue(String.class);
                    // String item_quantity = dataSnapshot1.child("Item_Quantity").getValue().toString();
                    String id = dataSnapshot1.child("User_ID").getValue(String.class);
                    String upload_date = dataSnapshot1.child("Upload_Date").getValue(String.class);
                    String upload_date_show = dataSnapshot1.child("Upload_Date_To_Show").getValue(String.class);
                    String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                    Items items = new Items();
                    items.setTitle(item_title);
                    items.setPrice(item_price);
                    items.setDescription(item_description);
                    items.setCategory(category);
                    items.setMaincategory("Industrial_Agriculture");
                    items.setId(id);
                    items.setDate(upload_date);
                    items.setDate_to_show(upload_date_show);
                    items.setImg_uri(img_uri);
                    itemlist.add(items);

                }
                AllItemsAdapter adapter = new AllItemsAdapter(itemlist,getActivity());
                home_recycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed (Uri uri){
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);

        }

        @Override
        public void onDetach () {
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
