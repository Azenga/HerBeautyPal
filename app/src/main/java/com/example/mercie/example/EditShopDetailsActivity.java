package com.example.mercie.example;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditShopDetailsActivity extends AppCompatActivity {

    public static final int RC_CHOOSE_IMAGE = 22;
    private ImageView shopCoverIV;
    private static final String TAG = "EditShopDetailsActivity";
    private EditText nameET, locationET;
    private Button updateBtn;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_details);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("shop_cover_images");

        initWidgets();

        shopCoverIV.setOnClickListener(view -> openChooseImageActivity());

        updateBtn.setOnClickListener(view -> uploadShop());
    }

    private void uploadShop() {
        if (imageUri != null) {
            StorageReference coverImageRef = mRef.child(System.currentTimeMillis() + '.' + getExtension(imageUri));

            coverImageRef.putFile(imageUri)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                String coverImageName = taskSnapshot.getMetadata().getName();

                                addToFirestore(coverImageName);
                            }
                    )
                    .addOnFailureListener(e -> Log.e(TAG, "uploadShop: ", e));
        } else {

            addToFirestore(null);

        }
    }

    private void addToFirestore(String coverImageName) {

        Map<String, Object> beautyShopMap = new HashMap<>();
        beautyShopMap.put("name", nameET.getText().toString().trim());
        beautyShopMap.put("location", locationET.getText().toString().trim());
        beautyShopMap.put("imageName", coverImageName);

        if (mAuth.getCurrentUser() != null) {
            mDb.collection("beauty_shops")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(beautyShopMap)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Beauty Shop Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, BeautyShopDashboardActivity.class));
                                } else {
                                    Log.w(TAG, "addToFirestore: ", task.getException());
                                }
                            }
                    );
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void openChooseImageActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RC_CHOOSE_IMAGE);
    }

    private void initWidgets() {
        shopCoverIV = findViewById(R.id.shop_cover_iv);

        nameET = findViewById(R.id.name_et);
        locationET = findViewById(R.id.location_et);

        updateBtn = findViewById(R.id.update_btn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data.getData() != null) {
            switch (requestCode) {
                case RC_CHOOSE_IMAGE:
                    imageUri = data.getData();
                    shopCoverIV.setImageURI(imageUri);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
