package com.example.mercie.example;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.mercie.example.models.Tip;

public class TipRemedyActivity extends AppCompatActivity {

    public static final String CURRENT_TIP_PARAM = "current-tip_param";

    private Tip mTip = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dandruff);

        if (getIntent() != null) mTip = (Tip) getIntent().getSerializableExtra(CURRENT_TIP_PARAM);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTip.getTipPackage());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        ViewPager viewPager = findViewById(R.id.container);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(RemmedyFragment.newInstance(mTip.getRemedy()), "Remedy");
        adapter.addFragment(DietFragment.newInstance(mTip.getDiet()), "Diet");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


}
