package in.co.kissanvendor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

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
import in.co.kissanvendor.extras.ImageResponse;
import in.co.kissanvendor.extras.RealPathUtil;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;
import in.co.kissanvendor.models.ProductImageGetSet;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProductDetails extends AppCompatActivity {

    EditText productname, stock, weight, discount, retailprice, gst, description,servicecharges,
            commission,totalpayment;
    String str_productname,str_description;
    float str_stock = 0.0F, str_discount = 0.0F, str_weight = 0.0F,str_retailprice = 0.0F,pricetot;
    Spinner categories_spinner, producttype_spinner, prodty_spinner, prodty1_spinner, supercategory,
            subcategory_spinner;
    TextView btn_EditProduct,priceClc;
    ArrayList<String> ImageArray = new ArrayList<>();
    ArrayList<ProductImageGetSet> getProductimagearray = new ArrayList<>();
    ArrayList<String> ProducttypeArray = new ArrayList<>();
    ArrayList<String> typeArray = new ArrayList<>();
    ImageView productimage1, productimage2, productimage3;
    private String userChoosenTask;
    private final int PICK_IMAGE_GALLERY = 4;
    private Bitmap bitmap;
    Uri photouri;
    boolean photoselected = false;
    SessionManager session;
    ViewDialog progressbar;
    String typename = "", typeid = "",productId = "", photostr1 = "", photostr2 = "", photostr3 = "",product = "",
            photoselection = "", categoryId = "", experience = "",soldBy = "",token = "",title = "",price = "",
            type = "",discount1 = "",description1 = "",weight1 = "",inStock = "",SubcategoryId = "",product_type = "";
    SessionManager sessionManager;
    ArrayList<ProductImageGetSet> filelist;

    JSONArray imagejson = new JSONArray();

    ArrayList<String> typeArray1;
    Map<String,String> type_Array;

    ArrayList<String> superCategoryList;
    HashMap<String, String> super_CategoryList;

    ArrayList<String> categoryList;
    HashMap<String, String> category_List;

    ArrayList<String> subcCategoryList = new ArrayList<>();
    HashMap<String, String> subCategory_List = new HashMap<>();

    JSONObject jsonObject_metadata;

    boolean bool_productType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_details);

        prodty_spinner = findViewById(R.id.prodty_spinner);
        productname = findViewById(R.id.productname);
        stock = findViewById(R.id.stock);
        weight = findViewById(R.id.weight);
        discount = findViewById(R.id.discount);
        retailprice = findViewById(R.id.retailprice);
        description = findViewById(R.id.description);
        btn_EditProduct = findViewById(R.id.btn_EditProduct);
        productimage1 = findViewById(R.id.productimage1);
        productimage2 = findViewById(R.id.productimage2);
        productimage3 = findViewById(R.id.productimage3);
        totalpayment = findViewById(R.id.totalpayment);
        prodty1_spinner = findViewById(R.id.prodty1_spinner);
        supercategory = findViewById(R.id.supercategory);
        subcategory_spinner = findViewById(R.id.subcategory_spinner);
        gst = findViewById(R.id.gst);
        priceClc = findViewById(R.id.priceClc);
        servicecharges = findViewById(R.id.servicecharges);
        commission = findViewById(R.id.commission);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        token = session.getToken();

        productId = getIntent().getStringExtra("productId");
        categoryId = getIntent().getStringExtra("categoryId");
        SubcategoryId = getIntent().getStringExtra("SubcategoryId");
        experience = getIntent().getStringExtra("experience");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        type = getIntent().getStringExtra("type");
        discount1 = getIntent().getStringExtra("discount");
        description1 = getIntent().getStringExtra("description");
        weight1 = getIntent().getStringExtra("weight");
        inStock = getIntent().getStringExtra("inStock");
        soldBy = getIntent().getStringExtra("soldBy");

        filelist = (ArrayList<ProductImageGetSet>) getIntent().getSerializableExtra("imagearray");

        //Toast.makeText(this, ""+filelist, Toast.LENGTH_SHORT).show();

        String im = "https://kisaanandfactory.com/static_file/"+filelist.get(0).getImageurl();

        int size = filelist.size();

        /*if(filelist.size() != 0){

            for(int i=0;i < filelist.size();i++){

                String storeImage = "https://kisaanandfactory.com/static_file/"+filelist.get(i).getImageurl();

                imagejson.put(storeImage);
            }
        }*/

        if(size == 1){

            Log.d("fegdzxd", im);
            Glide.with(EditProductDetails.this)
                    .load(im)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .error(R.drawable.product_image))
                    .into(productimage1);

            Log.d("fegdzxd", im);
            Glide.with(EditProductDetails.this)
                    .load(im)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .error(R.drawable.product_image))
                    .into(productimage2);

            Log.d("fegdzxd", im);
            Glide.with(EditProductDetails.this)
                    .load(im)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .error(R.drawable.product_image))
                    .into(productimage3);

        }else if(size == 2){

            String value = "1";

            for(int i=0;i < filelist.size();i++){

                String image = "https://kisaanandfactory.com/static_file/"+filelist.get(i).getImageurl();

                if(value.equals("1")){

                    Log.d("fegdzxd", image);
                    Glide.with(EditProductDetails.this)
                            .load(image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                    .error(R.drawable.product_image))
                            .into(productimage1);

                    value = "2";

                }else if(value.equals("2")){

                    Log.d("fegdzxd", image);
                    Glide.with(EditProductDetails.this)
                            .load(image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                    .error(R.drawable.product_image))
                            .into(productimage2);
                }else{

                    return;
                }

            }

        }else if(size <= 3) {

            String value = "1";

            for (int i = 0; i < filelist.size(); i++) {

                String image = "https://kisaanandfactory.com/static_file/" + filelist.get(i).getImageurl();

                if (value.equals("1")) {

                    Log.d("fegdzxd", image);
                    Glide.with(EditProductDetails.this)
                            .load(image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                    .error(R.drawable.product_image))
                            .into(productimage1);

                    value = "2";

                } else if (value.equals("2")) {

                    Log.d("fegdzxd", image);
                    Glide.with(EditProductDetails.this)
                            .load(image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                    .error(R.drawable.product_image))
                            .into(productimage2);

                    value = "3";

                } else if (value.equals("3")) {

                    Log.d("fegdzxd", image);
                    Glide.with(EditProductDetails.this)
                            .load(image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                    .error(R.drawable.product_image))
                            .into(productimage3);
                } else {

                    return;
                }
            }

        }

        productname.setText(title);
        stock.setText(inStock);
        weight.setText(weight1);
        discount.setText(discount1);
        //retailprice.setText(price);
        description.setText(description1);
        totalpayment.setText(price);

        typeArray = new ArrayList<>();
        typeArray.add("Select Type");
        typeArray.add("Goods");
        typeArray.add("Services");

        ArrayAdapter<String> typVehicle = new ArrayAdapter<String>(EditProductDetails.this,
                R.layout.spinnerfront2, typeArray);
        typVehicle.setDropDownViewResource(R.layout.spinneritem);
        prodty_spinner.setAdapter(typVehicle);

        typeArray1 = new ArrayList<>();
        typeArray1.add("Select Product Type");
        typeArray1.add("Refundable");
        typeArray1.add("Non-Refundable");

        type_Array = new HashMap<>();
        type_Array.put("Refundable","true");
        type_Array.put("Non-Refundable","false");

        ArrayAdapter<String> typVehicle1 = new ArrayAdapter<String>(EditProductDetails.this,
                R.layout.spinnerfront2, typeArray1);
        typVehicle1.setDropDownViewResource(R.layout.spinneritem);
        prodty1_spinner.setAdapter(typVehicle1);

        priceClc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (retailprice.getText().toString().trim().length() == 0) {
                    retailprice.setError("enter product retail price");
                    retailprice.requestFocus();

                }else if (discount.getText().toString().trim().length() == 0) {
                    discount.setError("enter discount price");
                    discount.requestFocus();

                }else{

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
                    String pricetot1 =  df.format(pricetot);

                    Log.d("ghghgh",String.valueOf(pricetot1));

                    totalpayment.setText(pricetot1);

                    // priceClculator(int_VenderPrice,int_Discount,0,int_commission,int_servicecharges,int_gst);


                }

            }
        });

        btn_EditProduct.setOnClickListener(new View.OnClickListener() {
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

                }else if(stock.getText().toString().trim().length()==0){
                    stock.setError("enter stock");
                    stock.requestFocus();

                }else if(product.trim().length()==0){

                    Toast.makeText(getApplicationContext(), "select product type", Toast.LENGTH_SHORT).show();

                }else if(weight.getText().toString().trim().length()==0){
                    weight.setError("enter product weight");
                    weight.requestFocus();

                }else if(discount.getText().toString().trim().length()==0){
                    discount.setError("enter product discount");
                    discount.requestFocus();

                }else if(totalpayment.getText().toString().trim().length()==0){
                    totalpayment.setError("enter product retail price");
                    totalpayment.requestFocus();

                }else {


                    str_productname = productname.getText().toString();
                    str_retailprice = Float.valueOf(totalpayment.getText().toString());
                    str_discount = Float.valueOf(discount.getText().toString());
                    str_description = description.getText().toString();
                    str_stock = Float.valueOf(stock.getText().toString());
                    str_weight = Float.valueOf(weight.getText().toString());
                    boolean isRefundable = Boolean.valueOf(bool_productType);

                    jsonObject_metadata = new JSONObject();
                    try {
                        jsonObject_metadata.put("color","");
                        jsonObject_metadata.put("dimension","");
                        jsonObject_metadata.put("quantity",0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   editProduct(str_productname,str_retailprice,type,str_discount,str_description,soldBy,SubcategoryId,str_stock,str_weight,isRefundable,categoryId,jsonObject_metadata);
                }

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

        prodty1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product_type = prodty1_spinner.getItemAtPosition(prodty1_spinner.getSelectedItemPosition()).toString();
                if (product_type.equalsIgnoreCase("Select Product Type")) {

                   product = "";

                } else {

                    Log.d("hshkjbsan",product_type);

                    if(product_type.equalsIgnoreCase("Refundable")){

                        bool_productType = true;

                    }else{

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


    }

    public void selectImg() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductDetails.this);
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
            Toast.makeText(EditProductDetails.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            Log.d("rfgrvdcs", String.valueOf(e));
            e.printStackTrace();
        }

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
                String path = MediaStore.Images.Media.insertImage(EditProductDetails.this.getContentResolver(), bitmap, "Title", null);
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

                    InputStream imageStream = EditProductDetails.this.getContentResolver().openInputStream(photouri);

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

        String path = RealPathUtil.getRealPath(EditProductDetails.this,photouri);
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
                    Toast.makeText(EditProductDetails.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                progressbar.hideDialog();
                Toast.makeText(EditProductDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getMessage",t.getMessage());
            }
        });
    }

    public void editProduct(String title, float price, String type, float discount, String description,
                            String soldBy,String subcategoryId, float inStock,float weight,
                            boolean isRefundable,String categoryId,JSONObject jsonObject_metadata){

        progressbar.showDialog();

        JSONObject jsonObject = new JSONObject();

        imagejson = new JSONArray();

        if(ImageArray.size() != 0){

            for (int m = 0; m < ImageArray.size(); m++) {
                imagejson.put(ImageArray.get(m));
            }
        }

        try {

            jsonObject.put("title",title);
            jsonObject.put("price",price);
            jsonObject.put("type",type);
            jsonObject.put("discount",discount);
            jsonObject.put("description",description);
            jsonObject.put("soldBy",soldBy);
            jsonObject.put("subcategoryId",subcategoryId);
            jsonObject.put("inStock",inStock);
            jsonObject.put("images",imagejson);
            jsonObject.put("weight",weight);
            jsonObject.put("isRefundable",isRefundable);
            jsonObject.put("categoryId",categoryId);
            jsonObject.put("metadata",jsonObject_metadata);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ServerLinks.editproduct+productId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressbar.hideDialog();

                Log.i("onResponse", response.toString());

                try {

                    Log.d("successresponceVolley", "" + response);
                    String code = response.getString("code");
                    String err = response.getString("err");
                    String msg = response.getString("msg");

                    if(code.equals("200")){

                        Toast.makeText(EditProductDetails.this, msg, Toast.LENGTH_SHORT).show();

                       Intent intent = new Intent(EditProductDetails.this,DashBoard.class);
                       startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                            Toast.makeText(EditProductDetails.this, data, Toast.LENGTH_SHORT).show();

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

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);
    }

    public void getsuperCatecory() {

        progressbar.showDialog();

        superCategoryList = new ArrayList<>();
        super_CategoryList = new HashMap<>();

        String url = ServerLinks.getSupercategory;

        Log.d("dssjhbjh",url);

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

                        Toast.makeText(EditProductDetails.this, msg, Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i = 0; i < jsonArray_data.length(); i++) {

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String _id = jsonObject_data.getString("_id");
                            String name = jsonObject_data.getString("name");

                            superCategoryList.add(name);
                            super_CategoryList.put(name, _id);
                        }

                        superCategoryList.add(0, "Select SuperCategory");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(EditProductDetails.this,
                                R.layout.spinnerfront2, superCategoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                        supercategory.setAdapter(dataAdapterVehicle);

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
                            Toast.makeText(EditProductDetails.this, data, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void getCategory(String supercategoryId){

        progressbar.showDialog();

        categoryList = new ArrayList<>();
        category_List = new HashMap<>();

        String category = ServerLinks.getCategory+supercategoryId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, category, new Response.Listener<String>() {
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

                        Toast.makeText(EditProductDetails.this, msg, Toast.LENGTH_SHORT).show();

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

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(EditProductDetails.this,
                                R.layout.spinnerfront2, categoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                        categories_spinner.setAdapter(dataAdapterVehicle);

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
                            Toast.makeText(EditProductDetails.this, data, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(EditProductDetails.this);
        requestQueue.add(stringRequest);

    }

    public void getSubCategory(String subCategoryId){

        progressbar.showDialog();

        subcCategoryList = new ArrayList<>();
        subCategory_List = new HashMap<>();

        String category = ServerLinks.getSubCategory+subCategoryId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, category, new Response.Listener<String>() {
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

                        Toast.makeText(EditProductDetails.this, msg, Toast.LENGTH_SHORT).show();

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
                        subcCategoryList.add(0,"select SubCategory");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(EditProductDetails.this,
                                R.layout.spinnerfront2, subcCategoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                        subcategory_spinner.setAdapter(dataAdapterVehicle);

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
                            Toast.makeText(EditProductDetails.this, data, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(EditProductDetails.this);
        requestQueue.add(stringRequest);

    }
}