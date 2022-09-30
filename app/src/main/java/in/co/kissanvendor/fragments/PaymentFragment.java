package in.co.kissanvendor.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.co.kissanvendor.R;
import in.co.kissanvendor.adapters.PaymentDetailsAdapter;
import in.co.kissanvendor.models.PaymentDetails;


public class PaymentFragment extends Fragment {

    RecyclerView payment_recycler;
    LinearLayoutManager linearLayoutManager;
    PaymentDetailsAdapter paymentDetailsAdapter;
    ArrayList<PaymentDetails> paymentDetails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        payment_recycler = v.findViewById(R.id.payment_recycler);

        paymentDetails.add(new PaymentDetails("123456789654","Apple","Delivered","Rs 250","Refundable","12 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Returned","Rs 630","Refundable","13 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Delivered","Rs 500","Refundable","14 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Returned","Rs 810","Refundable","15 sep 22"));

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        paymentDetailsAdapter = new PaymentDetailsAdapter(paymentDetails,getActivity());
        payment_recycler.setLayoutManager(linearLayoutManager);
        payment_recycler.setHasFixedSize(true);
        payment_recycler.setAdapter(paymentDetailsAdapter);

        return v;
    }
}