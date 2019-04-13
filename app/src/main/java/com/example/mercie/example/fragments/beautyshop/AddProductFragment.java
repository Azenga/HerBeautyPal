package com.example.mercie.example.fragments.beautyshop;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mercie.example.BeautyShopDashboardActivity;
import com.example.mercie.example.R;
import com.example.mercie.example.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.mercie.example.fragments.salonist.salon.ServicesFragment.TAG;

public class AddProductFragment extends Fragment {

    public static final int RC_CHOOSE_IMAGE = 22;
    //Widgets
    private Spinner packageSpinner;
    private TextInputEditText productNameTIET, productCostTIET;
    private ImageView productImage;
    private Button addProductBtn;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    private Uri imageUri = null;

    public AddProductFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("product_images");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        packageSpinner = view.findViewById(R.id.package_spinner);
        ArrayAdapter<CharSequence> packageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.beauty_packages, R.layout.simple_spinner_item);
        packageAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        packageSpinner.setAdapter(packageAdapter);

        //TIET
        productNameTIET = view.findViewById(R.id.product_name_tiet);
        productCostTIET = view.findViewById(R.id.product_cost_tiet);

        //ImageView
        productImage = view.findViewById(R.id.product_iv);

        //Button
        addProductBtn = view.findViewById(R.id.add_product_btn);

        //ProgressDialog
        progressDialog = new ProgressDialog(getActivity());

        return view;
    }

    private boolean emptyFields() {

        if (productNameTIET.getText().toString().isEmpty()) {
            productNameTIET.setError("Product Name Is Required");
            productNameTIET.requestFocus();
            return true;

        } else if (productCostTIET.getText().toString().isEmpty()) {
            productCostTIET.setError("Product Cost Is Required");
            productCostTIET.requestFocus();
            return true;
        } else {
            return false;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void addProduct(String productImageName) {

        String packageName = packageSpinner.getSelectedItem().toString();
        String name = productNameTIET.getText().toString().trim();
        double cost = Double.parseDouble(productCostTIET.getText().toString().trim());

        if (!emptyFields()) {
            Product product = new Product(packageName, name, cost, productImageName);


            if (mAuth.getCurrentUser() != null) {
                mDb.collection("products")
                        .document(mAuth.getCurrentUser().getUid())
                        .collection("Products")
                        .add(product)
                        .addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Product Added", Toast.LENGTH_SHORT).show();
                                        ((BeautyShopDashboardActivity) getActivity()).replaceFragment(new BeautyShopProductsFragment());
                                    } else {

                                        Toast.makeText(getActivity(), "Operation Failed", Toast.LENGTH_SHORT).show();
                                        Log.w(TAG, "addProduct: Failed", task.getException());

                                    }

                                    progressDialog.dismiss();
                                }
                        );
            }


        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RC_CHOOSE_IMAGE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        productImage.setOnClickListener(v -> chooseImage());
        addProductBtn.setOnClickListener(v -> uploadPicture());
    }

    private void uploadPicture() {

        progressDialog.setTitle("Adding Product");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        if (imageUri != null) {
            String imageName = String.valueOf(System.currentTimeMillis()) + '.' + getFileExtension(imageUri);

            StorageReference productImageRef = mRef.child(imageName);

            UploadTask productImageUploadTask = productImageRef.putFile(imageUri);
            productImageUploadTask
                    .addOnSuccessListener(taskSnapshot -> {
                        addProduct(taskSnapshot.getMetadata().getName());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "addProduct: Uploading Image Failed", e);
                        progressDialog.dismiss();
                    });
        } else {
            addProduct(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK && data != null) {
            switch (requestCode) {
                case RC_CHOOSE_IMAGE:
                    imageUri = data.getData();
                    productImage.setImageURI(imageUri);
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
