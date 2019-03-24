package com.example.mercie.example.fragments.salonist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mercie.example.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalonistEditProfileFragment extends Fragment {

    private EditText officialNameET, locationET, mobileNumberET, websiteET;
    private CircleImageView profileCIV;
    private Spinner dayFromSpinner, dayToSpinner, timeFromSpinner, timeToSpinner;

    public SalonistEditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salonist_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        officialNameET = view.findViewById(R.id.ss_ep_official_name_et);
        locationET = view.findViewById(R.id.ss_ep_location_et);
        mobileNumberET = view.findViewById(R.id.ss_ep_mobile_number_et);
        websiteET = view.findViewById(R.id.ss_ep_website_et);
        profileCIV = view.findViewById(R.id.ss_ep_profile_iv);
        dayFromSpinner = view.findViewById(R.id.ss_ep_spinner_day_from);
        dayToSpinner = view.findViewById(R.id.ss_ep_spinner_day_to);
        timeFromSpinner = view.findViewById(R.id.ss_ep_spinner_time_from);
        timeToSpinner = view.findViewById(R.id.ss_ep_spinner_time_to);
        Button changeProfileBtn = view.findViewById(R.id.ss_ep_change_profile_btn);
        Button ssEpBtn = view.findViewById(R.id.ss_ep_btn);

    }

    private void initSpinners() {
        String[] weekDays = getResources().getStringArray(R.array.week_days);
        String[] dayWorkHours = getResources().getStringArray(R.array.day_time);

        ArrayAdapter<String> weekDaysAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, weekDays);
        ArrayAdapter<String> dayWorkHoursAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dayWorkHours);

        dayFromSpinner.setAdapter(weekDaysAdapter);
        dayToSpinner.setAdapter(weekDaysAdapter);
        timeFromSpinner.setAdapter(dayWorkHoursAdapter);
        timeToSpinner.setAdapter(dayWorkHoursAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
