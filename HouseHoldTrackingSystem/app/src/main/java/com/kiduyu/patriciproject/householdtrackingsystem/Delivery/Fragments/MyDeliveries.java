package com.kiduyu.patriciproject.householdtrackingsystem.Delivery.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.patriciproject.householdtrackingsystem.Adapter.MyDeliveriesAdapter;
import com.kiduyu.patriciproject.householdtrackingsystem.R;
import com.kiduyu.patriciproject.householdtrackingsystem.SharedPref.Prevalent;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by Kiduyu klaus
 * on 10/11/2020 04:21 2020
 */
public class MyDeliveries extends Fragment {
    private RecyclerView recyclerView;
    private MyDeliveriesAdapter myDeliveriesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_deliveries_fragment, container, false);


        ArrayList<String> deliveries = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewMyDeliveries);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myDeliveriesAdapter = new MyDeliveriesAdapter(view.getContext(), deliveries);
        recyclerView.setAdapter(myDeliveriesAdapter);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Approved Deliveries").child(Prevalent.currentOnlineUser.getPhone());

        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot delivery : dataSnapshot.getChildren()){
                    deliveries.add("You are supposed to deliver "+delivery.child("itemunits").getValue()+ " units of "+
                            delivery.child("itemname").getValue() + " to user "+delivery.child("username").getValue() +".\nUrgency : "+delivery.child("urgency").getValue());
                    myDeliveriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}