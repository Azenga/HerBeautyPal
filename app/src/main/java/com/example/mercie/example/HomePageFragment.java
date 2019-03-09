package com.example.mercie.example;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

    private Button allsalons;
    private ImageView anima;
    private Button beautyshops;
    private Button tips;
    private Button consultant;

    AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R. layout.home_page_fragment, container, false);

        anima = (ImageView) view.findViewById(R.id.anima);
        anima.setBackgroundResource(R.drawable.animation);
        animationDrawable = (AnimationDrawable) anima.getBackground();

        allsalons = (Button) view.findViewById(R.id.allsalons);

        allsalons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SalonActivity.class));


            }
        });


        beautyshops = (Button) view.findViewById(R.id.beautyshops);

        beautyshops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopActivity.class));


            }
        });


        tips = (Button) view.findViewById(R.id.tips);

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TipsActivity.class));


            }
        });

        consultant = (Button) view.findViewById(R.id.consultant);
        consultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        animationDrawable.start();

        return view;

    }


}
