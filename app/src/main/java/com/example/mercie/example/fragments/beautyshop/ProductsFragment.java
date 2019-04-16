package com.example.mercie.example.fragments.beautyshop;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.ShopActivity;
import com.example.mercie.example.adapters.ShopProductsRVAdapter2;
import com.example.mercie.example.models.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private static final String TAG = "ProductsFragment";
    private String shopId = null;

    private FirebaseFirestore mDb;
    private List<Product> productList;
    private ShopProductsRVAdapter2 adapter2;


    public ProductsFragment() {
        mDb = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
    }

    public static ProductsFragment newInstance(String shopId) {

        Bundle args = new Bundle();
        args.putString(ShopActivity.SHOP_ID_PARAM, shopId);
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            shopId = getArguments().getString(ShopActivity.SHOP_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_products_fragment, container, false);

        RecyclerView productsRV = view.findViewById(R.id.products_rv);
        productsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsRV.setHasFixedSize(true);

        adapter2 = new ShopProductsRVAdapter2(getActivity(), productList);
        productsRV.setAdapter(adapter2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

            mDb.collection("products")
                    .document(shopId)
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
                                        adapter2.notifyDataSetChanged();
                                    }
                                }else{
                                    Toast.makeText(getActivity(), "No Products", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
    }
}