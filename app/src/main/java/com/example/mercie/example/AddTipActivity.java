package com.example.mercie.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mercie.example.models.Tip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.grpc.Context;

public class AddTipActivity extends AppCompatActivity {
    private static final String TAG = "AddTipActivity";

    //REQUEST CODES
    public static final int RC_CHOOSE_IMAGE = 22;
    public static final int RC_CROP_IMAGE = 10;
    //Widgets
    private ImageView tipIV;
    private Spinner packageSpinner;
    private Button changeImgBtn, addTipBtn;
    private EditText tipDescET, tipRemedyET, tipDietET;
    private ProgressDialog dialog;

    private boolean imageSelected = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        initComponents();
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("tips_images");


        changeImgBtn.setOnClickListener(view -> openChooseImageActivity());
        addTipBtn.setOnClickListener(view -> uploadImage());
    }

    private void uploadImage() {
        dialog.setTitle("Adding Tip");
        dialog.setMessage("PLease Wait");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        if (imageSelected) {
            Bitmap bitmap = ((BitmapDrawable) tipIV.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] bytes = baos.toByteArray();

            StorageReference imageRef = mRef.child(System.currentTimeMillis() + ".jpg");

            imageRef.putBytes(bytes)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                String imageName = taskSnapshot.getMetadata().getName();
                                addTip(imageName);
                            }
                    )
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Log.e(TAG, "uploadImage: Failed", e);
                    });
        } else {
            addTip(null);
        }
    }

    private void addTip(String imageName) {
        String packageName = packageSpinner.getSelectedItem().toString().trim();
        String desc = tipDescET.getText().toString().trim();
        String remedy = tipRemedyET.getText().toString().trim();
        String diet = tipDietET.getText().toString().trim();

        Tip tip = new Tip(packageName, desc, remedy, diet, imageName);


        mDb.collection("tips")
                .add(tip)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Tip Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "addTip: Failed", task.getException());
                                Toast.makeText(this, "Operation Failed", Toast.LENGTH_SHORT).show();

                            }

                            dialog.dismiss();
                        }
                );
    }

    private void initComponents() {

        tipIV = findViewById(R.id.tip_iv);
        packageSpinner = findViewById(R.id.package_spinner);
        changeImgBtn = findViewById(R.id.change_img_btn);
        addTipBtn = findViewById(R.id.add_tip_btn);
        tipDescET = findViewById(R.id.tip_desc_et);
        tipRemedyET = findViewById(R.id.tip_remedy_et);
        tipDietET = findViewById(R.id.tip_diet_et);

        dialog = new ProgressDialog(this);

    }

    private void openChooseImageActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RC_CHOOSE_IMAGE);
    }

    private void openCroppingActivity(Uri uri) {
        imageSelected = false;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", true);
        intent.putExtra("outputX", 512);
        intent.putExtra("outputY", 512);
        intent.putExtra("aspectX", 512);
        intent.putExtra("aspectY", 512);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        PackageManager packageManager = getPackageManager();

        List<ResolveInfo> croppingApps = packageManager.queryIntentActivities(intent, 0);

        if (croppingApps.size() > 0) startActivityForResult(intent, RC_CROP_IMAGE);
        else Toast.makeText(this, "Please, Install a cropping App", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case RC_CHOOSE_IMAGE:
                    imageSelected = true;
                    tipIV.setImageURI(data.getData());

                    Snackbar.make(findViewById(android.R.id.content), "Profile Picture Image", Snackbar.LENGTH_LONG)
                            .setAction("Crop the Image", view -> openCroppingActivity(data.getData()))
                            .show();

                    break;
                case RC_CROP_IMAGE:
                    imageSelected = true;
                    populateProfilePicture(data);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Toast.makeText(this, "Process Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateProfilePicture(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = bundle.getParcelable("data");
        if (bitmap != null)
            tipIV.setImageBitmap(bitmap);
    }
}
