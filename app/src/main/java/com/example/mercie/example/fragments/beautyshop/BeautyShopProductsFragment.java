package com.example.mercie.example.fragments.beautyshop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.R;
import com.example.mercie.example.adapters.ShopProductsRVAdapter;
import com.example.mercie.example.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BeautyShopProductsFragment extends Fragment {

    private static final String TAG = "BeautyShopProductsFragm";

    private FloatingActionButton addProductFAB;
    private BeautyProductsListener listener;

    private ShopProductsRVAdapter adapter;
    private List<Product> productList;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;


    public BeautyShopProductsFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        productList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beauty_shop_products, container, false);
        addProductFAB = view.findViewById(R.id.add_product_fab);
        addProductFAB.setOnClickListener(v -> listener.startAddProduct());

        RecyclerView productsRV = view.findViewById(R.id.products_rv);
        productsRV.setHasFixedSize(true);
        adapter = new ShopProductsRVAdapter(getActivity(), productList);
        productsRV.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (mAuth.getCurrentUser() != null) {
            mDb.collection("products")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("Products")
                    .addSnapshotListener(
                            (queryDocumentSnapshots, e) -> {

                                if (e != null) {
                                    Log.e(TAG, "onViewCreated: Getting Products", e);
                                    return;
                                }

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                        Product product = doc.toObject(Product.class);
                                        product.setId(doc.getId());

                                        productList.add(product);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                            }
                    );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BeautyProductsListener) {
            listener = (BeautyProductsListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement BeautyProductsListener Interface");
        }
    }

    public interface BeautyProductsListener {
        void startAddProduct();
    }

}
