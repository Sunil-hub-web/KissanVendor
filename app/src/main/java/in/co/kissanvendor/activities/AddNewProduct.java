package in.co.kissanvendor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewProduct extends AppCompatActivity {

    ImageView menu;
    ViewDialog progressbar;
    SessionManager session;
    TextView addnewproductbtn;
    EditText productname, stock, weight, discount, retailprice, gst, description;
    Spinner categories_spinner, producttype_spinner, prodty_spinner;
    String catname = "", photostr1 = "", photostr2 = "", photostr3 = "", photoselection = "", catid = "", subcatcatname = "", subcatcatid = "", typename = "", typeid = "", ttl="", des="", sold="";
    HashMap<String, String> hashCategories = new HashMap<String, String>();
    ArrayList<String> CategoriesArray = new ArrayList<>();
    HashMap<String, String> hashProducttype = new HashMap<String, String>();
    ArrayList<String> ProducttypeArray = new ArrayList<>();
    ArrayList<String> typeArray = new ArrayList<>();
    ArrayList<String> ImageArray = new ArrayList<>();
    ImageView productimage1, productimage2, productimage3;
    private String userChoosenTask;
    private final int PICK_IMAGE_CAMERA = 3, PICK_IMAGE_GALLERY = 4;
    private Bitmap bitmap;
    Uri photouri;
    boolean photoselected = false;
    private File destination = null;
    private String imgPath = null;

    int prc = 0,  stok = 0, exp = 0, wt = 0, disc = 0;


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
                    subcatcatname = "";
                    subcatcatid = "";

                    ProducttypeArray = new ArrayList<>();
                    hashProducttype = new HashMap<String, String>();
                    ProducttypeArray.add(0, "Select Sub-Category");

                    ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                            R.layout.spinnerfront2, ProducttypeArray);
                    dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                    producttype_spinner.setAdapter(dataAdapterVehicle);

                } else {

                    subcatcatname = "";
                    subcatcatid = "";
                    catid = hashCategories.get(catname);

                    GetProductType();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
            }
        });

        producttype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcatcatname = producttype_spinner.getItemAtPosition(producttype_spinner.getSelectedItemPosition()).toString();
                if (subcatcatname.equalsIgnoreCase("Select Sub-Category")) {

                    subcatcatid = "";

                } else {

                    subcatcatid = hashProducttype.get(subcatcatname);

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

                    if(typename.equalsIgnoreCase("Goods")) {
                        typeid = "product";
                    }else{
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

        addnewproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageArray = new ArrayList<>();
                if(photostr1.length()!=0){
                    ImageArray.add(photostr1);
                }
                if(photostr2.length()!=0){
                    ImageArray.add(photostr2);
                }
                if(photostr3.length()!=0){
                    ImageArray.add(photostr3);
                }

                if(productname.getText().toString().trim().length()==0){
                    productname.setError("enter product name");
                    productname.requestFocus();

                }else if(typeid.toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(), "select product type", Toast.LENGTH_SHORT).show();

                }else if(catid.toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(), "select category", Toast.LENGTH_SHORT).show();

                }else if(subcatcatid.toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(), "select sub-category", Toast.LENGTH_SHORT).show();

                }else if(stock.getText().toString().trim().length()==0){
                    stock.setError("enter stock");
                    stock.requestFocus();

                }else if(weight.getText().toString().trim().length()==0){
                    weight.setError("enter product weight");
                    weight.requestFocus();

                }else if(discount.getText().toString().trim().length()==0){
                    discount.setError("enter product discount");
                    discount.requestFocus();

                }else if(retailprice.getText().toString().trim().length()==0){
                    retailprice.setError("enter product retail price");
                    retailprice.requestFocus();

                }else {

                    ttl=productname.getText().toString();
                    prc= Integer.parseInt(retailprice.getText().toString());
                    disc= Integer.parseInt(discount.getText().toString());
                    des=description.getText().toString();
                    sold=session.getUserID();
                    stok= Integer.parseInt(stock.getText().toString());
                    exp=0;
                    wt= Integer.parseInt(weight.getText().toString());

                    addProduct();
                }
            }
        });

        typeArray = new ArrayList<>();
        typeArray.add("Select Type");
        typeArray.add("Goods");
        typeArray.add("Services");

        ArrayAdapter<String>typVehicle = new ArrayAdapter<String>(AddNewProduct.this,
                R.layout.spinnerfront2, typeArray);
        typVehicle.setDropDownViewResource(R.layout.spinneritem);
        prodty_spinner.setAdapter(typVehicle);

        GetCategories();
    }

    public void InIt() {

        menu = findViewById(R.id.menu);
        categories_spinner = findViewById(R.id.categories_spinner);
        prodty_spinner = findViewById(R.id.prodty_spinner);
        producttype_spinner = findViewById(R.id.producttype_spinner);
        productimage1 = findViewById(R.id.productimage1);
        productimage2 = findViewById(R.id.productimage2);
        productimage3 = findViewById(R.id.productimage3);
        addnewproductbtn = findViewById(R.id.addnewproductbtn);
        productname = findViewById(R.id.productname);
        stock = findViewById(R.id.stock);
        weight = findViewById(R.id.weight);
        discount = findViewById(R.id.discount);
        retailprice = findViewById(R.id.retailprice);
        gst = findViewById(R.id.gst);
        description = findViewById(R.id.description);

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

        Log.d("fvsDevbf", ServerLinks.getProductTypes_url+catid);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.getProductTypes_url+catid,
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
            paramjson.put("price", prc);
            paramjson.put("type", typeid);
            paramjson.put("discount", disc);
            paramjson.put("description", des);
            paramjson.put("soldBy", sold);
            paramjson.put("subcategoryId", subcatcatid);
            paramjson.put("inStock", stok);
            paramjson.put("experience", exp);
            paramjson.put("images", imagejson);
            paramjson.put("weight", wt);
            paramjson.put("categoryId", catid);

            Log.d("successresponceVolley", "" + paramjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ServerLinks.addProduct_url, paramjson,new Response.Listener<JSONObject>() {
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
                Log.d("fvsDevbf", ""+auth);
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
                    Log.d("vbrfxgfecs", "Pick from Camera::>>> "+bitmap);
                    photouri = Uri.parse(path);
                    Log.d("vbrfxgfe", "Pick from Camera::>>> "+photouri);
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

        String path = RealPathUtil.getRealPath(AddNewProduct.this,photouri);
        File file = new File(path);

        RequestBody imageBode = RequestBody.create(MediaType.parse(getContentResolver().getType(photouri)), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", "productimage.png", imageBode);

        Log.d("fvsdz", ""+photouri);



        Call<ImageResponse> call = new ApiToJsonHandler().uploadImage(session.getToken(), partImage);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, retrofit2.Response<ImageResponse> response) {

                if (response.isSuccessful()) {

                    progressbar.hideDialog();

                    // get the path and save it to images array
                    Log.d("fvsdzfvfc", ""+response.body().getMsg().getFilename());
                    Log.d("fvsdzfvfc", ""+response.body().getMsg().getPath());

                    if(photoselection.equalsIgnoreCase("1")){
                        productimage1.setImageBitmap(bitmap);
                        photostr1 = response.body().getMsg().getFilename();

                    }else if(photoselection.equalsIgnoreCase("2")){
                        productimage2.setImageBitmap(bitmap);
                        photostr2 = response.body().getMsg().getFilename();

                    }else if(photoselection.equalsIgnoreCase("3")){
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
                Log.d("getMessage",t.getMessage());
            }
        });
    }
}

