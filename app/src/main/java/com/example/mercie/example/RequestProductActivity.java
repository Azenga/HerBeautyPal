package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RequestProductActivity extends AppCompatActivity {

    public static final String PRODUCT_PARAM = "product-to-be-bought";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_product);
    }
}
