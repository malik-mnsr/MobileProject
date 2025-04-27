package com.hai811i.mobileproject.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.entity.Appointment;
import com.hai811i.mobileproject.entity.Patient;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class NewAppointmentDialogFragment extends DialogFragment {

    public interface Listener {
        void onAppointmentCreated(Appointment appt);
    }

    private Spinner spinnerPatient;
    private EditText etDate, etTime, etReason;
    private Listener listener;
    private List<Patient> patientList;
    private long chosenTimestamp;

    public NewAppointmentDialogFragment(Listener listener, List<Patient> patients) {
        this.listener = listener;
        this.patientList = patients;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_new_appointment, container, false);
        spinnerPatient = v.findViewById(R.id.spinnerPatient);
        etDate = v.findViewById(R.id.etDate);
        etTime = v.findViewById(R.id.etTime);
        etReason = v.findViewById(R.id.etReason);

        ArrayAdapter<Patient> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                patientList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatient.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();

        etDate.setOnClickListener(x -> new DatePickerDialog(requireContext(),
                (view, y, m, d) -> {
                    cal.set(Calendar.YEAR, y);
                    cal.set(Calendar.MONTH, m);
                    cal.set(Calendar.DAY_OF_MONTH, d);
                    etDate.setText(String.format("%02d/%02d/%04d", d, m+1, y));
                    chosenTimestamp = cal.getTimeInMillis();
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show());

        etTime.setOnClickListener(x -> new TimePickerDialog(requireContext(),
                (view, h, min) -> {
                    cal.set(Calendar.HOUR_OF_DAY, h);
                    cal.set(Calendar.MINUTE, min);
                    etTime.setText(String.format("%02d:%02d", h, min));
                    chosenTimestamp = cal.getTimeInMillis();
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
        ).show());

        setCancelable(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                Patient selected = (Patient) spinnerPatient.getSelectedItem();
                String reason = etReason.getText().toString().trim();
                if (selected == null || reason.isEmpty() || chosenTimestamp == 0) {
                    // TODO: afficher erreurs
                    return;
                }
                Appointment appt = new Appointment(
                        UUID.randomUUID().toString(),
                        selected.getId(),
                        chosenTimestamp,
                        reason
                );
                listener.onAppointmentCreated(appt);
                dismiss();
            });
        }
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_appointment)
                .setView(onCreateView(getLayoutInflater(), null, savedInstanceState))
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (d, w) -> dismiss())
                .create();
    }
}