package com.example.mercie.example.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class ShopProductsRVAdapter extends RecyclerView.Adapter<ShopProductsRVAdapter.ViewHolder> {

    private static final String TAG = "ShopProductsRVAdapter";

    private Context context;
    private List<Product> productList;
    private StorageReference mRef;


    public ShopProductsRVAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

        mRef = FirebaseStorage.getInstance().getReference().child("product_images");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_single_product_file, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Product product = productList.get(i);
        viewHolder.nameTV.setText(product.getName());
        viewHolder.costTV.setText("KES " + String.valueOf(product.getCost()));

        //Getting and setting Image
        if (product.getImageName() != null) {
            StorageReference productImageRef = mRef.child(product.getImageName());

            final long MB = 1024 * 1024;
            productImageRef.getBytes(MB)
                    .addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        viewHolder.productIV.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(
                            e -> {
                                Toast.makeText(context, "Loading product Image Failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onBindViewHolder: Loading product Image Failed", e);
                            }
                    );
        } else {
            Toast.makeText(context, "Product Image Missing", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView productIV;
        TextView nameTV, costTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            productIV = itemView.findViewById(R.id.product_iv);
            nameTV = itemView.findViewById(R.id.name_tv);
            costTV = itemView.findViewById(R.id.cost_tv);
        }
    }
}
