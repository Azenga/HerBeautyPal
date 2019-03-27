package com.example.mercie.example.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.models.SalonService;

public class ServiceDialogPrompt extends AppCompatDialogFragment {

    private static final String PACKAGE_NAME = "packageName";

    private String packageName = "";

    //View widgets
    private EditText serviceNameET, serviceCostET;

    //Interaction Listener
    private ServiceDialogPromptListener promptListener;

    public ServiceDialogPrompt() {
    }

    public static ServiceDialogPrompt getInstance(String packageName) {

        ServiceDialogPrompt serviceDialogPrompt = new ServiceDialogPrompt();

        Bundle args = new Bundle();

        args.putString(PACKAGE_NAME, packageName);

        serviceDialogPrompt.setArguments(args);

        return serviceDialogPrompt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.service_prompt_dialog_layout, null);

        serviceNameET = view.findViewById(R.id.service_name_et);
        serviceCostET = view.findViewById(R.id.service_cost_et);

        builder.setView(view)
                .setTitle("Add a Service: Package(" + packageName + ")")
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                    Toast.makeText(getActivity(), "Add Service Cancelled", Toast.LENGTH_SHORT).show();
                })
                .setPositiveButton("Add", (DialogInterface dialog, int which) -> {

                    String name = serviceNameET.getText().toString();
                    String cost = serviceCostET.getText().toString();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cost)) {
                        promptListener.addService(new SalonService(packageName, name, cost));
                    } else {
                        Toast.makeText(getActivity(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
                    }

                });

        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            packageName = getArguments().getString(PACKAGE_NAME);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ServiceDialogPromptListener) {
            promptListener = (ServiceDialogPromptListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement the ServiceDialogPromptListener");
        }
    }

    public interface ServiceDialogPromptListener {
        void addService(SalonService service);
    }
}
