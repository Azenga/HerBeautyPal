package com.example.mercie.example;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mercie.example.models.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Mercie on 2/27/2019.
 */

public class ChangeProfileFragment extends Fragment {

    private ImageView avatarIV;
    private EditText nameET, phoneET, addressET, genderET;

    private static final int IMAGE_REQUEST_CODE = 999;

    private Uri imageUri = null;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private StorageReference mStore;
    private FirebaseFirestore mdb;

    public ChangeProfileFragment() {
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseStorage.getInstance().getReference().child("avatars");
        mdb = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        avatarIV = view.findViewById(R.id.avatar_iv);
        avatarIV.setOnClickListener(
                v -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            openChooseImageIntent();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST_CODE);
                        }

                    } else {
                        openChooseImageIntent();
                    }

                }
        );

        nameET = view.findViewById(R.id.name_et);
        phoneET = view.findViewById(R.id.phone_et);
        addressET = view.findViewById(R.id.address_et);
        genderET = view.findViewById(R.id.gender_et);

        Button editBtn = view.findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(v -> editProfile());
    }

    private void openChooseImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }


    public String getFileExtension(Uri uri) {
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void editProfile() {
        String name = nameET.getText().toString().trim();
        String phone = phoneET.getText().toString().trim();
        String address = addressET.getText().toString().trim();
        String gender = genderET.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(gender)) {

            if (imageUri == null) {
                Client client = new Client(name, phone, address, gender, null);
                uploadClientDetailsToFirebase(client);

            } else {

                StorageReference fileRef = mStore.child(mAuth.getCurrentUser().getUid() + '.' + getFileExtension(imageUri));
                UploadTask uploadImageTask = fileRef.putFile(imageUri);

                uploadImageTask.addOnSuccessListener(
                        taskSnapshot -> {
                            String imageUrl = taskSnapshot.getMetadata().getName();

                            Client client = new Client(name, phone, address, gender, imageUrl);
                            uploadClientDetailsToFirebase(client);
                        }
                ).addOnFailureListener(e -> Toast.makeText(getActivity(), "An error occurred: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());

            }

        } else {
            Toast.makeText(getActivity(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadClientDetailsToFirebase(Client client) {

        //impactversion
        //impactfasion

        mdb.collection("clients")
                .document(mAuth.getCurrentUser().getUid())
                .set(client)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {

                                Toast.makeText(getActivity(), "Your profile has been edited", Toast.LENGTH_SHORT).show();

                                ((HomeActivity) getActivity()).displayFrag(R.id.nav_checkDetails);

                            } else {
                                Toast.makeText(getActivity(), "An error occurred: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_REQUEST_CODE) {

            if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                avatarIV.setImageURI(imageUri);
            }
        }
    }
}
