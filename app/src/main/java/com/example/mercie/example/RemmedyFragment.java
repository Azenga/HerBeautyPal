package com.example.mercie.example;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RemmedyFragment extends Fragment {

    public static final String TIP_REMMEDY_PARAM = "tip-remedy-param";

    private String mRemedy = null;

    public RemmedyFragment() {
    }

    public static RemmedyFragment newInstance(String remedy) {

        Bundle args = new Bundle();
        args.putString(TIP_REMMEDY_PARAM, remedy);
        RemmedyFragment fragment = new RemmedyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) mRemedy = getArguments().getString(TIP_REMMEDY_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_remmedy_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView remedyTV = view.findViewById(R.id.remedy_tv);
        remedyTV.setText(mRemedy);
    }
}
