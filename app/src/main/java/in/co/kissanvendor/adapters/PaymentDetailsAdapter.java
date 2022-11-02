package in.co.kissanvendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.co.kissanvendor.R;
import in.co.kissanvendor.models.PaymentDetails;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.viewHolder> {

    Context context;
    ArrayList<PaymentDetails> payment_Deatils;


    public PaymentDetailsAdapter(ArrayList<PaymentDetails> paymentDetails, FragmentActivity activity) {

        this.context = activity;
        this.payment_Deatils = paymentDetails;
    }

    @NonNull
    @Override
    public PaymentDetailsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.walletfragment,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentDetailsAdapter.viewHolder holder, int position) {

        PaymentDetails payment = payment_Deatils.get(position);

        holder.crAmount.setText(payment.getCramount());
        holder.estDate.setText(payment.getEstdate());
        holder.productId.setText(payment.getProductid());
        holder.productName.setText(payment.getProductname());
        holder.status.setText(payment.getStatues());
        holder.type.setText(payment.getType());

        if(payment.getStatues().equals("PENDING")){

            holder.status.setTextColor(ContextCompat.getColor(context, R.color.google_color));
            holder.crAmount.setTextColor(ContextCompat.getColor(context, R.color.google_color));

        }else{

            holder.status.setTextColor(ContextCompat.getColor(context, R.color.btn_color));
            holder.crAmount.setTextColor(ContextCompat.getColor(context, R.color.btn_color));
        }

    }

    @Override
    public int getItemCount() {
        return payment_Deatils.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView productId,productName,status,crAmount,type,estDate;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            estDate = itemView.findViewById(R.id.estDate);
            productId = itemView.findViewById(R.id.productId);
            productName = itemView.findViewById(R.id.productName);
            status = itemView.findViewById(R.id.status);
            crAmount = itemView.findViewById(R.id.crAmount);
            type = itemView.findViewById(R.id.type);
        }
    }
}
