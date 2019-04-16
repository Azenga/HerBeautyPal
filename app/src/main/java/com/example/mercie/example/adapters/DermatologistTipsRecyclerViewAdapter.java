package com.example.mercie.example.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.Tip;

import java.util.List;

public class DermatologistTipsRecyclerViewAdapter extends RecyclerView.Adapter<DermatologistTipsRecyclerViewAdapter.ViewHolder> {
    private List<Tip> tipList;

    public DermatologistTipsRecyclerViewAdapter(List<Tip> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_single_tip, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tip tip = tipList.get(i);

        viewHolder.packageTV.setText(tip.getTipPackage());
        viewHolder.descTV.setText(tip.getDescription());
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView packageTV, descTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            packageTV = itemView.findViewById(R.id.package_name_tv);
            descTV = itemView.findViewById(R.id.tip_desc_tv);
        }
    }
}
