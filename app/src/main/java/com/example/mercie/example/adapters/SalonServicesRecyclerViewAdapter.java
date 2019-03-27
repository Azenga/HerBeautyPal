package com.example.mercie.example.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.SalonService;

import java.util.List;

public class SalonServicesRecyclerViewAdapter extends RecyclerView.Adapter<SalonServicesRecyclerViewAdapter.ViewHolder> {

    private List<SalonService> services;

    public SalonServicesRecyclerViewAdapter(List<SalonService> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salon_service_single_item, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SalonService service = services.get(i);

        viewHolder.packageNameTV.setText(service.getPackageName());
        viewHolder.serviceNameTV.setText(service.getServiceName());
        viewHolder.serviceCostTV.setText(service.getServiceCost());

        // TODO: 3/25/19 Set onclick listener for every service
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView packageNameTV, serviceNameTV, serviceCostTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            packageNameTV = itemView.findViewById(R.id.which_package_tv);
            packageNameTV = itemView.findViewById(R.id.service_name_tv);
            packageNameTV = itemView.findViewById(R.id.service_cost_tv);
        }
    }
}
