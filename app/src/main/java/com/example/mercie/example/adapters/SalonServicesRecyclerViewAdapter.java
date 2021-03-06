package com.example.mercie.example.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.SalonServicesFragment;
import com.example.mercie.example.models.SalonService;

import java.util.List;

public class SalonServicesRecyclerViewAdapter extends RecyclerView.Adapter<SalonServicesRecyclerViewAdapter.ViewHolder> {

    private List<SalonService> services;
    private Context context;

    private SalonServicesFragment.SalonServiceFragmentListener mListener;


    public SalonServicesRecyclerViewAdapter(List<SalonService> services, SalonServicesFragment.SalonServiceFragmentListener listener) {
        this.services = services;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.salon_service_single_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SalonService service = services.get(i);

        viewHolder.serviceNameTV.setText(service.getServiceName());
        viewHolder.serviceCostTV.setText(service.getServiceCost());
        viewHolder.requestReservationIB.setOnClickListener(view -> {
            mListener.RequestSalonService(service);
        });


    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView serviceNameTV, serviceCostTV;
        ImageButton requestReservationIB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            serviceNameTV = itemView.findViewById(R.id.service_name_tv);
            serviceCostTV = itemView.findViewById(R.id.service_cost_tv);
            requestReservationIB = itemView.findViewById(R.id.request_reservation_ib);

        }
    }
}
