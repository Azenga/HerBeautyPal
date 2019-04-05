package com.example.mercie.example.fragments.salonist.salon;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.R;

public class ServicesFragment extends Fragment {

    private static final String SALON_ID_PARAM = "salon-id";

    private String salonId = null;

    //Widgets
    private RecyclerView servicesRV;
    private FloatingActionButton addServiceBtn;

    private OnServiceFragInteraction mListener;

    public ServicesFragment() {
    }

    public static ServicesFragment newInstance(String id) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putString(SALON_ID_PARAM, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) salonId = getArguments().getString(SALON_ID_PARAM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.salon_fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        servicesRV = view.findViewById(R.id.services_rv);

        addServiceBtn = view.findViewById(R.id.add_service_fab);
        addServiceBtn.setOnClickListener(v -> {
            if(mListener != null) {
                Toast.makeText(getActivity(), "mListener is not null", Toast.LENGTH_SHORT).show();
                mListener.loadAddFragmentService();
            }else {
                Toast.makeText(getActivity(), "mListener is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnServiceFragInteraction)
            mListener = (OnServiceFragInteraction) context;
    }

    public interface OnServiceFragInteraction {
        void loadAddFragmentService();
    }
}
