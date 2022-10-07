package in.co.kissanvendor.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        payment_recycler = v.findViewById(R.id.payment_recycler);

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        paymentDetails.add(new PaymentDetails("123456789654","Apple","Delivered","Rs 250","Refundable","12 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Returned","Rs 630","Refundable","13 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Delivered","Rs 500","Refundable","14 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Returned","Rs 810","Refundable","15 sep 22"));

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        paymentDetailsAdapter = new PaymentDetailsAdapter(paymentDetails,getActivity());
        payment_recycler.setLayoutManager(linearLayoutManager);
        payment_recycler.setHasFixedSize(true);
        payment_recycler.setAdapter(paymentDetailsAdapter);

        return v;
    }

    public void payoutRequest(long amount){

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