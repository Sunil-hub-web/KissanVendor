package in.co.kissanvendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.co.kissanvendor.R;
import in.co.kissanvendor.models.OrderGetSet;

public class CancelledOrderAdapter extends RecyclerView.Adapter<CancelledOrderAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private Context context;
    private List<OrderGetSet> accepetedJobLists;
    private List<OrderGetSet> filteredlist;
    public int cnt;



    public CancelledOrderAdapter(List<OrderGetSet> accepetedJobLists, Context context) {
        this.accepetedJobLists = accepetedJobLists;
        this.filteredlist = accepetedJobLists;
        this.context = context;

    }


    @Override
    public CancelledOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.canceled_order_layout, parent, false);
        CancelledOrderAdapter.MyViewHolder holder = new CancelledOrderAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final CancelledOrderAdapter.MyViewHolder holder, final int position) {

        OrderGetSet movie = filteredlist.get(position);

        holder.heading_date.setText(movie.getCreatedAt());
        holder.heading_name.setText(movie.getName());
        holder.heading_orderid.setText(movie.getPaymentMethod());
        holder.heading_amount.setText(movie.getTotalAmount());

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

    }

    @Override
    public int getItemCount() {

        return filteredlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView heading_date, heading_name, heading_orderid, heading_amount;
        public RelativeLayout heading_viewdetails;
        public LinearLayout headinglayout, detaillayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            heading_date = (TextView) itemView.findViewById(R.id.heading_date);
            heading_name = (TextView) itemView.findViewById(R.id.heading_name);
            heading_orderid = (TextView) itemView.findViewById(R.id.heading_orderid);
            heading_amount = (TextView) itemView.findViewById(R.id.heading_amount);
            heading_viewdetails = (RelativeLayout) itemView.findViewById(R.id.heading_viewdetails);
            headinglayout = (LinearLayout) itemView.findViewById(R.id.headinglayout);
            detaillayout = (LinearLayout) itemView.findViewById(R.id.detaillayout);

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