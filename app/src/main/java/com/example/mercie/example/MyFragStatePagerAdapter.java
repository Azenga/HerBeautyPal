package com.example.mercie.example;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mercie on 2/27/2019.
 */

public class MyFragStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleslist = new ArrayList<>();

    public MyFragStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment frag, String title) {
        fragmentList.add(frag);
        titleslist.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
