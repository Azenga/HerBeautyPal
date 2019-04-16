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

import com.example.mercie.example.R;
import com.example.mercie.example.ShopActivity;
import com.example.mercie.example.models.PShop;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientShopsRVAdapter extends RecyclerView.Adapter<ClientShopsRVAdapter.ViewHolder> {

    private static final String TAG = "ClientShopsRVAdapter";

    private List<PShop> shopList;
    private Context context;

    private StorageReference mRef;

    public ClientShopsRVAdapter(List<PShop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;

        mRef = FirebaseStorage.getInstance().getReference().child("shop_cover_images");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_single_shop_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        PShop shop = shopList.get(position);

        viewHolder.nameTV.setText(shop.getName());
        viewHolder.locationTV.setText(shop.getLocation());

        //Getting and Setting the Image
        if (shop.getCoverName() != null) {
            StorageReference shopCoverRef = mRef.child(shop.getCoverName());
            final long MB = 1024 * 1024;
            shopCoverRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                viewHolder.coverImageCIV.setImageBitmap(bitmap);
                            }
                    )
                    .addOnFailureListener(e -> Log.e(TAG, "onBindViewHolder: Getting Image Failed", e));
        }

        viewHolder.mView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShopActivity.class);
            intent.putExtra(ShopActivity.SHOP_ID_PARAM, shop.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CircleImageView coverImageCIV;
        TextView nameTV, locationTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            coverImageCIV = itemView.findViewById(R.id.shop_cover_civ);
            nameTV = itemView.findViewById(R.id.name_et);
            locationTV = itemView.findViewById(R.id.location_tv);
        }
    }
}
