package com.example.mercie.example.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.Salonist;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalonsRecyclerViewAdapter extends RecyclerView.Adapter<SalonsRecyclerViewAdapter.SalonViewHolder> {

    private List<Salonist> salons;
    private Context context;

    public SalonsRecyclerViewAdapter(Context context, List<Salonist> salons) {
        this.salons = salons;
        this.context = context;
    }

    @Override
    public SalonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.salon_fragment, viewGroup, false);

        return new SalonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalonViewHolder salonViewHolder, int i) {

        Salonist salonist = salons.get(i);
        salonViewHolder.salon = salonist;

        salonViewHolder.salonNameTV.setText(salonist.getName());
        salonViewHolder.salonLocationTV.setText(salonist.getLocation());

        // TODO: 3/24/19 Load An Image to the Salon CIV

    }

    @Override
    public int getItemCount() {
        return salons.size();
    }

    public class SalonViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public CircleImageView salonCIV;
        public TextView salonNameTV;
        public TextView salonLocationTV;
        public Salonist salon;

        public SalonViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            salonCIV = itemView.findViewById(R.id.salon_civ);
            salonNameTV = itemView.findViewById(R.id.salon_name_tv);
            salonLocationTV = itemView.findViewById(R.id.salon_location_tv);
        }
    }
}
