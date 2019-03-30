package com.example.mercie.example.fragments.beautyshop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeautyShopProductsFragment extends Fragment {

    private FloatingActionButton addProductFAB;

    private BeautyProducrsListener listener;


    public BeautyShopProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beauty_shop_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addProductFAB = view.findViewById(R.id.add_product_fab);
        addProductFAB.setOnClickListener(e -> listener.startAddProduct());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BeautyProducrsListener) {
            listener = (BeautyProducrsListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement BeautyProducrsListener Interface");
        }
    }

    public interface BeautyProducrsListener {
        void startAddProduct();
    }

}
