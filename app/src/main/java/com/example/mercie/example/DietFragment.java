package com.example.mercie.example;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DietFragment extends Fragment {

    public static final String DIET_TEXT_PARAM = "diet-text-param";

    private String mDiet = null;

    public DietFragment() {
    }

    public static DietFragment newInstance(String diet) {

        Bundle args = new Bundle();
        args.putString(DIET_TEXT_PARAM, diet);
        DietFragment fragment = new DietFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDiet = getArguments().getString(DIET_TEXT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_diet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView dietTV = view.findViewById(R.id.diet_tv);
        dietTV.setText(mDiet);
    }
}
