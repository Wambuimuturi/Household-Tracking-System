package com.kiduyu.patriciproject.householdtrackingsystem.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kiduyu.patriciproject.householdtrackingsystem.Constants.Constants;
import com.kiduyu.patriciproject.householdtrackingsystem.Delivery.DeliveryHomeActivity;
import com.kiduyu.patriciproject.householdtrackingsystem.Fragments.ManageConsumableFragment;
import com.kiduyu.patriciproject.householdtrackingsystem.Models.History;
import com.kiduyu.patriciproject.householdtrackingsystem.R;
import com.kiduyu.patriciproject.householdtrackingsystem.RequestHandler.RequestHandler;
import com.kiduyu.patriciproject.householdtrackingsystem.SharedPref.Prevalent;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryApproveAdapter extends RecyclerView.Adapter<DeliveryApproveAdapter.MyViewHolder> {
    private List<History> historyList;
    Context mcontext;
    private Range range1, range2, range3;

    @NonNull
    @Override
    public DeliveryApproveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_dg, parent, false);

        return new MyViewHolder(itemView);
    }

    public DeliveryApproveAdapter(Context context, List<History> consumableList) {
        this.historyList = consumableList;
        this.mcontext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryApproveAdapter.MyViewHolder holder, int position) {
        final History consumable = historyList.get(position);


        holder.name.setText("Name: "+consumable.getItemname());
        holder.order.setText("Order: "+consumable.getItemorder()+" units");
        holder.agent.setText("To User: "+consumable.getItemagent());
        holder.urgency.setText("Urgency: "+consumable.getItemurgency());

        holder.approvetv.setOnClickListener(v -> {
            //Deny(consumable.getId());
            final DatabaseReference RootRef;
            Map<String, String> params = new HashMap<>();
            params.put("itemname", consumable.getItemname());
            params.put("itemunits",consumable.getItemorder());
            params.put("username", consumable.getItemagent());
            params.put("urgency",consumable.getItemurgency());

            RootRef = FirebaseDatabase.getInstance().getReference().child("Approved Deliveries").child(Prevalent.currentOnlineUser.getPhone()).push();

            RootRef.setValue(params);

            //Delete from approve details
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Delivery Orders").child(Prevalent.currentOnlineUser.getName()).child(consumable.getId()).removeValue();

            FancyToast.makeText(mcontext, "Order Approved", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

            mcontext.startActivity(new Intent(mcontext,DeliveryHomeActivity.class));
        });

        holder.denytv.setOnClickListener(v -> {
            //Approve(consumable.getId());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Delivery Orders").child(Prevalent.currentOnlineUser.getName()).child(consumable.getId()).removeValue();
            FancyToast.makeText(mcontext, "Order Denied", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

            mcontext.startActivity(new Intent(mcontext,DeliveryHomeActivity.class));

        });

    }

    private void Approve(String id) {
            ProgressDialog progressDialog = new ProgressDialog(mcontext);
            progressDialog.setTitle("Loading ......");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.APPROVE_SCHEDULE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            mcontext.startActivity(new Intent(mcontext, DeliveryHomeActivity.class));



                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.d("TAG", "onResponsejsoneror: " + error.getMessage());
                            FancyToast.makeText(mcontext, error.getMessage(), FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    return params;
                }
            };
            RequestHandler.getInstance(mcontext).addToRequestQueue(stringRequest);

        }

    private void Deny(String id) {
        ProgressDialog progressDialog = new ProgressDialog(mcontext);
        progressDialog.setTitle("Loading ......");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DENY_SCHEDULE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        mcontext.startActivity(new Intent(mcontext, DeliveryHomeActivity.class));



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("TAG", "onResponsejsoneror: " + error.getMessage());
                        FancyToast.makeText(mcontext, error.getMessage(), FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestHandler.getInstance(mcontext).addToRequestQueue(stringRequest);
    }


    public void filterList(List<History> filteredList) {
        historyList = filteredList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,order,agent,urgency;
        Button denytv,approvetv;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.history_name);
            order = view.findViewById(R.id.history_order);
            agent = view.findViewById(R.id.history_agent);
            urgency = view.findViewById(R.id.history_urgency);
            denytv = view.findViewById(R.id.deliverydeny);
            approvetv = view.findViewById(R.id.deliveryapprove);
        }
    }

    @Override
    public int getItemCount() {

        return historyList.size();
    }
}
