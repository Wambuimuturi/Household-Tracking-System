package com.kiduyu.patriciproject.householdtrackingsystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.patriciproject.householdtrackingsystem.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReportsActivity extends AppCompatActivity {
    private BarChart barChartSuppliersAnalysis, barChartCustomersAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        barChartSuppliersAnalysis = findViewById(R.id.barChartSuppliersAnalysis);
        barChartCustomersAnalysis = findViewById(R.id.barChartCustomersAnalysis);

        loadCharts();

    }

    private void loadCharts(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Delivery Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Map<String ,String >> suppliers = new ArrayList<>();

                for (DataSnapshot supplier : dataSnapshot.getChildren()){
                    Map<String ,String > map = new HashMap<>();
                    map.put("name", supplier.getKey());
                    map.put("deliveries", String.valueOf(supplier.getChildrenCount()));
                    suppliers.add(map);
                }

                BarChart barChart = (BarChart)findViewById(R.id.barChartSuppliersAnalysis);
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();

                //entries.add(new BarEntry(38f, 0));

                int i = 0;
                for (Map<String ,String > customer : suppliers){
                    entries.add(new BarEntry(Integer.parseInt(customer.get("deliveries")), i));
                    labels.add(customer.get("name"));
                    i++;
                }

                BarDataSet bardataset = new BarDataSet(entries, " ");

                BarData data = new BarData(labels, bardataset);
                barChart.setData(data);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.animateY(5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Approved Deliveries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Map<String ,String >> customers = new ArrayList<>();

                for (DataSnapshot customer : dataSnapshot.getChildren()){
                    String customerName = "";
                    for (DataSnapshot order : customer.getChildren()){
                        customerName = order.child("username").getValue().toString();
                        break;
                    }

                    Map<String ,String > map = new HashMap<>();
                    map.put("name", customerName);
                    map.put("purchases", String.valueOf(customer.getChildrenCount()));
                    customers.add(map);
                }

                BarChart barChart = (BarChart)findViewById(R.id.barChartCustomersAnalysis);
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();

                //entries.add(new BarEntry(38f, 0));

                int i = 0;
                for (Map<String ,String > customer : customers){
                    entries.add(new BarEntry(Integer.parseInt(customer.get("purchases")), i));
                    labels.add(customer.get("name"));
                    i++;
                }

                BarDataSet bardataset = new BarDataSet(entries, " ");

                BarData data = new BarData(labels, bardataset);
                barChart.setData(data);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.animateY(5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}