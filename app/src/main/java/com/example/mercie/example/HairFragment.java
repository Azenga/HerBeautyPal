package com.example.mercie.example;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HairFragment extends Fragment {
private Button dandruff,splitends;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_hair_fragment, container, false);
        dandruff=(Button) view.findViewById(R.id.dandruff);
        splitends = (Button) view.findViewById(R.id.splitends);
        dandruff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DandruffActivity.class));
            }
        });
        splitends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DandruffActivity.class));
                TextView danduraffchange = (TextView) view.findViewById(R.id.danduraffchange);
                danduraffchange.setText("Split Ends");


            }
        });
        return view;


    }



}
