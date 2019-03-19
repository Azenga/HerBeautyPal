package com.example.mercie.example.fragments.salonist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.SalonistDashboardActivity;
import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.SalonService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalonistAddServiceFragment extends Fragment {

    private Spinner packageSpinner;
    private TextInputEditText serviceNameTIET;
    private TextInputEditText serviceCostTIET;
    private Button addServiceBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public SalonistAddServiceFragment() {
        //This must be empty
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salonist_add_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init the widgets
        String[] packages = getActivity().getResources().getStringArray(R.array.beauty_packages);
        ArrayAdapter<String> packagesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, packages);

        packageSpinner = view.findViewById(R.id.service_package_spinner);
        packageSpinner.setAdapter(packagesAdapter);

        serviceNameTIET = view.findViewById(R.id.service_name_tiet);
        serviceCostTIET = view.findViewById(R.id.service_cost_tiet);
        addServiceBtn = view.findViewById(R.id.add_service_btn);

        addServiceBtn.setOnClickListener(v -> addSalonService());


    }

    private void addSalonService() {
        String packageName = packageSpinner.getSelectedItem().toString();
        String serviceName = serviceNameTIET.getText().toString();
        String serviceCost = serviceCostTIET.getText().toString();

        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(serviceName) && !TextUtils.isEmpty(serviceCost)) {
            SalonService service = new SalonService(packageName, serviceName, serviceCost);

            getSalonist(service);
        } else {
            Toast.makeText(getActivity(), "All the fields are required", Toast.LENGTH_SHORT).show();
        }
    }


    private void getSalonist(SalonService service) {
        DocumentReference salonistRef = mFirestore.collection("salons").document(mAuth.getCurrentUser().getUid());

        salonistRef.get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot salonistSnapshot = task.getResult();

                                if (salonistSnapshot.exists()) {

                                    Salon salon = salonistSnapshot.toObject(Salon.class);

                                    //putTheUpatedSalonBack(salon);

                                } else {
                                    Toast.makeText(getActivity(), "You should update your profile", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), "Erro occurred getting salonist details: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void putTheUpatedSalonBack(Salon salon) {
        mFirestore.collection("salons")
                .document(mAuth.getCurrentUser().getUid())
                .set(salon)
                .addOnCompleteListener(
                        task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(getActivity(), "Service Added", Toast.LENGTH_SHORT).show();

                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(2000);
                                            super.run();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();

                                ((SalonistDashboardActivity) getActivity()).displayFragment(R.id.nav_home);

                            } else {
                                Toast.makeText(getActivity(), "Update details error: " + task1.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
    }

    //Normally makes sure there is communication between the Fragment and the FragmentActivity
    @Override
    public void onStart() {
        super.onStart();

        //Init Firebase variables

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


    }
}
