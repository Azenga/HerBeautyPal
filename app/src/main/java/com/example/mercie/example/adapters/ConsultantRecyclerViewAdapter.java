package com.example.mercie.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.ConsultantActivity;
import com.example.mercie.example.R;
import com.example.mercie.example.models.Dermatologist;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultantRecyclerViewAdapter extends RecyclerView.Adapter<ConsultantRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ConsultantRecyclerViewA";

    private Context context;
    private List<Dermatologist> consultants;


    private StorageReference mRef;

    public ConsultantRecyclerViewAdapter(Context context, List<Dermatologist> consultants) {
        this.context = context;
        this.consultants = consultants;

        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_consultant_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Dermatologist consultant = consultants.get(i);

        viewHolder.nameTV.setText(consultant.getOfficialName());
        viewHolder.locationTV.setText(consultant.getLocation());


        //Check For Image

        if (consultant.getProfilePicName() != null) {
            StorageReference profilePicRef = mRef.child(consultant.getProfilePicName());
            final long MB = 1024 * 1024;
            profilePicRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                viewHolder.coverCIV.setImageBitmap(bitmap);
                            }
                    )
                    .addOnFailureListener(
                            e -> {
                                Toast.makeText(context, "Failed to get the Image", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onBindViewHolder: Getting Image", e);
                            }
                    );
        }

        viewHolder.mView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ConsultantActivity.class);
            intent.putExtra(ConsultantActivity.DERMATOLOGIST_ID, consultant.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return consultants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CircleImageView coverCIV;
        TextView nameTV, locationTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            coverCIV = itemView.findViewById(R.id.shop_cover_civ);
            nameTV = itemView.findViewById(R.id.name_et);
            locationTV = itemView.findViewById(R.id.location_tv);
        }
    }
}
