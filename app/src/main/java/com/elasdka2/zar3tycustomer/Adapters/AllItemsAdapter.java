package com.elasdka2.zar3tycustomer.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elasdka2.zar3tycustomer.ItemInfoFrag;
import com.elasdka2.zar3tycustomer.Model.Items;
import com.elasdka2.zar3tycustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.MyViewHolder> {
    private ArrayList<Items> itemsList;
    private Context context;
    Items item;
    private FirebaseAuth SalesAuth;
    Items items;
    private String id;
    private StorageReference Sales_Storage_Ref;
    private DatabaseReference Fav_Ref;
    boolean IsFavorite = false;

    public AllItemsAdapter(ArrayList<Items> itemlist, Context context) {
        this.itemsList = itemlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        Fav_Ref = FirebaseDatabase.getInstance().getReference("Favorites");
        SalesAuth = FirebaseAuth.getInstance();
        id = Objects.requireNonNull(SalesAuth.getCurrentUser()).getUid();
        Sales_Storage_Ref = FirebaseStorage.getInstance().getReference("Sales");


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Animation myanim = AnimationUtils.loadAnimation(context, R.anim.stb2);
        holder.Card_Item.startAnimation(myanim);

        Fav_Ref.child(id).orderByChild("Item_Description")
                .equalTo(itemsList.get(position).getDescription())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            holder.fav_Img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_favorite));
                            IsFavorite = true;
                        }else  {
                            holder.fav_Img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_favorite_border));
                            IsFavorite = false;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        holder.Item_Title.setText(itemsList.get(position).getTitle());
        holder.Item_Price.setText(itemsList.get(position).getPrice());
        Glide.with(context.getApplicationContext()).load(itemsList.get(position).getImg_uri()).into(holder.Item_Img);

        holder.Card_Item.setOnClickListener(v -> {

            ItemInfoFrag fragment = new ItemInfoFrag();
            Bundle args = new Bundle();
            args.putString("ItemTitle", holder.Item_Title.getText().toString());
            args.putString("ItemDescription",itemsList.get(position).getDescription());
            args.putString("ItemPrice",holder.Item_Price.getText().toString());
            args.putString("ItemImg", itemsList.get(position).getImg_uri());
            args.putString("ItemCategory",itemsList.get(position).getCategory());
            args.putString("SellerID",itemsList.get(position).getId());
            args.putString("UniqueID","from_ItemsAdapter");
            fragment.setArguments(args);

            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = manager.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            fragmentTransaction1.replace(R.id.Frame_Content, fragment);
            fragmentTransaction1.commit();

        });

        holder.fav_Img.setOnClickListener(v -> {
           if (IsFavorite){
               Fav_Ref.child(id).child(itemsList.get(position).getKey()).removeValue().addOnCompleteListener(task1 -> {
                   if (task1.isSuccessful()){
                       holder.fav_Img.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border));

                       Toast.makeText(context.getApplicationContext(),
                               itemsList.get(position).getTitle() + " has been deleted from Favorites",
                               Toast.LENGTH_SHORT).show();
                       IsFavorite = false;

                   }else Toast.makeText(context.getApplicationContext(),
                           task1.getException().getMessage(),
                           Toast.LENGTH_LONG).show();
               });
           }else {
               Map<String, String> favMap = new HashMap<>();
               favMap.put("Item_Title", holder.Item_Title.getText().toString());
               favMap.put("Item_Description", itemsList.get(position).getDescription());
               favMap.put("Item_Price", holder.Item_Price.getText().toString() + " EGP");
               favMap.put("User_ID", id);

               Fav_Ref.child(id).push().setValue(favMap).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       holder.fav_Img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));

                       Toast.makeText(context.getApplicationContext(),
                               itemsList.get(position).getTitle() + " has been added to Favorites Successfully",
                               Toast.LENGTH_SHORT).show();
                       IsFavorite = true;

                   }else Toast.makeText(context.getApplicationContext(),
                           task.getException().getMessage(),
                           Toast.LENGTH_LONG).show();

               }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

           }
        });

        /*holder.fav_Img.setOnClickListener(v -> {

            if (holder.fav_Img.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_favorite_border).getConstantState()) {
                Map<String, String> favMap = new HashMap<>();
                favMap.put("Item_Title", holder.Item_Title.getText().toString());
                favMap.put("Item_Description", itemsList.get(position).getDescription());
                favMap.put("Item_Price", holder.Item_Price.getText().toString() + " EGP");
                favMap.put("User_ID", id);

                Fav_Ref.child(id).push().setValue(favMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        holder.fav_Img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));

                        Toast.makeText(context.getApplicationContext(),
                                itemsList.get(position).getTitle() + " has been added to Favorites Successfully",
                                Toast.LENGTH_SHORT).show();

                    }else Toast.makeText(context.getApplicationContext(),
                            task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
            }else {

                holder.fav_Img.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border));
                Fav_Ref.child(id).child(itemsList.get(position).getKey()).removeValue().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){

                        Toast.makeText(context.getApplicationContext(),
                                itemsList.get(position).getTitle() + " has been deleted from Favorites",
                                Toast.LENGTH_SHORT).show();

                    }else Toast.makeText(context.getApplicationContext(),
                            task1.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }



        });*/
    }
/*if (holder.fav_Img.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_favorite_border).getConstantState()){

        holder.fav_Img.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_favorite));
        Toast.makeText(context.getApplicationContext(), itemsList.get(position).getTitle() + " has been added to Favorites Successfully", Toast.LENGTH_SHORT).show();

    }else {
        holder.fav_Img.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border));
        Fav_Ref.child(id).child(itemsList.get(position).getKey()).removeValue().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){

                Toast.makeText(context.getApplicationContext(),
                        itemsList.get(position).getTitle() + " has been deleted from Favorites",
                        Toast.LENGTH_SHORT).show();

            }else Toast.makeText(context.getApplicationContext(),
                    task1.getException().getMessage(),
                    Toast.LENGTH_LONG).show();
        });
    }*/
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Item_Title, Item_Price;
        ImageView Item_Img,fav_Img;
        CardView Card_Item;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Item_Title = itemView.findViewById(R.id.cust_item_title_row);
            Item_Price = itemView.findViewById(R.id.cust_item_price_row);
            Item_Img = itemView.findViewById(R.id.cust_item_img_row);
            fav_Img = itemView.findViewById(R.id.favorite_Img);
            Card_Item = itemView.findViewById(R.id.cust_card_item_row);
        }
    }
}
