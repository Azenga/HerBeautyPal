package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.mercie.example.fragments.beautyshop.InfoFragment;
import com.example.mercie.example.fragments.beautyshop.OffersFragment;
import com.example.mercie.example.fragments.beautyshop.ProductsFragment;


public class ShopActivity extends AppCompatActivity {

    public static final String SHOP_ID_PARAM = "salon-id";
    private static final String TAG = "ShopActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar tool = findViewById(R.id.tool);

        String salonId = null;
        if (getIntent() != null) salonId = getIntent().getStringExtra(SHOP_ID_PARAM);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(this, HomeActivity.class)));

        ViewPager viewPager = findViewById(R.id.container);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(InfoFragment.newInstance(salonId), "Info");
        adapter.addFragment(ProductsFragment.newInstance(salonId), "Products");
        adapter.addFragment(new OffersFragment(), "Offers");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}