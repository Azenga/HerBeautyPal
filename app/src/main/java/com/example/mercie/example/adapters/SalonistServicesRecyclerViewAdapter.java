package com.example.mercie.example.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.SalonService;

import java.util.List;

public class SalonistServicesRecyclerViewAdapter extends RecyclerView.Adapter<SalonistServicesRecyclerViewAdapter.ViewHolder> {

    private List<SalonService> services;
    private Context context;

    public SalonistServicesRecyclerViewAdapter(List<SalonService> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salonist_service_single_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SalonService service = services.get(i);

        viewHolder.serviceNameTV.setText(service.getServiceName());
        viewHolder.serviceCostTV.setText(service.getServiceCost());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView serviceNameTV, serviceCostTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            serviceNameTV = itemView.findViewById(R.id.service_name_tv);
            serviceCostTV = itemView.findViewById(R.id.service_cost_tv);
        }
    }
}
