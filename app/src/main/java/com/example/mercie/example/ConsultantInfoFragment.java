package com.example.mercie.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.models.Dermatologist;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultantInfoFragment extends Fragment {
    private static final String TAG = "ConsultantInfoFragment";

    private String consultantUid = null;

    //Widgets
    private CircleImageView profileCIV;
    private TextView followersTV, followingTV, tipsCountTV, nameTV, contactTV, locationTV, websiteTV;
    private Button followBtn, messageBtn;

    private FirebaseFirestore mDb;
    private StorageReference mRef;

    public ConsultantInfoFragment() {
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
    }

    public static ConsultantInfoFragment newInstance(String uid) {

        Bundle args = new Bundle();
        args.putString(ConsultantActivity.DERMATOLOGIST_ID, uid);

        ConsultantInfoFragment fragment = new ConsultantInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            consultantUid = getArguments().getString(ConsultantActivity.DERMATOLOGIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultant_info, container, false);

        profileCIV = view.findViewById(R.id.profile_civ);

        followersTV = view.findViewById(R.id.followers_tv);
        followingTV = view.findViewById(R.id.following_tv);
        tipsCountTV = view.findViewById(R.id.tips_count_tv);

        followBtn = view.findViewById(R.id.follow_btn);
        messageBtn = view.findViewById(R.id.message_btn);

        nameTV = view.findViewById(R.id.name_tv);
        contactTV = view.findViewById(R.id.contact_tv);
        locationTV = view.findViewById(R.id.location_tv);
        websiteTV = view.findViewById(R.id.website_tv);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb.document("dermatologists/" + consultantUid)
                .addSnapshotListener(
                        (documentSnapshot, e) -> {
                            if (e != null) {
                                Toast.makeText(getActivity(), "Error getting document", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onViewCreated: Getting Consultant DEtails", e);
                                return;
                            }

                            if (documentSnapshot.exists()) {

                                Dermatologist dermatologist = documentSnapshot.toObject(Dermatologist.class);

                                //Populate profile Image

                                if (dermatologist.getProfilePicName() != null)
                                    populateImage(dermatologist.getProfilePicName());
                                nameTV.setText(dermatologist.getOfficialName());
                                contactTV.setText(dermatologist.getMobile());
                                locationTV.setText(dermatologist.getLocation());
                                websiteTV.setText(dermatologist.getWebsite());

                            } else {
                                Toast.makeText(getActivity(), "No Such Consultant", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    private void populateImage(String profilePicName) {
        StorageReference profileRef = mRef.child(profilePicName);

        final long MB = 1024 * 1024;

        profileRef.getBytes(MB)
                .addOnSuccessListener(
                        bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            profileCIV.setImageBitmap(bitmap);
                        }
                )
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed getting the image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "populateImage: Getting Consultant Image", e);
                });
    }
}
