package in.co.kissanvendor.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.co.kissanvendor.R;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;

public class RegisterActivity extends AppCompatActivity {

    ViewDialog progressbar;
    SessionManager session;
    Button signup_btn;
    TextView i_already_h;
    EditText fullname, mobilenumber, email, newpassword, confirmpassword;
    String fn, mn, em, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        i_already_h = findViewById(R.id.i_already_h);
        fullname = findViewById(R.id.fullname);
        mobilenumber = findViewById(R.id.mobilenumber);
        email = findViewById(R.id.email);
        newpassword = findViewById(R.id.newpassword);
        confirmpassword = findViewById(R.id.confirmpassword);

        signup_btn = findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullname.getText().toString().trim().equals("")){
                    fullname.setError("enter full name");
                    fullname.requestFocus();

                }else if(fullname.getText().toString().trim().length()<=5){

                    fullname.setError("enter mobile number");
                    fullname.requestFocus();

                }else if(!isValidUserName(fullname.getText().toString().trim())){

                    fullname.setError("enter name");
                    fullname.requestFocus();

                }else if(mobilenumber.getText().toString().trim().length()==0){
                    mobilenumber.setError("enter mobile number");
                    mobilenumber.requestFocus();

                }else if(mobilenumber.getText().toString().trim().length()!=10){
                    mobilenumber.setError("must be 10 digits");
                    mobilenumber.requestFocus();

                }
                else if(email.getText().toString().trim().length()==0){
                    email.setError("enter email");
                    email.requestFocus();

                }else if(!isEmailValid(email.getText().toString())){
                    email.setError("enter valid email");
                    email.requestFocus();

                } else if(newpassword.getText().toString().trim().length()==0){
                    newpassword.setError("enter password");
                    newpassword.requestFocus();

                }else if(newpassword.getText().toString().trim().length()<8){
                    newpassword.setError("password must be 8 characters");
                    newpassword.requestFocus();

                }else if(!newpassword.getText().toString().equalsIgnoreCase(confirmpassword.getText().toString())){
                    confirmpassword.setError("password didn't match");
                    confirmpassword.requestFocus();

                }else{

                    fn = fullname.getText().toString();
                    mn = mobilenumber.getText().toString().trim();
                    em = email.getText().toString().trim().toLowerCase();
                    pw = newpassword.getText().toString();

                    SignUpUser();
                }

            }
        });

        i_already_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void SignUpUser() {
        Log.d("successresponceVolley", "started");
        progressbar.showDialog();
        JSONObject paramjson = new JSONObject();
        try {

            paramjson.put("name", fn);
            paramjson.put("mobile", mn);
            paramjson.put("password", pw);
            paramjson.put("emailID", em);

            Log.d("successresponceVolley", "" + paramjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(ServerLinks.SignUp_url, paramjson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressbar.hideDialog();

                        try {
                            Log.d("successresponceVolley", "" + response);


                            String err = response.getString("err");
                            String code = response.getString("code");

                            if (err.equals("false")) {

                                 String status = response.getString("msg");

                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();


                            } else {
                                String message = response.getString("Result");

                                progressbar.hideDialog();
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                            if (error.networkResponse.statusCode == 400) {
                                String data = jsonError.getString("msg");
                                Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT).show();

//                            } else if (error.networkResponse.statusCode == 404) {
//                                JSONArray data = jsonError.getJSONArray("msg");
//                                JSONObject jsonitemChild = data.getJSONObject(0);
//                                String ms = jsonitemChild.toString();
//                                Toast.makeText(RegisterActivity.this, ms, Toast.LENGTH_SHORT).show();
//
//                            }
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
}