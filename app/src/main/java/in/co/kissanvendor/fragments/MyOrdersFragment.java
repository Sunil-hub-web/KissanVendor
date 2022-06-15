package in.co.kissanvendor.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.kissanvendor.R;
import in.co.kissanvendor.adapters.CancelledOrderAdapter;
import in.co.kissanvendor.extras.ServerLinks;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.extras.ViewDialog;
import in.co.kissanvendor.models.OrderGetSet;

public class MyOrdersFragment extends Fragment {

    RecyclerView orders_recycler;
    ArrayList<OrderGetSet> orderarray;
    ArrayList<OrderGetSet> orderarray1;
    ArrayList<OrderGetSet> orderarray2;
    ArrayList<OrderGetSet> orderarray3;
    ArrayList<OrderGetSet> cancelledorderarray;
    TabLayout tablayout;
    LinearLayout changestatus_lay;
    boolean ifselect = false;
    TextView backbtn, changestatusbtn;
    Dialog changeStatusDIalog;
    ViewDialog progressbar;
    SessionManager session;

    String order_Id,statues;
    Spinner spinner_Status;

    String[] orderStatus = {"-Select Status-", "pending", "packed", "shipped", "canceled"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myorders, container, false);

        InIt(v);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("fvsdzx", ""+tablayout.getSelectedTabPosition());

