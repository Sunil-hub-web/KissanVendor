package in.co.kissanvendor.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.chaos.view.PinView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import in.co.kissanvendor.R;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;

public class LoginActivity extends AppCompatActivity {

    Dialog forgotpwdDialog, otpdialog, newpwddialog;
    TextView forgot_pass;
    Button login_btn;
    ViewDialog progressbar;
    SessionManager session;
    String mn, pw, forgotpwdnum;
    EditText mobilenumber, password;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        InIt();

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPwdDialog();


            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,LandingActivity.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobilenumber.getText().toString().trim().length() == 0) {
                    mobilenumber.setError("enter mobile number");
                    mobilenumber.requestFocus();

                } else if (mobilenumber.getText().toString().trim().length() != 10) {
                    mobilenumber.setError("must be 10 digits");
                    mobilenumber.requestFocus();

                } else if (password.getText().toString().trim().length() == 0) {
                    mobilenumber.setError("enter mobilenumber");
                    mobilenumber.requestFocus();

                } else {

                    mn = mobilenumber.getText().toString().trim();
                    pw = password.getText().toString();

                    SignInUser();
                }
            }
        });
    }

    public void InIt() {

        login_btn = findViewById(R.id.login_btn);
        forgot_pass = findViewById(R.id.forgot_pass);
        mobilenumber = findViewById(R.id.mobilenumber);
        password = findViewById(R.id.password);
        back_btn = findViewById(R.id.back_btn);
    }

    private void forgotPwdDialog() {

        forgotpwdDialog = new Dialog(LoginActivity.this);
        forgotpwdDialog.setContentView(R.layout.forgot_pwd_dialog);
        Window window = forgotpwdDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView sure = forgotpwdDialog.findViewById(R.id.submit_btn);
        TextView cancel = forgotpwdDialog.findViewById(R.id.back);
        EditText phonenumberforgotpwd = forgotpwdDialog.findViewById(R.id.phonenumberforgotpwd);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phonenumberforgotpwd.getText().toString().trim().length() == 0) {
                    phonenumberforgotpwd.setError("enter registered mobile number");
                    phonenumberforgotpwd.requestFocus();

                } else if (phonenumberforgotpwd.getText().toString().trim().length() != 10) {
                    phonenumberforgotpwd.setError("enter valid mobile number");
                    phonenumberforgotpwd.requestFocus();

                } else {
                    forgotpwdnum = phonenumberforgotpwd.getText().toString();
                    ForgotPWd(forgotpwdnum);
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotpwdDialog.dismiss();
            }
        });

        forgotpwdDialog.setCancelable(false);
        forgotpwdDialog.show();
    }

    private void OTPDialog(String phn) {

        otpdialog = new Dialog(LoginActivity.this);
        otpdialog.setContentView(R.layout.otp_dialog);
        Window window = otpdialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        PinView pinvw = otpdialog.findViewById(R.id.pinView);
        TextView sure = otpdialog.findViewById(R.id.submit_btn);
        TextView cancel = otpdialog.findViewById(R.id.back);
        TextView resendotp = otpdialog.findViewById(R.id.resendotp);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pinvw.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_SHORT).show();

                }else if(pinvw.getText().toString().trim().length()!=4){
                    Toast.makeText(getApplicationContext(), "OTP must be 4 digits", Toast.LENGTH_SHORT).show();

                }else{

                    NewPwdDialog(pinvw.getText().toString(), phn);
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpdialog.dismiss();
            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpdialog.dismiss();
                ForgotPWd(forgotpwdnum);
            }
        });

        otpdialog.setCancelable(false);
        otpdialog.show();
    }

    private void NewPwdDialog(String ot, String ph) {

        newpwddialog = new Dialog(LoginActivity.this);
        newpwddialog.setContentView(R.layout.change_pwd_dialog);
        Window window = newpwddialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView sure = newpwddialog.findViewById(R.id.submit_btn);
        TextView cancel = newpwddialog.findViewById(R.id.back);
        EditText newpwd = newpwddialog.findViewById(R.id.newpwd);
        EditText confirmpwd = newpwddialog.findViewById(R.id.confirmpwd);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newpwd.getText().toString().trim().length()==0){
                    newpwd.setError("enter password");
                    newpwd.requestFocus();

                }else if(newpwd.getText().toString().trim().length()<8){
                    newpwd.setError("password must be 8 characters");
                    newpwd.requestFocus();

                }else if(!newpwd.getText().toString().equalsIgnoreCase(confirmpwd.getText().toString())){
                    confirmpwd.setError("password didn't match");
                    confirmpwd.requestFocus();

                }else{

                    ChangePWd(ph, confirmpwd.getText().toString(), ot);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newpwddialog.dismiss();
            }
        });

        newpwddialog.setCancelable(false);
        newpwddialog.show();
    }


    private void SignInUser() {
        Log.d("successresponceVolley", "started");
        progressbar.showDialog();
        JSONObject paramjson = new JSONObject();
        try {

            paramjson.put("mobile", mn);
            paramjson.put("password", pw);

            Log.d("successresponceVolley", "" + paramjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(ServerLinks.SignIn_url, paramjson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("successresponceVolley", "" + response);
                            String code = response.getString("code");

                            if (code.equalsIgnoreCase("200")) {

                                progressbar.hideDialog();

                                String mobile = response.getString("mobile");
                                String token = response.getString("token");
                                String msg = response.getString("msg");

                                session.setToken(token);

                                GetProfileData(msg);

                            } else {
                                String message = response.getString("msg");

                                progressbar.hideDialog();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                progressbar.hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }
            }


        });

        request_json.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request_json);
    }

    private void GetProfileData(String msg) {
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

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                JSONObject vendorDetails = jsonObject.getJSONObject("vendorDetails");
                                String mobile = vendorDetails.getString("mobile");
                                //String mobileVerified = vendorDetails.getString("mobileVerified");
                                String emailID = vendorDetails.getString("emailID");
                                //String emailVerified = vendorDetails.getString("emailVerified");
                                String name = vendorDetails.getString("name");
                                String _id = vendorDetails.getString("_id");

                                JSONObject location = vendorDetails.getJSONObject("location");
                                String address = location.getString("address");
                                String street = location.getString("street");
                                String locality = location.getString("locality");
                                String city = location.getString("city");
                                String state = location.getString("state");
                                String zip = location.getString("zip");

                                session.setUserID(_id);
                                session.setUserName(name);
                                session.setPhone(mobile);
                                session.setEmail(emailID);
                                //session.setEmailVerifyStatus(emailVerified);
                                session.setAddress1(address);
                                session.setState(state);
                                session.setCity(city);
                                session.setPinCode(zip);
                                session.setStreet(street);
                                session.setlocality(locality);

                                session.setLogin();

                                Intent i = new Intent(getApplicationContext(), DashBoard.class);
                                startActivity(i);
                                finish();

                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {

                            Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);

                                    String data = jsonError.getString("msg");
                                    Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void ForgotPWd(String phn) {

        Log.d("successresponceVolley", "started");
        progressbar.showDialog();
        JSONObject paramjson = new JSONObject();
        try {

            paramjson.put("mobile", phn);

            Log.d("successresponceVolley", "" + paramjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(ServerLinks.forgotPassword_url, paramjson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("successresponceVolley", "" + response);
                            String code = response.getString("code");

                            if (code.equalsIgnoreCase("200")) {
                                forgotpwdDialog.dismiss();
                                progressbar.hideDialog();

                                OTPDialog(phn);

                            } else {
                                String message = response.getString("msg");

                                progressbar.hideDialog();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                progressbar.hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }
            }


        });

        request_json.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request_json);
    }

    private void ChangePWd(String phn, String pw, String otp) {
        Log.d("successresponceVolley", "started");
        progressbar.showDialog();
        JSONObject paramjson = new JSONObject();
        try {

            paramjson.put("mobile", phn);
            paramjson.put("password", pw);
            paramjson.put("otp", otp);

            Log.d("successresponceVolley", "" + paramjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(ServerLinks.resetPassword_url, paramjson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("successresponceVolley", "" + response);
                            String code = response.getString("code");

                            if (code.equalsIgnoreCase("200")) {
                                newpwddialog.dismiss();
                                otpdialog.dismiss();
                                progressbar.hideDialog();


                                Toast.makeText(getApplicationContext(), "Password Reset Successfull", Toast.LENGTH_SHORT).show();

                            } else {
                                String message = response.getString("msg");

                                progressbar.hideDialog();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                progressbar.hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginActivity.this, "OTP didn't Match", Toast.LENGTH_SHORT).show();
                            newpwddialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }
            }


        });

        request_json.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request_json);
    }


    /*public void verifayOtp(String countryCode,String Mobile,String Otp){

        progressbar.showDialog();

        Map<String,String> params = new HashMap<>(  );

        params.put("countryCode",countryCode);
        params.put("mobile",Mobile);
        params.put("otp",Otp);

        JSONObject jsonObject1 = new JSONObject ( params );

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.PUT, ServerLinks.verify_otp, jsonObject1, new Response.Listener<JSONObject> ( ) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                progressbar.hideDialog();

                try {

                    String code = response.getString("code");

                    if(code.equals("200")){

                        String message = response.getString("msg");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else if(code.equals("400")){

                        String message = response.getString("msg");
                        Toast.makeText(OtpVerification.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss ();
                error.printStackTrace();
                Toast.makeText (OtpVerification.this, ""+error, Toast.LENGTH_SHORT).show ( );


            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue (OtpVerification.this);
        requestQueue.add (jsonObjectRequest);

    }*/


}