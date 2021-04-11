package com.kiduyu.patriciproject.householdtrackingsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.kiduyu.patriciproject.householdtrackingsystem.R;


public class MapsActivity extends AppCompatActivity {
    private WebView webView;
    private String userLocation;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        userLocation = getIntent().getStringExtra("location");
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://maps.google.com/maps?q=Nyeri Town");
    }
}