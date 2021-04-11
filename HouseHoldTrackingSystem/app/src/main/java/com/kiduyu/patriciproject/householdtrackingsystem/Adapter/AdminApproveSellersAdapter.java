package com.kiduyu.patriciproject.householdtrackingsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.patriciproject.householdtrackingsystem.Admin.AdminHomeActivity;
import com.kiduyu.patriciproject.householdtrackingsystem.Models.Person;
import com.kiduyu.patriciproject.householdtrackingsystem.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminApproveSellersAdapter extends RecyclerView.Adapter<AdminApproveSellersAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String message;
    private Context mcontext;

    // data is passed into the constructor
    public AdminApproveSellersAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.approve_sellers_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        message = mData.get(position);
        String sellerKey = message.split("\n")[0];
        holder.myTextView.setText(message.split("\n")[1]);

        holder.approveButton.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Approve Delivery").child(sellerKey);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name,phone,password;

                    System.out.println("****************"+dataSnapshot);
                    name = dataSnapshot.child("name").getValue().toString();
                    phone = dataSnapshot.child("phone").getValue().toString();
                    password = dataSnapshot.child("password").getValue().toString();

                    Person person = new Person(name,phone,password,"https://cdn.pixabay.com/photo/2014/03/24/17/19/teacher-295387__340.png","");


                    database.getReference().child("Delivery").child(phone).setValue(person);
                    myRef.removeValue();
                    FancyToast.makeText(mcontext,"Seller Approved Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false);
                    mcontext.startActivity(new Intent(mcontext, AdminHomeActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        holder.denyButton.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Approve Delivery").child(sellerKey);

            myRef.removeValue();
            FancyToast.makeText(mcontext,"Seller Denied Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false);
            mcontext.startActivity(new Intent(mcontext, AdminHomeActivity.class));
        });



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        Button approveButton, denyButton;


        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.TextVewMessage);
            approveButton = itemView.findViewById(R.id.sellerApprove);
            denyButton = itemView.findViewById(R.id.sellerDeny);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
