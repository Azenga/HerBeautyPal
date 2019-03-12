package com.example.mercie.example;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Mercie on 2/27/2019.
 */

public class HomePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView anima = view.findViewById(R.id.anima);
        anima.setBackgroundResource(R.drawable.animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) anima.getBackground();

        Button allsalons = view.findViewById(R.id.allsalons);
        allsalons.setOnClickListener(v -> startActivity(new Intent(getActivity(), SalonActivity.class)));


        Button beautyshops = view.findViewById(R.id.beautyshops);
        beautyshops.setOnClickListener(v -> startActivity(new Intent(getActivity(), ShopActivity.class)));


        Button tips = view.findViewById(R.id.tips);

        tips.setOnClickListener(v -> startActivity(new Intent(getActivity(), TipsActivity.class)));

        Button consultant = view.findViewById(R.id.consultant);
        consultant.setOnClickListener(v -> {
        });

        animationDrawable.start();

    }
}
