package com.example.mercie.example.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.Appointment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AppointmentsRecylcerViewAdapter extends RecyclerView.Adapter<AppointmentsRecylcerViewAdapter.ViewHolder> {

    private List<Appointment> appointments;
    private Context context;

    private FirebaseFirestore mDb;

    public AppointmentsRecylcerViewAdapter(List<Appointment> appointments, Context context) {
        this.appointments = appointments;
        this.context = context;

        mDb = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_appointment_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Appointment appointment = appointments.get(position);

        viewHolder.serviceNameTV.setText(appointment.getServiceName());
        viewHolder.timeTV.setText(appointment.getDate() + ' ' + appointment.getTime());

        mDb.collection("clients")
                .document(appointment.getFromId())
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.getResult().exists()) {
                                String clientName = task.getResult().getString("name");
                                viewHolder.clientNameTV.setText(clientName);

                            }
                        }
                );

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTV, timeTV, clientNameTV;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            serviceNameTV = itemView.findViewById(R.id.service_name_tv);
            timeTV = itemView.findViewById(R.id.time_tv);
            clientNameTV = itemView.findViewById(R.id.client_name_tv);
        }
    }
}
