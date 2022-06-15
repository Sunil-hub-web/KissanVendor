package in.co.kissanvendor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.kissanvendor.R;
import in.co.kissanvendor.activities.AddNewProduct;
import in.co.kissanvendor.activities.DashBoard;
import in.co.kissanvendor.activities.LoginActivity;
import in.co.kissanvendor.adapters.ProductAdapter;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;
import in.co.kissanvendor.models.ProductGetSet;
import in.co.kissanvendor.models.ProductImageGetSet;
import in.co.kissanvendor.models.Weight_ModelClass;


public class ProductListFragment extends Fragment {

    TextView addnewproductbtn;
    RecyclerView product_recyclerview;
    SessionManager session;
    ViewDialog progressbar;
    ArrayList<ProductGetSet> productarray = new ArrayList<ProductGetSet>();
    ArrayList<ProductImageGetSet> productimagearray = new ArrayList<ProductImageGetSet>();
    ArrayList<Weight_ModelClass> weightArray = new ArrayList<Weight_ModelClass>();
    TextView totalproducts, instockproducts, outofstockproducts;
    ProgressBar outofstockprogress, instockprogress, totalproductprogress;
    ProductAdapter adapter;
    EditText searchproduct_ed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);

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

        GetProductList();

        return v;
    }

    public void InIt(View v){

        product_recyclerview = v.findViewById(R.id.product_recyclerview);
        addnewproductbtn = v.findViewById(R.id.addnewproductbtn);

        totalproducts = v.findViewById(R.id.totalproducts);
        instockproducts = v.findViewById(R.id.instockproducts);
        outofstockproducts = v.findViewById(R.id.outofstockproducts);

        outofstockprogress = v.findViewById(R.id.outofstockprogress);
        instockprogress = v.findViewById(R.id.instockprogress);
        totalproductprogress = v.findViewById(R.id.totalproductprogress);
        searchproduct_ed = v.findViewById(R.id.searchproduct_ed);
        searchproduct_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    adapter.getFilter().filter("");
                }else{
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void GetProductList() {
        progressbar.showDialog();

        Log.d("fvsDevbf", ServerLinks.getProduct_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getProduct_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SignUp_Activity.this, response, Toast.LENGTH_LONG).show();
                        progressbar.hideDialog();
                        Log.d("fvsDevbf", response);
                        try {
                            productarray = new ArrayList<ProductGetSet>();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {

                                String total = jsonObject.getString("total");
                                String instock = jsonObject.getString("instock");
                                String outofstock = jsonObject.getString("outofstock");

                                totalproducts.setText(total);
                                instockproducts.setText(instock);
                                outofstockproducts.setText(outofstock);

                                int toto = Integer.parseInt(total);
                                int inso = Integer.parseInt(instock);
                                int ouso = Integer.parseInt(outofstock);
                                int inavg;
                                int outavg;

                                if(toto==0 || inso==0) {
                                    inavg = 0;
                                }else{
                                    inavg = (inso / toto) * 100;
                                }
                                if(toto==0 || ouso==0) {
                                    outavg = 0;
                                }else{
                                    outavg = (ouso / toto) * 100;
                                }

                                totalproductprogress.setProgress(100);
                                instockprogress.setProgress(inavg);
                                outofstockprogress.setProgress(outavg);

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("products");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String discount = jsobjectitem.getString("discount");
                                    String subcategoryId = jsobjectitem.getString("subcategoryId");
                                    String totalRating = jsobjectitem.getString("totalRating");
                                    String avgRating = jsobjectitem.getString("avgRating");
                                    String sold = jsobjectitem.getString("sold");
                                    String _id = jsobjectitem.getString("_id");
                                    String addedBy = jsobjectitem.getString("addedBy");
                                    String title = jsobjectitem.getString("title");
                                    String price = jsobjectitem.getString("price");
                                    String type = jsobjectitem.getString("type");
                                    String description = jsobjectitem.getString("description");
                                    String soldBy = jsobjectitem.getString("soldBy");
                                    String inStock = jsobjectitem.getString("inStock");
                                    String experience = jsobjectitem.getString("experience");
                                    String categoryId = jsobjectitem.getString("categoryId");
                                    String createdAt = jsobjectitem.getString("createdAt");
                                    String updatedAt = jsobjectitem.getString("updatedAt");

                                    productimagearray = new ArrayList<ProductImageGetSet>();
                                    JSONArray imagesarr = jsobjectitem.getJSONArray("images");

                                    for (int k = 0; k < imagesarr.length(); k++) {

                                        String images = imagesarr.getString(k);
                                        productimagearray.add(new ProductImageGetSet(images));

                                    }

                                    weightArray = new ArrayList<>();
                                    JSONArray weightarr = jsobjectitem.getJSONArray("weight");
                                    for(int j=0;j<weightarr.length();j++){

                                        String weight = weightarr.getString(j);
                                        weightArray.add(new Weight_ModelClass(weight));

                                    }

                                    productarray.add(new ProductGetSet(discount, subcategoryId, totalRating, avgRating, sold, _id, addedBy,
                                            title, price, type, description, soldBy, inStock, experience, categoryId, createdAt, updatedAt, productimagearray,weightArray));

                                }

                                product_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                product_recyclerview.setNestedScrollingEnabled(false);
                                adapter = new ProductAdapter(productarray, getActivity());
                                product_recyclerview.setAdapter(adapter);

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
                Log.d("fvsDevbf", ""+auth);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", ""+params);
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