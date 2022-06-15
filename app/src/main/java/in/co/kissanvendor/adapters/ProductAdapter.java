package in.co.kissanvendor.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.kissanvendor.R;
import in.co.kissanvendor.activities.EditProductDetails;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;
import in.co.kissanvendor.models.ProductGetSet;
import in.co.kissanvendor.models.ProductImageGetSet;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private Context context;
    private List<ProductGetSet> accepetedJobLists;
    private List<ProductGetSet> filteredlist;
    public int cnt;
    SessionManager session;

    public ProductAdapter(List<ProductGetSet> accepetedJobLists, Context context) {
        this.accepetedJobLists = accepetedJobLists;
        this.filteredlist = accepetedJobLists;
        this.context = context;

    }


    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        ProductAdapter.MyViewHolder holder = new ProductAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ProductAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        session = new SessionManager(context);


        ProductGetSet movie = filteredlist.get(position);

        holder.productname.setText(movie.getTitle());
        holder.stock.setText("Stock : "+movie.getInStock());
        holder.price.setText("₹"+movie.getPrice());
        holder.discount.setText("₹"+movie.getDiscount());
        holder.discount.setPaintFlags(holder.discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(movie.getProductimagearray().size()!=0) {
            String im = "https://kisaanandfactory.com/static_file/"+movie.getProductimagearray().get(0).getImageurl();
          /*  if(im.contains("uploads/")){
                im=im.replace("uploads/", "https://kisaanandfactory.com/static_file/");
            }*/
            Log.d("fegdzxd", im);
            Glide.with(context)
                    .load(im)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .error(R.drawable.product_image))
                    .into(holder.productimage);
        }else{
            Glide.with(context)
                    .load(R.drawable.product_image)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .error(R.drawable.product_image))
                    .into(holder.productimage);
        }

        holder.img_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productId = movie.get_id();

                deleteProduct(productId,position);
                filteredlist.remove(position);
                notifyDataSetChanged();
                notifyItemChanged(position);

            }
        });

        holder.img_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditProductDetails.class);
                intent.putExtra("productId",movie.get_id());
                intent.putExtra("categoryId",movie.getCategoryId());
                intent.putExtra("experience",movie.getExperience());
                intent.putExtra("soldBy",movie.getSoldBy());
                intent.putExtra("title",movie.getTitle());
                intent.putExtra("price",movie.getPrice());
                intent.putExtra("type",movie.getType());
                intent.putExtra("discount",movie.getDiscount());
                intent.putExtra("description",movie.getDescription());

                intent.putExtra("weight",movie.getWeight().get(0).getWeight());
                intent.putExtra("inStock",movie.getInStock());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return filteredlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productname, stock, price, discount;
        public ImageView productimage;
        RelativeLayout img_Delete,img_Edit;

        public MyViewHolder(View itemView) {
            super(itemView);

            productname = (TextView) itemView.findViewById(R.id.productname);
            stock = (TextView) itemView.findViewById(R.id.stock);
            price = (TextView) itemView.findViewById(R.id.price);
            discount = (TextView) itemView.findViewById(R.id.discount);
            productimage = (ImageView) itemView.findViewById(R.id.productimage);
            img_Delete = (RelativeLayout) itemView.findViewById(R.id.img_Delete);
            img_Edit = (RelativeLayout) itemView.findViewById(R.id.img_Edit);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredlist = accepetedJobLists;
                } else {
                    ArrayList<ProductGetSet> filteredList = new ArrayList<>();
                    for (ProductGetSet row : accepetedJobLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredlist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredlist = (ArrayList<ProductGetSet>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void deleteProduct(String productId,int position){


        String productDelete = ServerLinks.productDelete_url+productId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, productDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("fvsDevbf", response);
                    Log.d("fvsDevbffggg", jsonObject.toString());
                    Toast.makeText(context, "Product delete Successfully", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);


    }



}