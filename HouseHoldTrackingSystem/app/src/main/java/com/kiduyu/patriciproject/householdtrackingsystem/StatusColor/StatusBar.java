package com.kiduyu.patriciproject.householdtrackingsystem.StatusColor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kiduyu.patriciproject.householdtrackingsystem.Constants.Constants;
import com.kiduyu.patriciproject.householdtrackingsystem.Home.HomeActivity;
import com.kiduyu.patriciproject.householdtrackingsystem.R;
import com.kiduyu.patriciproject.householdtrackingsystem.RequestHandler.RequestHandler;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class StatusBar {

    public StatusBar() {

    }

    /**
     * Making notification bar transparent
     */
    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


    public static void showProgressDialog(Context ctx, Boolean state) {

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ctx);
        if (state == true) {
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        } else if (state == false) {
            progressDialog.dismiss();
        }


    }

    public static void FetchData(Context ctx) {

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
                                    FancyToast.makeText(ctx,name+" Is almost Depleted Make a new Schedule",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
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
        RequestHandler.getInstance(ctx).addToRequestQueue(request);
        // requestQueue.add(jsonArrayRequest);
    }
}
