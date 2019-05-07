package com.example.mercie.example.fragments.dermatologist;


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
import com.example.mercie.example.UpdateDermatologistActivity;
import com.example.mercie.example.models.Dermatologist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Profile extends Fragment {


    //Widgets
    private ImageView profilePivIV;
    private TextView nameTV, contactTV, locationTV, emailAddressTV, websiteTV;
    private Button editProfileBtn;

    //Firebase Variables
    private StorageReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;


    public Profile() {
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dermatologist_fragment_profile, container, false);


        profilePivIV = view.findViewById(R.id.profile_pic_civ);

        nameTV = view.findViewById(R.id.name_et);
        contactTV = view.findViewById(R.id.contact_tv);
        locationTV = view.findViewById(R.id.location_tv);
        emailAddressTV = view.findViewById(R.id.email_tv);
        websiteTV = view.findViewById(R.id.website_tv);

        editProfileBtn = view.findViewById(R.id.edit_profile_btn);

        editProfileBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), UpdateDermatologistActivity.class)));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDb.collection("dermatologists").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(

                        task -> {

                            if (task.isSuccessful()) {

                                DocumentSnapshot shopDoc = task.getResult();

                                if (shopDoc.exists()) {
                                    Dermatologist dermatologist = shopDoc.toObject(Dermatologist.class);
                                    updateUI(dermatologist);
                                } else {
                                    Toast.makeText(getActivity(), "You should update your profile", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(getActivity(), "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                );

    }

    private void updateUI(Dermatologist dermatologist) {
        nameTV.setText(dermatologist.getOfficialName());
        contactTV.setText(dermatologist.getMobile());
        locationTV.setText(dermatologist.getLocation());
        if (mAuth.getCurrentUser() != null)
            emailAddressTV.setText(mAuth.getCurrentUser().getEmail());
        websiteTV.setText(dermatologist.getWebsite());

        //Profile Image
        if (dermatologist.getProfilePicName() != null) {
            StorageReference profilePicRef = mRef.child(dermatologist.getProfilePicName());
            final long MB = 1024 * 1024;
            profilePicRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profilePivIV.setImageBitmap(bitmap);

                            }
                    )
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
