package in.co.kissanvendor.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.kissanvendor.R;
import in.co.kissanvendor.adapters.PaymentDetailsAdapter;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;
import in.co.kissanvendor.models.PaymentDetails;


public class PaymentFragment extends Fragment {

    RecyclerView payment_recycler;
    LinearLayoutManager linearLayoutManager;
    PaymentDetailsAdapter paymentDetailsAdapter;
    ArrayList<PaymentDetails> paymentDetails = new ArrayList<>();
    SessionManager session;
    ViewDialog progressbar;
    TextView frame_23,priceClc,some_id;
    EditText totalpayment;
    LinearLayout paymentinfo_lin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        payment_recycler = v.findViewById(R.id.payment_recycler);
        frame_23 = v.findViewById(R.id.frame_23);
        priceClc = v.findViewById(R.id.priceClc);
        totalpayment = v.findViewById(R.id.totalpayment);
        paymentinfo_lin = v.findViewById(R.id.paymentinfo_lin);
        some_id = v.findViewById(R.id.some_id);

        frame_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paymentinfo_lin.setVisibility(View.VISIBLE);

            }
        });

        priceClc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalpayment.getText().toString().trim().equals("")){

                    Toast.makeText(getActivity(), "Please give The widthdraw amount", Toast.LENGTH_SHORT).show();
                }else{

                    String amount = totalpayment.getText().toString().trim();
                    int in_amount = Integer.parseInt(amount);

                    payoutRequest(in_amount);
                }
            }
        });

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        transaction();
        
        return v;
    }

    public void payoutRequest(int amount){

        progressbar.showDialog();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount",amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerLinks.payoutrequest, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    String code = response.getString("code");
                    String msg = response.getString("msg");
                    if(code.equals("200")){

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressbar.hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {


                    Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {

                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = session.getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }

        };

        jsonObjectRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);
    }

     public void transaction(){

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.transactions, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");

                    if(code.equals("200")){

                        String data = jsonObject.getString("data");
                        JSONObject jsonObject_data = new JSONObject(data);

                        String vendor = jsonObject_data.getString("vendor");
                        String payouts = jsonObject_data.getString("payouts");
                        String deposits = jsonObject_data.getString("deposits");

                        JSONArray jsonArray_payouts = new JSONArray(payouts);
                        JSONArray jsonArray_deposits = new JSONArray(deposits);
                        JSONObject jsonObject_vendor = new JSONObject(vendor);

                        String wallet = jsonObject_vendor.getString("wallet");

                        JSONObject jsonObject_wallet = new JSONObject(wallet);

                        String balance = jsonObject_wallet.getString("balance");
                        String lockedBalance = jsonObject_wallet.getString("lockedBalance");
                        //String name = jsonObject_vendor.getString("name");

                        some_id.setText(balance);

                        if(jsonArray_payouts.length() != 0){

                            for(int i=0;i<jsonArray_payouts.length();i++){

                                JSONObject jsonObject_payouts = jsonArray_payouts.getJSONObject(i);

                                String status = jsonObject_payouts.getString("status");
                                String razorpayPayoutId = jsonObject_payouts.getString("razorpayPayoutId");
                                String razorpayUtr = jsonObject_payouts.getString("razorpayUtr");
                                String _id = jsonObject_payouts.getString("_id");
                                String vendorId = jsonObject_payouts.getString("vendorId");
                                String amount = jsonObject_payouts.getString("amount");
                                String createdAt = jsonObject_payouts.getString("createdAt");
                                String updatedAt = jsonObject_payouts.getString("updatedAt");

                                PaymentDetails paymentDetails1 = new PaymentDetails(
                                        _id,"",status,amount,"",createdAt
                                );

                                paymentDetails.add(paymentDetails1);
                            }

                            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            paymentDetailsAdapter = new PaymentDetailsAdapter(paymentDetails,getActivity());
                            payment_recycler.setLayoutManager(linearLayoutManager);
                            payment_recycler.setHasFixedSize(true);
                            payment_recycler.setAdapter(paymentDetailsAdapter);
                            
                        }else{

                            Toast.makeText(getActivity(), "Transaction Not Avilable", Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(getActivity(), "Data Is Not Avilable", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressbar.hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {

                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }
                    }
                }

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = session.getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }

        };

         stringRequest.setRetryPolicy(new
                 DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
         requestQueue.getCache().clear();
         requestQueue.add(stringRequest);
     }

}