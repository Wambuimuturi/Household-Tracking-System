package com.kiduyu.patriciproject.householdtrackingsystem.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kiduyu.patriciproject.householdtrackingsystem.Account.LoginActivity;
import com.kiduyu.patriciproject.householdtrackingsystem.Adapter.ConsumableAdapter;
import com.kiduyu.patriciproject.householdtrackingsystem.Constants.Constants;
import com.kiduyu.patriciproject.householdtrackingsystem.Fragments.ConsumablesFragment;
import com.kiduyu.patriciproject.householdtrackingsystem.Fragments.HistoryFragment;
import com.kiduyu.patriciproject.householdtrackingsystem.Fragments.HomeFragment;
import com.kiduyu.patriciproject.householdtrackingsystem.Fragments.ProfileFragment;
import com.kiduyu.patriciproject.householdtrackingsystem.Fragments.ReportsFragment;
import com.kiduyu.patriciproject.householdtrackingsystem.Models.Consumable;
import com.kiduyu.patriciproject.householdtrackingsystem.R;
import com.kiduyu.patriciproject.householdtrackingsystem.RequestHandler.RequestHandler;
import com.kiduyu.patriciproject.householdtrackingsystem.SharedPref.Prevalent;
import com.kiduyu.patriciproject.householdtrackingsystem.SharedPref.SharedPrefManager;
import com.kiduyu.patriciproject.householdtrackingsystem.StatusColor.StatusBar;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView textViewUsername, textViewUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.changeStatusBarColor(this);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        textViewUsername = (TextView) findViewById(R.id.user_name_hs);
        textViewUserEmail = (TextView) findViewById(R.id.user_id_hs);

        textViewUserEmail.setText(Prevalent.currentOnlineUser.getPhone());
        textViewUsername.setText(Prevalent.currentOnlineUser.getName());
        CircleImageView imageView = (CircleImageView) findViewById(R.id.user_photo);
        Glide.with(this).load(Prevalent.currentOnlineUser.getImage()).into(imageView);

        if (savedInstanceState== null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.navigationHome);}
        StatusBar.FetchData(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationHome:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                    return true;
                case R.id.navigationconsumable:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                            new ConsumablesFragment()).commit();
                    //MenuItem.se
                    return true;
                case R.id.navigationreports:

                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                            new ReportsFragment()).commit();
                    return true;
                case  R.id.navigationdelivery:

                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                    return true;
                case  R.id.navigationMenu:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                    return true;
            }



            return false;
        }

    };

    public void logout(View view) {
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void FetchData() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Constants.CONSUMERBLE_API,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Consumable");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject author = jsonArray.getJSONObject(i);
                                String name = author.getString("itemname");
                                String remaining = author.getString("itemremaining");
                                String cost = author.getString("itemcost");
                                String itemmeasure = author.getString("itemmeasure");
                                String time = author.getString("itemtime");

                                //mAuthorList.add(new AuthorHome(name, image));
                                //Log.d(TAG, "onResponseApi: "+name+"\n");

                                int remainings = Integer.parseInt(remaining);
                                if (remainings <= 3){
                                    //Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT).show();
                                    FancyToast.makeText(HomeActivity.this,name+" Is almost Depleted Make a new Schedule",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(request);
        // requestQueue.add(jsonArrayRequest);
    }
}
