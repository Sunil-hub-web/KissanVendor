package in.co.kissanvendor.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.gson.Gson;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.kissanvendor.R;
import in.co.kissanvendor.extras.ApiResponse;
import in.co.kissanvendor.extras.ApiToJsonHandler;
import in.co.kissanvendor.extras.FileUtils;
import in.co.kissanvendor.extras.ImageResponse;
import in.co.kissanvendor.extras.RealPathUtil;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import top.defaults.colorpicker.ColorPickerPopup;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AddNewProduct extends AppCompatActivity {

    ImageView menu;
    ViewDialog progressbar;
    SessionManager session;
    TextView addnewproductbtn, priceClc;
    EditText productname, stock, weight, discount, retailprice, gst, description, servicecharges,
            commission, totalpayment, dimention, edit_color, quentity;
    Spinner categories_spinner, producttype_spinner, prodty_spinner, prodty1_spinner, supercategory,
            subcategory_spinner;
    String catname = "", photostr1 = "", photostr2 = "", photostr3 = "", photoselection = "",
            catid = "", subcatcatname = "", subcatcatid = "", typename = "", typeid = "", ttl = "",
            des = "", sold = "", super_category = "", supercat = "", product_type = "", produ_Type = "";
    HashMap<String, String> hashCategories = new HashMap<String, String>();
    ArrayList<String> CategoriesArray = new ArrayList<>();
    HashMap<String, String> hashProducttype = new HashMap<String, String>();
    ArrayList<String> ProducttypeArray = new ArrayList<>();
    ArrayList<String> typeArray = new ArrayList<>();
    ArrayList<String> typeArray1;
    Map<String, Object> type_Array;
    ArrayList<String> ImageArray = new ArrayList<>();
    ImageView productimage1, productimage2, productimage3;
    private String userChoosenTask;
    private final int PICK_IMAGE_CAMERA = 3, PICK_IMAGE_GALLERY = 4;
    private Bitmap bitmap;
    Uri photouri;
    boolean photoselected = false;
    private final File destination = null;
    private final String imgPath = null;

    int prc = 0, stok = 0, exp = 0, wt = 0, disc = 0;
    float pricetot;
    Boolean bool_productType = false;

    ArrayList<String> superCategoryList;
    HashMap<String, String> super_CategoryList;

    ArrayList<String> categoryList;
    HashMap<String, String> category_List;

    ArrayList<String> subcCategoryList = new ArrayList<>();
    HashMap<String, String> subCategory_List = new HashMap<>();

    JSONObject jsonObject_metadata;
    int mDefaultColor = 0xffffff00;

    String charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        InIt();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catname = categories_spinner.getItemAtPosition(categories_spinner.getSelectedItemPosition()).toString();
                if (catname.equalsIgnoreCase("Select Category")) {

                    catid = "";

                } else {

                    //subcatcatname = "";
                    //subcatcatid = "";
                    catid = category_List.get(catname);

                    //GetProductType();

                    getSubCategory(catid);

                    extraCharge(catid);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
            }
        });

        subcategory_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcatcatname = subcategory_spinner.getItemAtPosition(subcategory_spinner.getSelectedItemPosition()).toString();
                if (subcatcatname.equalsIgnoreCase("select SubCategory")) {

                    subcatcatid = "";

                } else {

                    subcatcatid = subCategory_List.get(subcatcatname);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Type", Toast.LENGTH_SHORT).show();
            }
        });

        prodty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typename = prodty_spinner.getItemAtPosition(prodty_spinner.getSelectedItemPosition()).toString();
                if (typename.equalsIgnoreCase("Select Type")) {

                    typeid = "";

                } else {

                    if (typename.equalsIgnoreCase("Goods")) {
                        typeid = "product";
                    } else {
                        typeid = "service";
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Type", Toast.LENGTH_SHORT).show();
            }
        });

        prodty1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                product_type = prodty1_spinner.getItemAtPosition(prodty1_spinner.getSelectedItemPosition()).toString();

                if (product_type.equalsIgnoreCase("Select Product Type")) {

                    //Toast.makeText(AddNewProduct.this, "Select Product Type", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("hshkjbsan", product_type);

                    if (product_type.equalsIgnoreCase("Refundable")) {

                        bool_productType = true;

                    } else {

                        bool_productType = false;
                    }

                    //bool_productType = Boolean.parseBoolean(produ_Type);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(getApplicationContext(), "Select Type", Toast.LENGTH_SHORT).show();
            }
        });

        supercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                super_category = supercategory.getItemAtPosition(supercategory.getSelectedItemPosition()).toString();

                if (super_category.equalsIgnoreCase("Select SuperCategory")) {

                    supercat = "";

                } else {

                    supercat = super_CategoryList.get(super_category);

                    getCategory(supercat);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        productimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoselection = "1";
                selectImg();
            }
        });

        productimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoselection = "2";
                selectImg();
            }
        });

        productimage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoselection = "3";
                selectImg();
            }
        });

        priceClc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (retailprice.getText().toString().trim().length() == 0) {
                    retailprice.setError("enter product retail price");
                    retailprice.requestFocus();

                } else if (discount.getText().toString().trim().length() == 0) {
                    discount.setError("enter discount price");
                    discount.requestFocus();

                } else {

                    String str_gst = gst.getText().toString().trim();
                    String str_servicecharges = servicecharges.getText().toString().trim();
                    String str_commission = commission.getText().toString().trim();
                    String str_VenderPrice = retailprice.getText().toString().trim();
                    String str_Discount = discount.getText().toString().trim();

                    int int_gst = Integer.valueOf(str_gst);
                    int int_servicecharges = Integer.valueOf(str_servicecharges);
                    int int_commission = Integer.valueOf(str_commission);
                    int int_VenderPrice = Integer.valueOf(str_VenderPrice);
                    int int_Discount = Integer.valueOf(str_Discount);

                    float disc = int_VenderPrice * int_Discount / 100;
                    float tot_pric = int_VenderPrice - disc;
                    float gst = tot_pric * 18 / 100;
                    float commi = tot_pric * 5 / 100;
                    float coservic = tot_pric * 1 / 100;
                    pricetot = tot_pric + gst + commi + coservic;

                    DecimalFormat df = new DecimalFormat("#.##");
                    String pricetot1 = df.format(pricetot);

                    Log.d("ghghgh", String.valueOf(pricetot1));

                    totalpayment.setText(pricetot1);

                    // priceClculator(int_VenderPrice,int_Discount,0,int_commission,int_servicecharges,int_gst);


                }

            }
        });

        addnewproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageArray = new ArrayList<>();
                if (photostr1.length() != 0) {
                    ImageArray.add(photostr1);
                }
                if (photostr2.length() != 0) {
                    ImageArray.add(photostr2);
                }
                if (photostr3.length() != 0) {
                    ImageArray.add(photostr3);
                }

                if (productname.getText().toString().trim().length() == 0) {
                    productname.setError("enter product name");
                    productname.requestFocus();

                } else if (typeid.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "select product type", Toast.LENGTH_SHORT).show();

                } else if (catid.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "select category", Toast.LENGTH_SHORT).show();

                } else if (subcatcatid.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "select sub-category", Toast.LENGTH_SHORT).show();

                } else if (stock.getText().toString().trim().length() == 0) {
                    stock.setError("enter stock");
                    stock.requestFocus();

                } else if (weight.getText().toString().trim().length() == 0) {
                    weight.setError("enter product weight");
                    weight.requestFocus();

                } else if (discount.getText().toString().trim().length() == 0) {
                    discount.setError("enter product discount");
                    discount.requestFocus();

                } else if (retailprice.getText().toString().trim().length() == 0) {
                    retailprice.setError("enter product retail price");
                    retailprice.requestFocus();

                } else if (product_type.trim().length() == 0) {

                    Toast.makeText(AddNewProduct.this, "Select Product Type", Toast.LENGTH_SHORT).show();

                } else if (totalpayment.getText().toString().trim().length() == 0) {

                    Toast.makeText(AddNewProduct.this, "Please select total price", Toast.LENGTH_SHORT).show();

                } else {

                    ttl = productname.getText().toString();
                    prc = Integer.parseInt(retailprice.getText().toString());
                    disc = Integer.parseInt(discount.getText().toString());
                    des = description.getText().toString();
                    sold = session.getUserID();
                    stok = Integer.parseInt(stock.getText().toString());
                    exp = 0;
                    wt = Integer.parseInt(weight.getText().toString());

                    String str_dimention = dimention.getText().toString().trim();
                    String str_color = edit_color.getText().toString().trim();

                    jsonObject_metadata = new JSONObject();
                    try {
                        jsonObject_metadata.put("color", str_color);
                        jsonObject_metadata.put("dimension", str_dimention);
                        jsonObject_metadata.put("quantity", stok);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addProduct();
                }
            }
        });

        typeArray = new ArrayList<>();
        typeArray.add("Select Type");
        typeArray.add("Goods");
        typeArray.add("Services");

        ArrayAdapter<String> typVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                R.layout.spinnerfront2, typeArray);
        typVehicle.setDropDownViewResource(R.layout.spinneritem);
        prodty_spinner.setAdapter(typVehicle);

        typeArray1 = new ArrayList<>();
        typeArray1.add("Select Product Type");
        typeArray1.add("Refundable");
        typeArray1.add("Non-Refundable");

        type_Array = new HashMap<>();
        type_Array.put("Refundable", "true");
        type_Array.put("Non-Refundable", "false");

        ArrayAdapter<String> typVehicle1 = new ArrayAdapter<String>(AddNewProduct.this,
                R.layout.spinnerfront2, typeArray1);
        typVehicle1.setDropDownViewResource(R.layout.spinneritem);
        prodty1_spinner.setAdapter(typVehicle1);

        //GetCategories();

        getsuperCatecory();

        edit_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // colorPicker(view);

                openDialog(true);
            }
        });
    }

    public void InIt() {

        menu = findViewById(R.id.menu);
        categories_spinner = findViewById(R.id.categories_spinner);
        prodty_spinner = findViewById(R.id.prodty_spinner);
        prodty1_spinner = findViewById(R.id.prodty1_spinner);
        subcategory_spinner = findViewById(R.id.subcategory_spinner);
        productimage1 = findViewById(R.id.productimage1);
        productimage2 = findViewById(R.id.productimage2);
        productimage3 = findViewById(R.id.productimage3);
        addnewproductbtn = findViewById(R.id.addnewproductbtn);
        supercategory = findViewById(R.id.supercategory);
        productname = findViewById(R.id.productname);
        stock = findViewById(R.id.stock);
        weight = findViewById(R.id.weight);
        discount = findViewById(R.id.discount);
        retailprice = findViewById(R.id.retailprice);
        gst = findViewById(R.id.gst);
        description = findViewById(R.id.description);
        priceClc = findViewById(R.id.priceClc);
        servicecharges = findViewById(R.id.servicecharges);
        commission = findViewById(R.id.commission);
        totalpayment = findViewById(R.id.totalpayment);
        dimention = findViewById(R.id.dimention);
        edit_color = findViewById(R.id.color);
        quentity = findViewById(R.id.quentity);


    }

    private void GetCategories() {
        progressbar.showDialog();
        CategoriesArray = new ArrayList<>();
        hashCategories = new HashMap<String, String>();

        Log.d("fvsDevbf", ServerLinks.getCategories_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getCategories_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressbar.hideDialog();
                        Log.d("fvsDevbf", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {

                                JSONArray dataorder = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataorder.length(); i++) {
                                    JSONObject itemarray = dataorder.getJSONObject(i);
                                    String _id = itemarray.getString("_id");
                                    String name = itemarray.getString("name");

                                    hashCategories.put(name, _id);
                                    CategoriesArray.add(name);

                                }

                                CategoriesArray.add(0, "Select Category");

                                ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                        R.layout.spinnerfront2, CategoriesArray);
                                dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                categories_spinner.setAdapter(dataAdapterVehicle);

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
                                    Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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

    private void GetProductType() {
        progressbar.showDialog();
        ProducttypeArray = new ArrayList<>();
        hashProducttype = new HashMap<String, String>();

        Log.d("fvsDevbf", ServerLinks.getProductTypes_url + catid);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getProductTypes_url + catid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {


                                JSONArray dataorder = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataorder.length(); i++) {
                                    JSONObject itemarray = dataorder.getJSONObject(i);
                                    String _id = itemarray.getString("_id");
                                    String name = itemarray.getString("name");

                                    hashProducttype.put(name, _id);
                                    ProducttypeArray.add(name);
                                }

                                ProducttypeArray.add(0, "Select Sub-Category");

                                ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                        R.layout.spinnerfront2, ProducttypeArray);
                                dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                producttype_spinner.setAdapter(dataAdapterVehicle);

                                progressbar.hideDialog();
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

                            ProducttypeArray.add(0, "Select Type");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                    R.layout.spinnerfront2, ProducttypeArray);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                            producttype_spinner.setAdapter(dataAdapterVehicle);

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {

                            Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    ProducttypeArray.add(0, "Select Type");

                                    ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                            R.layout.spinnerfront2, ProducttypeArray);
                                    dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                    producttype_spinner.setAdapter(dataAdapterVehicle);

                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);

                                    String data = jsonError.getString("msg");
                                    Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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

    private void addProduct() {

        progressbar.showDialog();
        JSONObject paramjson = new JSONObject();
        JSONArray imagejson = new JSONArray();
        for (int m = 0; m < ImageArray.size(); m++) {
            imagejson.put(ImageArray.get(m));
        }

        try {

            paramjson.put("title", ttl);
            paramjson.put("price", pricetot);
            paramjson.put("type", typeid);
            paramjson.put("discount", disc);
            paramjson.put("description", des);
            paramjson.put("soldBy", sold);
            paramjson.put("subcategoryId", subcatcatid);
            paramjson.put("inStock", stok);
            paramjson.put("experience", exp);
            paramjson.put("images", imagejson);
            paramjson.put("weight", wt);
            paramjson.put("isRefundable", bool_productType);
            paramjson.put("categoryId", catid);
            paramjson.put("metadata", jsonObject_metadata);

            Log.d("successresponceVolley", "" + paramjson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ServerLinks.addProduct_url, paramjson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());
                try {
                    Log.d("successresponceVolley", "" + response);
                    String code = response.getString("code");

                    if (code.equalsIgnoreCase("200")) {

                        String message = response.getString("msg");

                        progressbar.hideDialog();
                        Toast.makeText(AddNewProduct.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String message = response.getString("msg");

                        progressbar.hideDialog();
                        Toast.makeText(AddNewProduct.this, message, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();

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
        };
        request.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    private File createFile(Uri uri) {
        String path = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }
        if (Build.VERSION.SDK_INT < 11) {
            path = FileUtils.getRealPathFromURI_BelowAPI11(this, uri);
        } else if (Build.VERSION.SDK_INT < 19) {
            path = FileUtils.getRealPathFromURI_API11to18(this, uri);
        } else {
            path = FileUtils.getRealPathFromURI_API19(this, uri);
        }

        File image = new File(path);
        return image;
    }

    public void selectImg() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewProduct.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                /*if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    cameraIntent();

                } else*/

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        try {
            final String[] ACCEPT_MIME_TYPES = {
                    "image/*"
            };
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
        } catch (Exception e) {
            Toast.makeText(AddNewProduct.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            Log.d("rfgrvdcs", String.valueOf(e));
            e.printStackTrace();
        }

    }

    private void cameraIntent() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5 && resultCode == RESULT_OK) {

            try {
                // photouri = data.getData();

                productimage1.setImageURI(photouri);
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                String path = MediaStore.Images.Media.insertImage(AddNewProduct.this.getContentResolver(), bitmap, "Title", null);
                Log.d("vbrfxgfecs", "Pick from Camera::>>> " + bitmap);
                photouri = Uri.parse(path);
                Log.d("vbrfxgfe", "Pick from Camera::>>> " + photouri);
                photoselected = true;


                upload(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {

                photouri = data.getData();


                try {

                    InputStream imageStream = AddNewProduct.this.getContentResolver().openInputStream(photouri);

                    bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    photoselected = true;

                    upload(bitmap);

                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                    Log.d("plih", String.valueOf(e));
                }


            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void upload(final Bitmap bitmap) {

        progressbar.showDialog();

        String path = RealPathUtil.getRealPath(AddNewProduct.this, photouri);
        File file = new File(path);

        RequestBody imageBode = RequestBody.create(MediaType.parse(getContentResolver().getType(photouri)), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", "productimage.png", imageBode);

        Log.d("fvsdz", "" + photouri);

        Call<ImageResponse> call = new ApiToJsonHandler().uploadImage(session.getToken(), partImage);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, retrofit2.Response<ImageResponse> response) {

                if (response.isSuccessful()) {

                    progressbar.hideDialog();

                    // get the path and save it to images array
                    Log.d("fvsdzfvfc", "" + response.body().getMsg().getFilename());
                    Log.d("fvsdzfvfc", "" + response.body().getMsg().getPath());

                    if (photoselection.equalsIgnoreCase("1")) {
                        productimage1.setImageBitmap(bitmap);
                        photostr1 = response.body().getMsg().getFilename();

                    } else if (photoselection.equalsIgnoreCase("2")) {
                        productimage2.setImageBitmap(bitmap);
                        photostr2 = response.body().getMsg().getFilename();

                    } else if (photoselection.equalsIgnoreCase("3")) {
                        productimage3.setImageBitmap(bitmap);
                        photostr3 = response.body().getMsg().getFilename();

                    }

                } else {
                    progressbar.hideDialog();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    Toast.makeText(AddNewProduct.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                progressbar.hideDialog();
                Toast.makeText(AddNewProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getMessage", t.getMessage());
            }
        });
    }

    public void getsuperCatecory() {

        progressbar.showDialog();

        superCategoryList = new ArrayList<>();
        super_CategoryList = new HashMap<>();

        superCategoryList.clear();
        super_CategoryList.clear();

        String url = ServerLinks.getSupercategory;

        Log.d("dssjhbjh", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getSupercategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    if (code.equals("200")) {

                        Toast.makeText(AddNewProduct.this, msg, Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i = 0; i < jsonArray_data.length(); i++) {

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String _id = jsonObject_data.getString("_id");
                            String name = jsonObject_data.getString("name");

                            superCategoryList.add(name);
                            super_CategoryList.put(name, _id);
                        }

                        superCategoryList.add(0, "Select SuperCategory");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                R.layout.spinnerfront2, superCategoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                        supercategory.setAdapter(dataAdapterVehicle);

                        serviceCharge();

                    } else {

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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

    public void getCategory(String supercategoryId) {

        progressbar.showDialog();

        categoryList = new ArrayList<>();
        category_List = new HashMap<>();

        categoryList.clear();
        category_List.clear();

        String category = ServerLinks.getCategory + supercategoryId;

        Log.d("hsfjhva", category);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");

                    if (code.equals("200")) {

                        if (jsonObject.has("data")) {

                            String data = jsonObject.getString("data");

                            Toast.makeText(AddNewProduct.this, msg, Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray_data = new JSONArray(data);

                            for (int i = 0; i < jsonArray_data.length(); i++) {

                                JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                                String _id = jsonObject_data.getString("_id");
                                String name = jsonObject_data.getString("name");
                                String productType = jsonObject_data.getString("productType");
                                String superCategoryId = jsonObject_data.getString("superCategoryId");

                                categoryList.add(name);
                                category_List.put(name, _id);
                            }

                            categoryList.add(0, "Select Category");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                    R.layout.spinnerfront2, categoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                            categories_spinner.setAdapter(dataAdapterVehicle);

                        }else{

                           // categoryList.add(0, "Select Category");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                    R.layout.spinnerfront2, categoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                            categories_spinner.setAdapter(dataAdapterVehicle);

                            String message = jsonObject.getString("msg");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        }


                    } else {

                        //categoryList.add(0, "Select Category");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                R.layout.spinnerfront2, categoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                        categories_spinner.setAdapter(dataAdapterVehicle);

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddNewProduct.this);
        requestQueue.add(stringRequest);

    }

    public void getSubCategory(String subCategoryId) {

        progressbar.showDialog();

        subcCategoryList = new ArrayList<>();
        subCategory_List = new HashMap<>();

        subCategory_List.clear();
        subcCategoryList.clear();


        String category = ServerLinks.getSubCategory + subCategoryId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");

                    if (code.equals("200")) {

                        if (jsonObject.has("data")) {

                            String data = jsonObject.getString("data");

                            Toast.makeText(AddNewProduct.this, msg, Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray_data = new JSONArray(data);

                            for (int i = 0; i < jsonArray_data.length(); i++) {

                                JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                                String _id = jsonObject_data.getString("_id");
                                String name = jsonObject_data.getString("name");
                           /* String productType = jsonObject_data.getString("productType");
                            String superCategoryId = jsonObject_data.getString("superCategoryId");*/

                                subcCategoryList.add(name);
                                subCategory_List.put(name, _id);

                            }

                            subcCategoryList.add(0, "select SubCategory");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                    R.layout.spinnerfront2, subcCategoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                            subcategory_spinner.setAdapter(dataAdapterVehicle);

                        } else {

                            subCategory_List.clear();
                            subcCategoryList.clear();

                            String message = jsonObject.getString("msg");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                           // subcCategoryList.add(0, "select SubCategory");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                    R.layout.spinnerfront2, subcCategoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                            subcategory_spinner.setAdapter(dataAdapterVehicle);
                        }

                    } else {

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        //subcCategoryList.add(0, "select SubCategory");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                                R.layout.spinnerfront2, subcCategoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                        subcategory_spinner.setAdapter(dataAdapterVehicle);
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

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddNewProduct.this);
        requestQueue.add(stringRequest);

    }

    public void priceClculator(int priceByVendor, int discount, int refundCharge, int commission, int serviceCharge, int gst) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("priceByVendor", priceByVendor);
            jsonObject.put("discount", discount);
            jsonObject.put("refundCharge", refundCharge);
            jsonObject.put("commission", commission);
            jsonObject.put("serviceCharge", serviceCharge);
            jsonObject.put("gst", gst);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerLinks.priceCalculator, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String code = response.getString("code");
                    String err = response.getString("err");
                    String msg = response.getString("msg");
                    String amountPaidByUser = response.getString("amountPaidByUser");

                    if (code.equals("200")) {

                        totalpayment.setText(amountPaidByUser);

                    } else {

                        Toast.makeText(AddNewProduct.this, msg, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddNewProduct.this);
        requestQueue.add(jsonObjectRequest);

    }

   /* public void colorPicker(View v) {

        new ColorPickerPopup.Builder(AddNewProduct.this).initialColor(
                        Color.RED)
                .enableBrightness(true)
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(v,new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void
                            onColorPicked(int color) {
                                mDefaultColor = color;
                                Toast.makeText(AddNewProduct.this, ""+mDefaultColor, Toast.LENGTH_SHORT).show();
                            }
                        });

    }*/

    void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(AddNewProduct.this, mDefaultColor, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                AddNewProduct.this.mDefaultColor = color;
                displayColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    void displayColor(int color) {

        //String hexColor = Integer.toHexString(color).substring(2);
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        //  Toast.makeText(AddNewProduct.this, "#"+hexColor, Toast.LENGTH_SHORT).show();
        edit_color.setText(hexColor);
    }

    public void serviceCharge() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.serviceCharge, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    JSONArray jsonArray_data = new JSONArray(data);

                    for (int i = 0; i < jsonArray_data.length(); i++) {

                        JSONObject jsonObjec_data = jsonArray_data.getJSONObject(i);

                        charge = jsonObjec_data.getString("charge");
                        String _id = jsonObjec_data.getString("_id");

                    }

                    servicecharges.setText(charge);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
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
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddNewProduct.this);
        requestQueue.add(stringRequest);

    }

    public void extraCharge(String categoryID) {

        // progressbar.showDialog();

        String urlextraCharge = ServerLinks.extraCharge + categoryID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlextraCharge, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                Log.d("hjdbiugs", response.toString());

                try {

                    String crappyPrefix = "null";

                    if (response.startsWith(crappyPrefix)) {
                        response = response.substring(crappyPrefix.length(), response.length());
                    }

                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    if (code.equals("200")) {

                        if (data.equals("null")) {

                            commission.setText("0");
                            gst.setText("0");

                        } else {

                            JSONObject jsonObject_data = new JSONObject(data);

                            String commission1 = jsonObject_data.getString("commission");
                            String refundCharge1 = jsonObject_data.getString("refundCharge");
                            String gst1 = jsonObject_data.getString("gst");
                            String categoryId1 = jsonObject_data.getString("categoryId");
                            String _id1 = jsonObject_data.getString("_id");

                            commission.setText(commission1);
                            gst.setText(gst1);

                            Toast.makeText(AddNewProduct.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(AddNewProduct.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

                    progressbar.hideDialog();
                    e.printStackTrace();

                    Log.d("hsgzxuygjh", e.toString());
                }

            }
        }, new Response.ErrorListener() {
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
                            Toast.makeText(AddNewProduct.this, data, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddNewProduct.this);
        requestQueue.add(stringRequest);

    }
}

