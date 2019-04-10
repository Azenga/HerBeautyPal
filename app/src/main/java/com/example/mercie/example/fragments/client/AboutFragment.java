package com.example.mercie.example.fragments.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.HomeActivity;
import com.example.mercie.example.R;
import com.example.mercie.example.models.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Mercie on 2/27/2019.
 */


public class AboutFragment extends Fragment {

    private TextView nameTV, contactTV, locationTV, emailTV;
    private ImageView profilePicIV;
    private ImageButton editProfileIB;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    public AboutFragment() {
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);


        editProfileIB = view.findViewById(R.id.edit_profile_ib);
        editProfileIB.setOnClickListener(v -> ((HomeActivity) getActivity()).displayFrag(R.id.nav_edit_profile));

        nameTV = view.findViewById(R.id.name_tv);
        contactTV = view.findViewById(R.id.name_tv);
        locationTV = view.findViewById(R.id.location_tv);
        emailTV = view.findViewById(R.id.email_tv);

        profilePicIV = view.findViewById(R.id.profile_pic_iv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb.collection("clients")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Client client = task.getResult().toObject(Client.class);

                                nameTV.setText(client.getName());
                                contactTV.setText(client.getContact());
                                locationTV.setText(client.getAddress());
                                emailTV.setText(mAuth.getCurrentUser().getEmail());

                                if (client.getImageUrl() != null) {
                                    StorageReference profilePicRef = mRef.child(client.getImageUrl());
                                    final long MB = 1024 * 1024;
                                    profilePicRef.getBytes(MB)
                                            .addOnSuccessListener(
                                                    bytes -> {

                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        profilePicIV.setImageBitmap(bitmap);
                                                    }
                                            )
                                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                                }

                            } else {
                                Toast.makeText(getActivity(), "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }
}

