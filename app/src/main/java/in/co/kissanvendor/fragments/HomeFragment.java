package in.co.kissanvendor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.co.kissanvendor.R;
import in.co.kissanvendor.activities.AddNewProduct;
import in.co.kissanvendor.activities.DashBoard;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;

public class HomeFragment extends Fragment {

    SessionManager session;
    ViewDialog progressbar;
    TextView addnewproductbtn, productlistbtn, totalordertxt, pendingorderstxt, dispatchedordertxt,
            dispatchcounttxt, pendingcounttxt, totalcounttxt,
            bottomtotalproduct, bottominstock, bottompending, bottomoutofstock, bottomhold, bottomcancel;
    ProgressBar totalordersprogressbar, pendingordersprogressbar, dispatchedordersprogressbar;
    LinearLayout totprodlay, instprodlay, oustprodlay;

    SwipeRefreshLayout swapRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        InIt(v);

        addnewproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddNewProduct.class);
                startActivity(i);
            }
        });
        productlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductListFragment fragment2 = new ProductListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment2);
                fragmentTransaction.commit();
            }
        });

        swapRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetHomeDatas();

            }
        });


        return v;
    }

    public void gotoProductListing(){

        DashBoard.dashboardview.setVisibility(View.GONE);
        DashBoard.dashboard.setTextColor(getResources().getColor(R.color.navtextcolor));

        DashBoard.productlistingview.setVisibility(View.VISIBLE);
        DashBoard.productlisting.setTextColor(getResources().getColor(R.color.colorPrimary));
        DashBoard.pagetitle.setText("Products");
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new ProductListFragment())
                .commit();
    }

    public void InIt(View v){

        addnewproductbtn = v.findViewById(R.id.addnewproductbtn);
        productlistbtn = v.findViewById(R.id.productlistbtn);

        totalordertxt = v.findViewById(R.id.totalordertxt);
        pendingorderstxt = v.findViewById(R.id.pendingorderstxt);
        dispatchedordertxt = v.findViewById(R.id.dispatchedordertxt);
        dispatchcounttxt = v.findViewById(R.id.dispatchcounttxt);
        pendingcounttxt = v.findViewById(R.id.pendingcounttxt);
        totalcounttxt = v.findViewById(R.id.totalcounttxt);
        bottomtotalproduct = v.findViewById(R.id.bottomtotalproduct);
        bottominstock = v.findViewById(R.id.bottominstock);
        bottompending = v.findViewById(R.id.bottompending);
        bottomoutofstock = v.findViewById(R.id.bottomoutofstock);
        bottomhold = v.findViewById(R.id.bottomhold);
        bottomcancel = v.findViewById(R.id.bottomcancel);
        swapRefresh = v.findViewById(R.id.swapRefresh);

        totalordersprogressbar = v.findViewById(R.id.totalordersprogressbar);
        pendingordersprogressbar = v.findViewById(R.id.pendingordersprogressbar);
        dispatchedordersprogressbar = v.findViewById(R.id.dispatchedordersprogressbar);

        totprodlay = v.findViewById(R.id.totprodlay);
        instprodlay = v.findViewById(R.id.instprodlay);
        oustprodlay = v.findViewById(R.id.oustprodlay);

        totprodlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductListing();
            }
        });

        oustprodlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductListing();
            }
        });

        instprodlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductListing();
            }
        });



        GetHomeDatas();
    }

    private void GetHomeDatas() {
        progressbar.showDialog();

        Log.d("fvsDevbf", ServerLinks.getHomeData_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getHomeData_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SignUp_Activity.this, response, Toast.LENGTH_LONG).show();

                        Log.d("fvsDevbf", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {

                                String totalOrders = jsonObject.getString("totalOrders");
                                String totalOrdersToday = jsonObject.getString("totalOrders");
                                String dispatchOrdersToday = jsonObject.getString("dispatchOrdersToday");
                                String pendingOrdersToday = jsonObject.getString("pendingOrdersToday");

                                String totalProducts = jsonObject.getString("totalProducts");
                                String inStockProducts = jsonObject.getString("inStockProducts");
                                String outOfStockProducts = jsonObject.getString("outOfStockProducts");

                                bottomtotalproduct.setText(totalProducts);
                                bottominstock.setText(inStockProducts);
                                bottomoutofstock.setText(outOfStockProducts);

                                totalordertxt.setText(totalOrdersToday);
                                pendingorderstxt.setText(pendingOrdersToday);
                                dispatchedordertxt.setText(dispatchOrdersToday);


                                int toto = Integer.parseInt(totalOrdersToday);
                                int peno = Integer.parseInt(pendingOrdersToday);
                                int diso = Integer.parseInt(dispatchOrdersToday);

                                int penavg,penavg1;
                                int disavg,disavg1;

                                if(toto==0 || peno==0) {
                                     penavg = 0;
                                }else{
                                     penavg = (peno / toto) * 100;
                                }

                                if(toto==0 || diso==0) {
                                    disavg = 0;
                                }else{
                                     disavg = (diso/toto)*100;
                                }

                                penavg1 = (int)Math.round(penavg);
                                disavg1 = (int)Math.round(disavg);


                                totalcounttxt.setText("100%");
                                pendingcounttxt.setText(penavg1+"%");
                                dispatchcounttxt.setText(disavg1+"%");

                                pendingordersprogressbar.setProgress(penavg1);
                                dispatchedordersprogressbar.setProgress(disavg1);


                                progressbar.hideDialog();

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

}