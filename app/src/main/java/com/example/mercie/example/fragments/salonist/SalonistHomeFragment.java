package com.example.mercie.example.fragments.salonist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.UpdateSalonActivity;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SalonistHomeFragment extends Fragment {

    private static final String SALONIST_PARAM = "salon";

    private Salonist mSalonist = null;

    private ProfileFragListener mListener;

    //Fragment widgets
    private TextView nameTV, locationTV, mobileTV, websiteTV, emailAddressTV;
    private ImageView profilePicIV;

    //FirebaseStorage
    private StorageReference mRef;
    private FirebaseAuth mAuth;

    public SalonistHomeFragment() {
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
        mAuth = FirebaseAuth.getInstance();
    }

    public static SalonistHomeFragment newInstance(Salonist salonist) {

        SalonistHomeFragment fragment = new SalonistHomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(SALONIST_PARAM, salonist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSalonist = (Salonist) getArguments().getSerializable(SALONIST_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salonist_home, container, false);
    }

    //For initializing the widgets
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Display TextViews
        nameTV = view.findViewById(R.id.name_et);
        locationTV = view.findViewById(R.id.location_tv);
        mobileTV = view.findViewById(R.id.contact_tv);
        websiteTV = view.findViewById(R.id.website_tv);
        emailAddressTV = view.findViewById(R.id.email_tv);

        //ImageView
        profilePicIV = view.findViewById(R.id.avatar_iv);

        //Button
        Button editProfile = view.findViewById(R.id.edit_profile_btn);
        editProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), UpdateSalonActivity.class)));

        updateUI();

    }

    private void updateUI() {
        if (mSalonist != null) {
            if (mSalonist.getName() != null) nameTV.setText(mSalonist.getName());
            if (mSalonist.getLocation() != null) locationTV.setText(mSalonist.getLocation());
            if (mSalonist.getContact() != null) mobileTV.setText(mSalonist.getContact());
            if (mSalonist.getWebsite() != null) websiteTV.setText(mSalonist.getWebsite());
            emailAddressTV.setText(mAuth.getCurrentUser().getEmail());

            if (mSalonist.getProfilePicName() != null) {

                final long MB = 1024 * 1024;

                mRef.child(mSalonist.getProfilePicName())
                        .getBytes(MB)
                        .addOnSuccessListener(
                                bytes -> {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    profilePicIV.setImageBitmap(bitmap);
                                }
                        )
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error loading image: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ProfileFragListener) {
            mListener = (ProfileFragListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement ProfileFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ProfileFragListener {
        void openEditFragment(Salonist salonist);
    }
}