                if(tablayout.getSelectedTabPosition()==0){

                    SetPendingData();

                }else if(tablayout.getSelectedTabPosition()==1){

                    SetCanceledData();

                }else if(tablayout.getSelectedTabPosition()==2){

                    SetPackedData();

                }else if(tablayout.getSelectedTabPosition()==3){

                  SetShippedData();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPendingData();
                changestatus_lay.setVisibility(View.GONE);
            }
        });

        changestatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeStatusDialog();
            }
        });

        //SetPendingData();
        GetOrderHistory();
        return v;
    }

    public void InIt(View v){

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        orders_recycler = v.findViewById(R.id.orders_recycler);
        tablayout = v.findViewById(R.id.tablayout);
        changestatus_lay = v.findViewById(R.id.changestatus_lay);
        backbtn = v.findViewById(R.id.backbtn);
        changestatusbtn = v.findViewById(R.id.changestatusbtn);
    }

    public void SetPendingData(){

        orders_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orders_recycler.setNestedScrollingEnabled(false);
        PendingOrderAdapter adapter = new PendingOrderAdapter(orderarray, getActivity());
        orders_recycler.setAdapter(adapter);

    }

    public void SetShippedData(){

        orders_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orders_recycler.setNestedScrollingEnabled(false);
        ShippedOdderAdapter adapter = new ShippedOdderAdapter(orderarray1, getActivity());
        orders_recycler.setAdapter(adapter);

    }

    public void SetPackedData(){

        orders_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orders_recycler.setNestedScrollingEnabled(false);
        PackedOdderAdapter adapter = new PackedOdderAdapter(orderarray2, getActivity());
        orders_recycler.setAdapter(adapter);

    }

    public void SetCanceledData(){

        orders_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orders_recycler.setNestedScrollingEnabled(false);
        CancelledOrderAdapter adapter = new CancelledOrderAdapter(cancelledorderarray, getActivity());
        orders_recycler.setAdapter(adapter);
    }

    public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.MyViewHolder> implements Filterable {

        private LayoutInflater inflater;
        private Context context;
        private List<OrderGetSet> accepetedJobLists;
        private List<OrderGetSet> filteredlist;
        public int cnt;



        public PendingOrderAdapter(List<OrderGetSet> accepetedJobLists, Context context) {
            this.accepetedJobLists = accepetedJobLists;
            this.filteredlist = accepetedJobLists;
            this.context = context;

        }


        @Override
        public PendingOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.from(parent.getContext()).inflate(R.layout.pendingorder_item_layout, parent, false);
            PendingOrderAdapter.MyViewHolder holder = new PendingOrderAdapter.MyViewHolder(view);
            return holder;

        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final PendingOrderAdapter.MyViewHolder holder, final int position) {

            OrderGetSet movie = filteredlist.get(position);

            try {
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                java.util.Date date = form.parse(movie.getCreatedAt());
                //SimpleDateFormat postFormater = new SimpleDateFormat("MMMMM, dd, yyyy");
                SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
                String newDateStr = postFormater.format(date);

                SimpleDateFormat postFormater1 = new SimpleDateFormat("hh:mm");
                String newTimeStr = postFormater1.format(date);

                holder.heading_date.setText(newDateStr);
                holder.text_ProductDate.setText(newDateStr);
                //booking_time.setText(newTimeStr);

                Log.d("changedatime",newDateStr);


            }
            catch (ParseException e) {

                e.printStackTrace();
                Log.d("changedat",e.toString());
            }

            order_Id = movie.get_id();

            holder.heading_name.setText(movie.getName());
            holder.heading_orderid.setText(movie.getPaymentMethod());
            holder.heading_amount.setText(movie.getTotalAmount());


            holder.text_OrderId.setText(movie.get_id());
            holder.text_productname.setText(movie.getProductnames());
            holder.text_ProductStatues.setText(movie.getOrderStatus());
            holder.text_ProductQty.setText(movie.getQuantities());
            holder.text_Amount.setText(movie.getTotalAmount());
            holder.text_PaymentMode.setText(movie.getPaymentMethod());
            holder.text_DeliveryTo.setText(movie.getName());
            holder.text_MobileNo.setText(movie.getContacts());

            holder.headinglayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.headinglayout.setVisibility(View.GONE);
                    holder.detaillayout.setVisibility(View.VISIBLE);
                }
            });
            holder.detaillayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.headinglayout.setVisibility(View.VISIBLE);
                    holder.detaillayout.setVisibility(View.GONE);
                }
            });
            holder.changestat_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeStatusDialog();
                }
            });
            holder.selectioncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked==true){
                        filteredlist.get(position).setIsselected("yes");
                    }else{
                        filteredlist.get(position).setIsselected("no");
                    }

                    checkIfItemSelected();
                }
            });

        }

        @Override
        public int getItemCount() {

            return filteredlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView heading_date, heading_name, heading_orderid, heading_amount;
            public RelativeLayout heading_viewdetails;
            public CheckBox selectioncheckbox;
            public LinearLayout headinglayout, detaillayout, changestat_l;

            TextView text_ProductDate,text_OrderId,text_productname,text_ProductStatues,text_ProductQty,
                    text_Amount,text_PaymentMode,text_DeliveryTo,text_MobileNo;

            public MyViewHolder(View itemView) {
                super(itemView);

                heading_date = (TextView) itemView.findViewById(R.id.heading_date);
                heading_name = (TextView) itemView.findViewById(R.id.heading_name);
                heading_orderid = (TextView) itemView.findViewById(R.id.heading_orderid);
                heading_amount = (TextView) itemView.findViewById(R.id.heading_amount);
                heading_viewdetails = (RelativeLayout) itemView.findViewById(R.id.heading_viewdetails);
                headinglayout = (LinearLayout) itemView.findViewById(R.id.headinglayout);
                detaillayout = (LinearLayout) itemView.findViewById(R.id.detaillayout);
                changestat_l = (LinearLayout) itemView.findViewById(R.id.changestat_l);
                selectioncheckbox = (CheckBox) itemView.findViewById(R.id.selectioncheckbox);


                text_ProductDate = (TextView) itemView.findViewById(R.id.text_ProductDate);
                text_OrderId = (TextView) itemView.findViewById(R.id.text_OrderId);
                text_productname = (TextView) itemView.findViewById(R.id.text_productname);
                text_ProductStatues = (TextView) itemView.findViewById(R.id.text_ProductStatues);
                text_ProductQty = (TextView) itemView.findViewById(R.id.text_ProductQty);
                text_Amount = (TextView) itemView.findViewById(R.id.text_Amount);
                text_PaymentMode = (TextView) itemView.findViewById(R.id.text_PaymentMode);
                text_DeliveryTo = (TextView) itemView.findViewById(R.id.text_DeliveryTo);
                text_MobileNo = (TextView) itemView.findViewById(R.id.text_MobileNo);



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
                        ArrayList<OrderGetSet> filteredList = new ArrayList<>();
                        for (OrderGetSet row : accepetedJobLists) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getOrderStatus().toLowerCase().contains(charString.toLowerCase())) {
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
                    filteredlist = (ArrayList<OrderGetSet>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

    }

    public class ShippedOdderAdapter extends RecyclerView.Adapter<ShippedOdderAdapter.MyViewHolder> implements  Filterable {

        private LayoutInflater inflater;
        private Context context;
        private List<OrderGetSet> accepetedJobLists;
        private List<OrderGetSet> filteredlist;
        public int cnt;

        public ShippedOdderAdapter(ArrayList<OrderGetSet> orderarray, Context activity) {

            this.accepetedJobLists = orderarray;
            this.filteredlist = orderarray;
            this.context = activity;

        }

        @NonNull
        @Override
        public ShippedOdderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = inflater.from(parent.getContext()).inflate(R.layout.pendingorder_item_layout, parent, false);
            ShippedOdderAdapter.MyViewHolder holder = new ShippedOdderAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ShippedOdderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            OrderGetSet movie = filteredlist.get(position);

            try {
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                java.util.Date date = form.parse(movie.getCreatedAt());
                //SimpleDateFormat postFormater = new SimpleDateFormat("MMMMM, dd, yyyy");
                SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
                String newDateStr = postFormater.format(date);

                SimpleDateFormat postFormater1 = new SimpleDateFormat("hh:mm");
                String newTimeStr = postFormater1.format(date);

                holder.heading_date.setText(newDateStr);
                holder.text_ProductDate.setText(newDateStr);
                //booking_time.setText(newTimeStr);

                Log.d("changedatime",newDateStr);


            }
            catch (ParseException e) {

                e.printStackTrace();
                Log.d("changedat",e.toString());
            }

            order_Id = movie.get_id();

            holder.heading_name.setText(movie.getName());
            holder.heading_orderid.setText(movie.getPaymentMethod());
            holder.heading_amount.setText(movie.getTotalAmount());


            holder.text_OrderId.setText(movie.get_id());
            holder.text_productname.setText(movie.getProductnames());
            holder.text_ProductStatues.setText(movie.getOrderStatus());
            holder.text_ProductQty.setText(movie.getQuantities());
            holder.text_Amount.setText(movie.getTotalAmount());
            holder.text_PaymentMode.setText(movie.getPaymentMethod());
            holder.text_DeliveryTo.setText(movie.getName());
            holder.text_MobileNo.setText(movie.getContacts());

            holder.headinglayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.headinglayout.setVisibility(View.GONE);
                    holder.detaillayout.setVisibility(View.VISIBLE);
                }
            });
            holder.detaillayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.headinglayout.setVisibility(View.VISIBLE);
                    holder.detaillayout.setVisibility(View.GONE);
                }
            });
            holder.changestat_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeStatusDialog();
                }
            });
            holder.selectioncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked==true){
                        filteredlist.get(position).setIsselected("yes");
                    }else{
                        filteredlist.get(position).setIsselected("no");
                    }

                    checkIfItemSelected1();
                }
            });

        }

        @Override
        public int getItemCount() {

            return filteredlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView heading_date, heading_name, heading_orderid, heading_amount;
            public RelativeLayout heading_viewdetails;
            public CheckBox selectioncheckbox;
            public LinearLayout headinglayout, detaillayout, changestat_l;

            TextView text_ProductDate,text_OrderId,text_productname,text_ProductStatues,text_ProductQty,
                    text_Amount,text_PaymentMode,text_DeliveryTo,text_MobileNo;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                heading_date = (TextView) itemView.findViewById(R.id.heading_date);
                heading_name = (TextView) itemView.findViewById(R.id.heading_name);
                heading_orderid = (TextView) itemView.findViewById(R.id.heading_orderid);
                heading_amount = (TextView) itemView.findViewById(R.id.heading_amount);
                heading_viewdetails = (RelativeLayout) itemView.findViewById(R.id.heading_viewdetails);
                headinglayout = (LinearLayout) itemView.findViewById(R.id.headinglayout);
                detaillayout = (LinearLayout) itemView.findViewById(R.id.detaillayout);
                changestat_l = (LinearLayout) itemView.findViewById(R.id.changestat_l);
                selectioncheckbox = (CheckBox) itemView.findViewById(R.id.selectioncheckbox);


                text_ProductDate = (TextView) itemView.findViewById(R.id.text_ProductDate);
                text_OrderId = (TextView) itemView.findViewById(R.id.text_OrderId);
                text_productname = (TextView) itemView.findViewById(R.id.text_productname);
                text_ProductStatues = (TextView) itemView.findViewById(R.id.text_ProductStatues);
                text_ProductQty = (TextView) itemView.findViewById(R.id.text_ProductQty);
                text_Amount = (TextView) itemView.findViewById(R.id.text_Amount);
                text_PaymentMode = (TextView) itemView.findViewById(R.id.text_PaymentMode);
                text_DeliveryTo = (TextView) itemView.findViewById(R.id.text_DeliveryTo);
                text_MobileNo = (TextView) itemView.findViewById(R.id.text_MobileNo);

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
                        ArrayList<OrderGetSet> filteredList = new ArrayList<>();
                        for (OrderGetSet row : accepetedJobLists) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getOrderStatus().toLowerCase().contains(charString.toLowerCase())) {
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
                    filteredlist = (ArrayList<OrderGetSet>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

    }

    public class PackedOdderAdapter extends RecyclerView.Adapter<PackedOdderAdapter.MyViewHolder> implements Filterable {

        private LayoutInflater inflater;
        private Context context;
        private List<OrderGetSet> accepetedJobLists;
        private List<OrderGetSet> filteredlist;
        public int cnt;

        public PackedOdderAdapter(ArrayList<OrderGetSet> orderarray, Context activity) {

            this.accepetedJobLists = orderarray;
            this.filteredlist = orderarray;
            this.context = activity;

        }

        @NonNull
        @Override
        public PackedOdderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = inflater.from(parent.getContext()).inflate(R.layout.pendingorder_item_layout, parent, false);
            PackedOdderAdapter.MyViewHolder holder = new PackedOdderAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PackedOdderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            OrderGetSet movie = filteredlist.get(position);

            try {
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                java.util.Date date = form.parse(movie.getCreatedAt());
                //SimpleDateFormat postFormater = new SimpleDateFormat("MMMMM, dd, yyyy");
                SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
                String newDateStr = postFormater.format(date);

                SimpleDateFormat postFormater1 = new SimpleDateFormat("hh:mm");
                String newTimeStr = postFormater1.format(date);

                holder.heading_date.setText(newDateStr);
                holder.text_ProductDate.setText(newDateStr);
                //booking_time.setText(newTimeStr);

                Log.d("changedatime",newDateStr);


            }
            catch (ParseException e) {

                e.printStackTrace();
                Log.d("changedat",e.toString());
            }

            order_Id = movie.get_id();

            holder.heading_name.setText(movie.getName());
            holder.heading_orderid.setText(movie.getPaymentMethod());
            holder.heading_amount.setText(movie.getTotalAmount());


            holder.text_OrderId.setText(movie.get_id());
            holder.text_productname.setText(movie.getProductnames());
            holder.text_ProductStatues.setText(movie.getOrderStatus());
            holder.text_ProductQty.setText(movie.getQuantities());
            holder.text_Amount.setText(movie.getTotalAmount());
            holder.text_PaymentMode.setText(movie.getPaymentMethod());
            holder.text_DeliveryTo.setText(movie.getName());
            holder.text_MobileNo.setText(movie.getContacts());

            holder.headinglayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.headinglayout.setVisibility(View.GONE);
                    holder.detaillayout.setVisibility(View.VISIBLE);
                }
            });
            holder.detaillayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.headinglayout.setVisibility(View.VISIBLE);
                    holder.detaillayout.setVisibility(View.GONE);
                }
            });
            holder.changestat_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeStatusDialog();
                }
            });
            holder.selectioncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked==true){
                        filteredlist.get(position).setIsselected("yes");
                    }else{
                        filteredlist.get(position).setIsselected("no");
                    }

                    checkIfItemSelected2();
                }
            });

        }

        @Override
        public int getItemCount() {

            return filteredlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView heading_date, heading_name, heading_orderid, heading_amount;
            public RelativeLayout heading_viewdetails;
            public CheckBox selectioncheckbox;
            public LinearLayout headinglayout, detaillayout, changestat_l;

            TextView text_ProductDate,text_OrderId,text_productname,text_ProductStatues,text_ProductQty,
                    text_Amount,text_PaymentMode,text_DeliveryTo,text_MobileNo;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                heading_date = (TextView) itemView.findViewById(R.id.heading_date);
                heading_name = (TextView) itemView.findViewById(R.id.heading_name);
                heading_orderid = (TextView) itemView.findViewById(R.id.heading_orderid);
                heading_amount = (TextView) itemView.findViewById(R.id.heading_amount);
                heading_viewdetails = (RelativeLayout) itemView.findViewById(R.id.heading_viewdetails);
                headinglayout = (LinearLayout) itemView.findViewById(R.id.headinglayout);
                detaillayout = (LinearLayout) itemView.findViewById(R.id.detaillayout);
                changestat_l = (LinearLayout) itemView.findViewById(R.id.changestat_l);
                selectioncheckbox = (CheckBox) itemView.findViewById(R.id.selectioncheckbox);


                text_ProductDate = (TextView) itemView.findViewById(R.id.text_ProductDate);
                text_OrderId = (TextView) itemView.findViewById(R.id.text_OrderId);
                text_productname = (TextView) itemView.findViewById(R.id.text_productname);
                text_ProductStatues = (TextView) itemView.findViewById(R.id.text_ProductStatues);
                text_ProductQty = (TextView) itemView.findViewById(R.id.text_ProductQty);
                text_Amount = (TextView) itemView.findViewById(R.id.text_Amount);
                text_PaymentMode = (TextView) itemView.findViewById(R.id.text_PaymentMode);
                text_DeliveryTo = (TextView) itemView.findViewById(R.id.text_DeliveryTo);
                text_MobileNo = (TextView) itemView.findViewById(R.id.text_MobileNo);

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
                        ArrayList<OrderGetSet> filteredList = new ArrayList<>();
                        for (OrderGetSet row : accepetedJobLists) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getOrderStatus().toLowerCase().contains(charString.toLowerCase())) {
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
                    filteredlist = (ArrayList<OrderGetSet>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    public void checkIfItemSelected() {
        ifselect = false;
        for (int l = 0; l < orderarray.size(); l++) {

            if(orderarray.get(l).getIsselected().equalsIgnoreCase("yes")){
                ifselect = true;
            }

        }

        if(ifselect==true){
            changestatus_lay.setVisibility(View.VISIBLE);
        }else{
            changestatus_lay.setVisibility(View.GONE);
        }
    }
    public void checkIfItemSelected1() {
        ifselect = false;
        for (int l = 0; l < orderarray1.size(); l++) {

            if(orderarray1.get(l).getIsselected().equalsIgnoreCase("yes")){
                ifselect = true;
            }

        }

        if(ifselect==true){
            changestatus_lay.setVisibility(View.VISIBLE);
        }else{
            changestatus_lay.setVisibility(View.GONE);
        }
    }
    public void checkIfItemSelected2() {
        ifselect = false;
        for (int l = 0; l < orderarray2.size(); l++) {

            if(orderarray2.get(l).getIsselected().equalsIgnoreCase("yes")){
                ifselect = true;
            }

        }

        if(ifselect==true){
            changestatus_lay.setVisibility(View.VISIBLE);
        }else{
            changestatus_lay.setVisibility(View.GONE);
        }
    }

    private void ChangeStatusDialog() {

        changeStatusDIalog = new Dialog(getActivity());
        changeStatusDIalog.setContentView(R.layout.changestatus_dialog);
        Window window = changeStatusDIalog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView sure = changeStatusDIalog.findViewById(R.id.submit_btn);
        TextView cancel = changeStatusDIalog.findViewById(R.id.back);
        spinner_Status = changeStatusDIalog.findViewById(R.id.spinner_Status);

        ArrayAdapter OrderStatusAdapter = new ArrayAdapter(getActivity(), R.layout.spinnerfront2, orderStatus);
        OrderStatusAdapter.setDropDownViewResource(R.layout.spinneritem);
        spinner_Status.setAdapter(OrderStatusAdapter);
        spinner_Status.setSelection(-1, true);

        spinner_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                statues = spinner_Status.getSelectedItem().toString();
                Toast.makeText(getActivity(), statues, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //String statues = spinner_Status.getSelectedItem().toString();

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StatusUpdate(statues);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusDIalog.dismiss();
            }
        });

        changeStatusDIalog.setCancelable(false);
        changeStatusDIalog.show();
    }

    private void GetOrderHistory()  {
        progressbar.showDialog();
        orderarray = new ArrayList<OrderGetSet>();
        orderarray1 = new ArrayList<OrderGetSet>();
        orderarray2 = new ArrayList<OrderGetSet>();
        cancelledorderarray = new ArrayList<OrderGetSet>();

        orderarray.clear();
        orderarray1.clear();
        orderarray2.clear();
        cancelledorderarray.clear();

        Log.d("fvsDevbf", ServerLinks.orderHistory_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.orderHistory_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SignUp_Activity.this, response, Toast.LENGTH_LONG).show();
                        progressbar.hideDialog();
                        Log.d("fvsDevbfff", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("code").equals("200")) {

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("orders");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String expectedDelivery = jsobjectitem.getString("expectedDelivery");
                                    String discountPrice = jsobjectitem.getString("discountPrice");
                                    String oderedBy = jsobjectitem.getString("oderedBy");
                                    String cart_id = jsobjectitem.getString("cart_id");
                                    String orderImg = jsobjectitem.getString("orderImg");
                                    String _id = jsobjectitem.getString("_id");
                                    String shippingCharge = jsobjectitem.getString("shippingCharge");
                                    String totalAmount = jsobjectitem.getString("totalAmount");
                                    String seller = jsobjectitem.getString("seller");
                                    String orderStatus = jsobjectitem.getString("orderStatus");
                                    String __v = jsobjectitem.getString("__v");
                                    String createdAt = jsobjectitem.getString("createdAt");
                                    String updatedAt = jsobjectitem.getString("updatedAt");

                                    JSONObject paymentDetails = jsobjectitem.getJSONObject("paymentDetails");
                                    String paymentMethod = paymentDetails.getString("paymentMethod");
                                    String paymentStatus = paymentDetails.getString("paymentStatus");

                                    JSONObject trackingDetails = jsobjectitem.getJSONObject("trackingDetails");
                                    String orderedtime = trackingDetails.getString("ordered");
                                    String packedtime = trackingDetails.getString("packed");
                                    String shippedtime = trackingDetails.getString("shipped");

                                    JSONObject shippingDetails = jsobjectitem.getJSONObject("shippingDetails");
                                    String name = shippingDetails.getString("name");
                                    String addressID = shippingDetails.getString("addressID");
                                    String contacts = shippingDetails.getString("contacts");


                                    JSONArray products = jsobjectitem.getJSONArray("products");

                                    String productnames = "";
                                    String quantities = "";

                                    for (int k = 0; k < products.length(); k++) {

                                        JSONObject productsitem = products.getJSONObject(k);

                                        JSONObject jsonObject_product = productsitem.getJSONObject("product");

                                        if(productnames.length()==0){
                                            productnames = jsonObject_product.getString("title");
                                        }else{
                                            productnames = productnames+", "+jsonObject_product.getString("title");
                                        }
                                        if(quantities.length()==0){
                                            quantities = productsitem.getString("productQuantity");
                                        }else{
                                            quantities = quantities+", "+jsonObject_product.getString("title");
                                        }

                                    }

                                    if(orderStatus.equalsIgnoreCase("cancelled")){

                                        cancelledorderarray.add(new OrderGetSet(expectedDelivery, discountPrice, createdAt, updatedAt, _id, oderedBy, cart_id, orderImg, shippingCharge, totalAmount, seller,
                                                orderStatus, __v, paymentMethod, paymentStatus, orderedtime, packedtime, shippedtime, name, addressID, contacts, productnames, quantities, "no"));

                                    }else if(orderStatus.equalsIgnoreCase("shipped")) {

                                        orderarray1.add(new OrderGetSet(expectedDelivery, discountPrice, createdAt, updatedAt, _id, oderedBy, cart_id, orderImg, shippingCharge, totalAmount, seller,
                                                orderStatus, __v, paymentMethod, paymentStatus, orderedtime, packedtime, shippedtime, name, addressID, contacts, productnames, quantities, "no"));

                                        Log.d("gsybjnsb",orderarray1.toString());

                                    }else if(orderStatus.equalsIgnoreCase("packed")) {

                                        orderarray2.add(new OrderGetSet(expectedDelivery, discountPrice, createdAt, updatedAt, _id, oderedBy, cart_id, orderImg, shippingCharge, totalAmount, seller,
                                                orderStatus, __v, paymentMethod, paymentStatus, orderedtime, packedtime, shippedtime, name, addressID, contacts, productnames, quantities, "no"));

                                        Log.d("gsybjnsb",orderarray2.toString());

                                    }else{

                                        orderarray.add(new OrderGetSet(expectedDelivery, discountPrice, createdAt, updatedAt, _id, oderedBy, cart_id, orderImg, shippingCharge, totalAmount, seller,
                                                orderStatus, __v, paymentMethod, paymentStatus, orderedtime, packedtime, shippedtime, name, addressID, contacts, productnames, quantities, "no"));

                                        Log.d("gsybjnsb",orderarray.toString());
                                    }
                                }

                                SetPendingData();

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
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", ""+params);
                return params;
            }*/
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void StatusUpdate(String status){

        progressbar.showDialog();

        String url = ServerLinks.statusupdate+order_Id;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressbar.hideDialog();

                try {
                    String code = response.getString("code");
                    String err = response.getString("err");
                    String msg = response.getString("msg");

                    if(code.equals("200")){

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        changeStatusDIalog.dismiss();
                        GetOrderHistory();
                        changestatus_lay.setVisibility(View.GONE);

                    }else{

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = session.getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", ""+auth);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);
    }

}