package com.example.mercie.example.fragments.beautyshop;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mercie.example.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {

    private Spinner packageSpinner;


    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        packageSpinner = view.findViewById(R.id.package_spinner);
        ArrayAdapter<CharSequence> packageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.beauty_packages, R.layout.simple_spinner_item);
        packageAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        packageSpinner.setAdapter(packageAdapter);

    }
}
