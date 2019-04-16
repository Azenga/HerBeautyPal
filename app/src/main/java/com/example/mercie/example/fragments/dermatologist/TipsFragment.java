package com.example.mercie.example.fragments.dermatologist;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.AddTipActivity;
import com.example.mercie.example.R;
import com.example.mercie.example.adapters.DermatologistTipsRecyclerViewAdapter;
import com.example.mercie.example.models.Tip;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipsFragment extends Fragment {
    private static final String TAG = "TipsFragment";

    private RecyclerView tipsRV;
    private FloatingActionButton addTipFAB;
    private List<Tip> tipList;

    private FirebaseFirestore mDb;
    private DermatologistTipsRecyclerViewAdapter adapter;

    public TipsFragment() {
        mDb = FirebaseFirestore.getInstance();
        tipList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dermatologist_fragment_tips, container, false);

        tipsRV = view.findViewById(R.id.tips_rv);
        tipsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        tipsRV.setHasFixedSize(true);
        adapter = new DermatologistTipsRecyclerViewAdapter(tipList);
        tipsRV.setAdapter(adapter);
        addTipFAB = view.findViewById(R.id.add_tip_fab);

        addTipFAB.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTipActivity.class)));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Get tips and display them in a RecyclerView

        mDb.collection("tips")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {

                            if (e != null) {
                                Log.e(TAG, "onViewCreated: ", e);
                                return;
                            }

                            if (!queryDocumentSnapshots.isEmpty()) {
                                tipList.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Tip tip = snapshot.toObject(Tip.class);
                                    tipList.add(tip);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }
                );

    }
}
