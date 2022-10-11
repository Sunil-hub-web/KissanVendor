package in.co.kissanvendor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import in.co.kissanvendor.R;
import in.co.kissanvendor.activities.AddNewProduct;
import in.co.kissanvendor.activities.DashBoard;
import in.co.kissanvendor.activities.LoginActivity;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;

public class MyAccountFragment extends Fragment {

    SessionManager session;
    ViewDialog progressbar;
    TextView username_heading;
    ImageView edit, save_btn;
    EditText name_ed, mobilenumber_ed, email_ed, bankname_ed, accountnumber_ed, ifsccode_ed, state_ed,
            city_ed, street_ed, zipcode_ed,shopName_ed,pannumber_ed,gstnumber_ed,address_ed,locality_ed;
    String nm, mob, eml, bnknm, ifsc, stat, cty, strt , shop,pan,gst,address,loca;
    long acnnm, zip;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myaccount, container, false);

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        Init(v);
        GetProfileData();
        return v;
    }

    public void Init(View v){

        username_heading = v.findViewById(R.id.username_heading);

        name_ed = v.findViewById(R.id.name_ed);
        shopName_ed = v.findViewById(R.id.shopName_ed);
        pannumber_ed = v.findViewById(R.id.pannumber_ed);
        gstnumber_ed = v.findViewById(R.id.gstnumber_ed);
        address_ed = v.findViewById(R.id.address_ed);
        locality_ed = v.findViewById(R.id.locality_ed);
        mobilenumber_ed = v.findViewById(R.id.mobilenumber_ed);
        email_ed = v.findViewById(R.id.email_ed);
        bankname_ed = v.findViewById(R.id.bankname_ed);
        accountnumber_ed = v.findViewById(R.id.accountnumber_ed);
        ifsccode_ed = v.findViewById(R.id.ifsccode_ed);
        state_ed = v.findViewById(R.id.state_ed);
        city_ed = v.findViewById(R.id.city_ed);
        street_ed = v.findViewById(R.id.street_ed);
        zipcode_ed = v.findViewById(R.id.zipcode_ed);

        save_btn = v.findViewById(R.id.save_btn);
        edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_ed.setEnabled(true);
                mobilenumber_ed.setEnabled(true);
                email_ed.setEnabled(true);
                bankname_ed.setEnabled(true);
                accountnumber_ed.setEnabled(true);
                ifsccode_ed.setEnabled(true);
                state_ed.setEnabled(true);
                city_ed.setEnabled(true);
                street_ed.setEnabled(true);
                zipcode_ed.setEnabled(true);
                shopName_ed.setEnabled(true);
                address_ed.setEnabled(true);
                pannumber_ed.setEnabled(true);
                gstnumber_ed.setEnabled(true);
                locality_ed.setEnabled(true);

                name_ed.requestFocus();

                edit.setVisibility(View.GONE);
                save_btn.setVisibility(View.VISIBLE);

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name_ed.getText().toString().trim().equals("")){
                    name_ed.setError("enter name");
                    name_ed.requestFocus();

                }else if(name_ed.getText().toString().trim().length()<=4){

                    name_ed.setError("enter name");
                    name_ed.requestFocus();

                }else if(mobilenumber_ed.getText().toString().trim().length()==0){
                    mobilenumber_ed.setError("enter mobile number");
                    mobilenumber_ed.requestFocus();

                }else if(mobilenumber_ed.getText().toString().trim().length()!=10){
                    mobilenumber_ed.setError("must be 10 digits");
                    mobilenumber_ed.requestFocus();

                }else if(email_ed.getText().toString().trim().length()==0){
                    email_ed.setError("enter email");
                    email_ed.requestFocus();

                }else if(!isEmailValid(email_ed.getText().toString())){
                    email_ed.setError("enter valid email");
                    email_ed.requestFocus();

                }else if(bankname_ed.getText().toString().trim().equals("")){
                    bankname_ed.setError("enter bank name");
                    bankname_ed.requestFocus();

                }else if(bankname_ed.getText().toString().trim().length()<5){

                    bankname_ed.setError("enter name");
                    bankname_ed.requestFocus();

                }else if(!isValidUserName(bankname_ed.getText().toString().trim())){

                    bankname_ed.setError("enter name");
                    bankname_ed.requestFocus();

                }else if(accountnumber_ed.getText().toString().trim().equals("")){
                    accountnumber_ed.setError("enter account number");
                    accountnumber_ed.requestFocus();

                }else if(accountnumber_ed.getText().toString().trim().length()<=10){
                    accountnumber_ed.setError("enter account number");
                    accountnumber_ed.requestFocus();

                }else if(ifsccode_ed.getText().toString().trim().equals("")){
                    ifsccode_ed.setError("enter IFSC code");
                    ifsccode_ed.requestFocus();

                }else if(ifsccode_ed.getText().toString().trim().length()<=10){
                    ifsccode_ed.setError("enter IFSC code");
                    ifsccode_ed.requestFocus();

                }else if(!isValidIfcCode(ifsccode_ed.getText().toString().trim())){

                    ifsccode_ed.setError("enter IFSC code");
                    ifsccode_ed.requestFocus();

                }else if(state_ed.getText().toString().trim().length()==0){
                    state_ed.setError("enter state");
                    state_ed.requestFocus();

                }else if(!isValidUserName(state_ed.getText().toString().trim())){

                    state_ed.setError("enter name");
                    state_ed.requestFocus();

                }else if(city_ed.getText().toString().trim().length()==0){
                    city_ed.setError("enter city");
                    city_ed.requestFocus();

                }else if(!isValidUserName(city_ed.getText().toString().trim())){

                    city_ed.setError("enter name");
                    city_ed.requestFocus();

                }else if(street_ed.getText().toString().trim().length()==0){
                    street_ed.setError("enter street");
                    street_ed.requestFocus();

                }else if(!isValidUserName(city_ed.getText().toString().trim())){

                    city_ed.setError("enter name");
                    city_ed.requestFocus();

                }else if(zipcode_ed.getText().toString().trim().length()==0){
                    zipcode_ed.setError("enter zipcode");
                    zipcode_ed.requestFocus();

                }else if(zipcode_ed.getText().toString().trim().length()!=6){
                    zipcode_ed.setError("zipcode must be 6 digit");
                    zipcode_ed.requestFocus();

                }else{

                    nm = name_ed.getText().toString();
                    mob = mobilenumber_ed.getText().toString();
                    eml = email_ed.getText().toString();
                    bnknm = bankname_ed.getText().toString();
                    acnnm = Long.parseLong(accountnumber_ed.getText().toString());
                    ifsc = ifsccode_ed.getText().toString();
                    stat = state_ed.getText().toString();
                    cty = city_ed.getText().toString();
                    strt = street_ed.getText().toString();
                    shop = shopName_ed.getText().toString();
                    pan = pannumber_ed.getText().toString();
                    gst = gstnumber_ed.getText().toString();
                    address = address_ed.getText().toString();
                    loca = locality_ed.getText().toString();
                    zip = Long.parseLong(zipcode_ed.getText().toString());

                    updateProfile();
                }

            }
        });

    }

    private void GetProfileData() {
        progressbar.showDialog();

        Log.d("fvsDevbf", ServerLinks.getProfileData_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getProfileData_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SignUp_Activity.this, response, Toast.LENGTH_LONG).show();
                        progressbar.hideDialog();
                        Log.d("fvsDevbf", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {


                                JSONObject vendorDetails = jsonObject.getJSONObject("vendorDetails");
                                String mobile = vendorDetails.getString("mobile");
                                //String mobileVerified = vendorDetails.getString("mobileVerified");
                                String emailID = vendorDetails.getString("emailID");
                                //String emailVerified = vendorDetails.getString("emailVerified");
                                String name = vendorDetails.getString("name");
                                String _id = vendorDetails.getString("_id");
                                String shopname = vendorDetails.getString("shopName");
                                String gstNumber = vendorDetails.getString("gstNumber");
                                String panNumber = vendorDetails.getString("panNumber");

                                JSONObject location = vendorDetails.getJSONObject("location");
                                String address = location.getString("address");
                                String street = location.getString("street");
                                String locality = location.getString("locality");
                                String city = location.getString("city");
                                String state = location.getString("state");
                                String zip = location.getString("zip");

                                JSONObject accountDetails = vendorDetails.getJSONObject("accountDetails");
                                String accountNumber = accountDetails.getString("accountNumber");
                                String bankName = accountDetails.getString("bankName");
                                String ifscCode = accountDetails.getString("ifscCode");

                                session.setUserID(_id);
                                session.setUserName(name);
                                session.setPhone(mobile);
                                session.setEmail(emailID);
                               // session.setEmailVerifyStatus(emailVerified);
                                session.setAddress1(address);
                                session.setState(state);
                                session.setCity(city);
                                session.setPinCode(zip);
                                session.setStreet(street);
                                session.setlocality(locality);

                                username_heading.setText(name);
                                name_ed.setText(name);
                                mobilenumber_ed.setText(mobile);
                                email_ed.setText(emailID);
                                bankname_ed.setText(bankName);
                                accountnumber_ed.setText(accountNumber);
                                ifsccode_ed.setText(ifscCode);
                                state_ed.setText(state);
                                city_ed.setText(city);
                                street_ed.setText(street);
                                address_ed.setText(address);
                                locality_ed.setText(locality);
                                shopName_ed.setText(shopname);
                                gstnumber_ed.setText(gstNumber);
                                pannumber_ed.setText(panNumber);

                                if(zip.equalsIgnoreCase("null")){

                                }else {
                                    zipcode_ed.setText(zip);
                                }


                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successExcep", "" + e);
                            progressbar.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                }) {

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

    private void updateProfile() {

        progressbar.showDialog();
        JSONObject paramjson = new JSONObject();

        try {

            paramjson.put("name", nm);
            paramjson.put("mobile", mob);
            paramjson.put("emailID", eml);
            paramjson.put("shopname", shop);
            paramjson.put("IsWholeSaler", true);
            paramjson.put("panImage", "");
            paramjson.put("panNumber", pan);
            paramjson.put("address", address);
            paramjson.put("street", strt);
            paramjson.put("locality", loca);
            paramjson.put("city", cty);
            paramjson.put("state", stat);
            paramjson.put("zip", zip);
            paramjson.put("billingDetails_email", eml);
            paramjson.put("gstImage", "");
            paramjson.put("gstNumber", gst);
            paramjson.put("bankName", bnknm);
            paramjson.put("accountNumber", acnnm);
            paramjson.put("ifscCode", ifsc);


            Log.d("successresponceVolley", "" + paramjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ServerLinks.updateProfile_url, paramjson,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());
                try {
                    Log.d("successresponceVolley", "" + response);
                    String code = response.getString("code");

                    if (code.equalsIgnoreCase("200")) {

                        String message = response.getString("msg");

                        progressbar.hideDialog();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        name_ed.setEnabled(false);
                        mobilenumber_ed.setEnabled(false);
                        email_ed.setEnabled(false);
                        bankname_ed.setEnabled(false);
                        accountnumber_ed.setEnabled(false);
                        ifsccode_ed.setEnabled(false);
                        state_ed.setEnabled(false);
                        city_ed.setEnabled(false);
                        street_ed.setEnabled(false);
                        zipcode_ed.setEnabled(false);

                        edit.setVisibility(View.VISIBLE);
                        save_btn.setVisibility(View.GONE);

                    } else {
                        String message = response.getString("msg");

                        progressbar.hideDialog();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressbar.hideDialog();
                    Log.d("successresponceEr", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
                progressbar.hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    Log.d("successresponceVolley", "" + error.networkResponse);
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                String auth = session.getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", ""+auth);
                return headers;
            }
        };
        request.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidUserName(final String userName) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN =  "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";

        pattern =  Pattern.compile (PASSWORD_PATTERN);
        matcher = pattern.matcher (userName);

        return matcher.matches ( );

    }

    public boolean isValidIfcCode(final String ifsccode) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN =  "^[A-Za-z]{4,}0[0-9]{6,}$";

        pattern =  Pattern.compile (PASSWORD_PATTERN);
        matcher = pattern.matcher (ifsccode);

        return matcher.matches ( );

    }

}