package in.co.kissanvendor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    EditText productname, stock, weight, discount, retailprice, description;
    String str_productname,str_description;
    int str_stock = 0,  str_retailprice = 0, str_discount = 0, str_weight = 0;
    Spinner prodty_spinner;
    TextView btn_EditProduct;
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
    String typename = "", typeid = "",productId = "", photostr1 = "", photostr2 = "", photostr3 = "",
            photoselection = "", categoryId = "", experience = "",soldBy = "",token = "",title = "",price = "",
            type = "",discount1 = "",description1 = "",weight1 = "",inStock = "";
    SessionManager sessionManager;

    JSONArray imagejson = new JSONArray();

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

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        token = session.getToken();

        productId = getIntent().getStringExtra("productId");
        categoryId = getIntent().getStringExtra("categoryId");
        experience = getIntent().getStringExtra("experience");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        type = getIntent().getStringExtra("type");
        discount1 = getIntent().getStringExtra("discount");
        description1 = getIntent().getStringExtra("description");
        weight1 = getIntent().getStringExtra("weight");
        inStock = getIntent().getStringExtra("inStock");
        soldBy = getIntent().getStringExtra("soldBy");

        productname.setText(title);
        stock.setText(inStock);
        weight.setText(weight1);
        discount.setText(discount1);
        retailprice.setText(price);
        description.setText(description1);

        typeArray = new ArrayList<>();
        typeArray.add("Select Type");
        typeArray.add("Goods");
        typeArray.add("Services");

        ArrayAdapter<String> typVehicle = new ArrayAdapter<String>(EditProductDetails.this,
                R.layout.spinnerfront2, typeArray);
        typVehicle.setDropDownViewResource(R.layout.spinneritem);
        prodty_spinner.setAdapter(typVehicle);


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


                    str_productname = productname.getText().toString();
                    str_retailprice = Integer.parseInt(retailprice.getText().toString());
                    str_discount = Integer.parseInt(discount.getText().toString());
                    str_description = description.getText().toString();
                    str_stock = Integer.parseInt(stock.getText().toString());
                    str_weight = Integer.parseInt(weight.getText().toString());
                    int int_experience = Integer.parseInt(experience);

                    if(experience.equals("0")){

                        int_experience = 1;
                    }

                    editProduct(str_productname,str_retailprice,typeid,str_discount,str_description,soldBy,str_stock,int_experience,str_weight);
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

    public void editProduct(String title, int price, String type, int discount, String description,
                            String soldBy, int inStock, int  experience,int weight){

        progressbar.showDialog();

        JSONObject jsonObject = new JSONObject();

            imagejson = new JSONArray();
            for (int m = 0; m < ImageArray.size(); m++) {
                imagejson.put(ImageArray.get(m));
        }

        try {

            jsonObject.put("title",title);
            jsonObject.put("price",price);
            jsonObject.put("type",type);
            jsonObject.put("discount",discount);
            jsonObject.put("description",description);
            jsonObject.put("soldBy",soldBy);
            jsonObject.put("inStock",inStock);
            jsonObject.put("experience",experience);
            jsonObject.put("images",imagejson);
            jsonObject.put("weight",weight);
            jsonObject.put("categoryId",categoryId);

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
}