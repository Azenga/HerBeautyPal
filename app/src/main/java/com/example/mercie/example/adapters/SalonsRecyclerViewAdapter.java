package com.example.mercie.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.SalonActivity;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalonsRecyclerViewAdapter extends RecyclerView.Adapter<SalonsRecyclerViewAdapter.SalonViewHolder> {

    private List<Salonist> salons;
    private Context context;

    private StorageReference mRef;

    public SalonsRecyclerViewAdapter(Context context, List<Salonist> salons) {
        this.salons = salons;
        this.context = context;

        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
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

        StorageReference salonPicRef = mRef.child(salonist.getProfilePicName());

        final  long MB = 1024 * 1024;

        salonPicRef.getBytes(MB)
                .addOnSuccessListener(
                        bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            salonViewHolder.salonCIV.setImageBitmap(bitmap);
                        }
                ).addOnFailureListener(e -> Toast.makeText(context, "Error getting a salon image: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());

        //Onclick listener for each salon item
        salonViewHolder.mView.setOnClickListener(
                view -> {
                    Intent intent = new Intent(context, SalonActivity.class);
                    intent.putExtra("salon", salonist);

                    context.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return salons.size();
    }

    public class SalonViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CircleImageView salonCIV;
        TextView salonNameTV;
        TextView salonLocationTV;
        Salonist salon;

        public SalonViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            salonCIV = itemView.findViewById(R.id.salon_civ);
            salonNameTV = itemView.findViewById(R.id.salon_name_tv);
            salonLocationTV = itemView.findViewById(R.id.salon_location_tv);
        }
    }
}
