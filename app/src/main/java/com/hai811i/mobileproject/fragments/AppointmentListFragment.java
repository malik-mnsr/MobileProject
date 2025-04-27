package com.hai811i.mobileproject.fragments;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.entity.Appointment;
import com.hai811i.mobileproject.entity.Patient;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListFragment extends Fragment {
    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();
    private List<Patient> patientList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        rvAppointments = view.findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        // Mock patients for Spinner
        patientList.add(new Patient("id1","Alice","Dupont","01/01/1980","Aucun antécédent"));
        patientList.add(new Patient("id2","Bob","Martin","05/03/1975","Allergie: Pollen"));

        // Mock appointments
        // appointmentList.add(new Appointment(...));

        adapter = new AppointmentAdapter(
                appointmentList,
                appt -> Toast.makeText(getContext(),
                        "Rendez-vous du patient " + appt.getPatientId(),
                        Toast.LENGTH_SHORT).show()
        );
        rvAppointments.setAdapter(adapter);

        view.findViewById(R.id.fabAdd).setOnClickListener(v -> {
            NewAppointmentDialogFragment dialog = new NewAppointmentDialogFragment(
                    appt -> {
                        appointmentList.add(appt);
                        adapter.notifyItemInserted(appointmentList.size() - 1);
                    },
                    patientList
            );
            dialog.show(getParentFragmentManager(), "newAppt");
        });

        loadAppointments();
        return view;
    }

    private void loadAppointments() {
        // TODO: charger les rendez-vous depuis la DB locale ou Firebase
        adapter.notifyDataSetChanged();
    }

    /** Expose patient list to dialog */
    public List<Patient> getPatientList() {
        return patientList;
    }
}
