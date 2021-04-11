package com.kiduyu.patriciproject.householdtrackingsystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.patriciproject.householdtrackingsystem.Account.LoginActivity;
import com.kiduyu.patriciproject.householdtrackingsystem.Adapter.AdminApproveSellersAdapter;
import com.kiduyu.patriciproject.householdtrackingsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    private AdminApproveSellersAdapter adminApproveSellersAdapter;
    private Button buttonViewReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        buttonViewReports = findViewById(R.id.buttonViewReports);
        buttonViewReports.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
        });


        ArrayList<String> sellers = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewApproveSellers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        adminApproveSellersAdapter = new AdminApproveSellersAdapter(this, sellers);
        recyclerView.setAdapter(adminApproveSellersAdapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Approve Delivery");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    TextView textView = findViewById(R.id.TextViewPlaceholder);
                    textView.setVisibility(View.GONE);
                }
                for (DataSnapshot seller : dataSnapshot.getChildren()){
                    sellers.add(seller.getKey()+"\nSeller by the name \""+seller.child("name").getValue()+
                            "\" and mobile \""+seller.child("phone").getValue()+"\" wants to join  the system.");
                    adminApproveSellersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}