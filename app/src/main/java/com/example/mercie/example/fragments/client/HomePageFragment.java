package com.example.mercie.example.fragments.client;

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

import com.example.mercie.example.ConsultantsActivity;
import com.example.mercie.example.R;
import com.example.mercie.example.SalonsActivity;
import com.example.mercie.example.ShopActivity;
import com.example.mercie.example.ShopsActivity;
import com.example.mercie.example.TipsActivity;

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

        ImageView anim = view.findViewById(R.id.anima);
        anim.setBackgroundResource(R.drawable.animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) anim.getBackground();

        Button allSaloons = view.findViewById(R.id.allsalons);
        allSaloons.setOnClickListener(v -> startActivity(new Intent(getActivity(), SalonsActivity.class)));

        Button beautyShops = view.findViewById(R.id.beautyshops);
        beautyShops.setOnClickListener(v -> startActivity(new Intent(getActivity(), ShopsActivity.class)));

        Button tips = view.findViewById(R.id.tips);
        tips.setOnClickListener(v -> startActivity(new Intent(getActivity(), TipsActivity.class)));

        Button consultant = view.findViewById(R.id.consultant);
        consultant.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ConsultantsActivity.class));
        });

        animationDrawable.start();

    }
}
