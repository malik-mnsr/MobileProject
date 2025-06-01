package com.hai811i.mobileproject.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.AppointmentDTO;
import com.hai811i.mobileproject.dto.MedicalRecordDTO;
import com.hai811i.mobileproject.dto.PrescriptionDTO;
import com.hai811i.mobileproject.entity.DrugReference;
import com.hai811i.mobileproject.entity.Page;
import com.hai811i.mobileproject.utils.AppointmentAdapter;
import com.hai811i.mobileproject.utils.DrugArrayAdapter;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PatientAppointmentsFragment extends Fragment implements AppointmentAdapter.OnAppointmentClickListener {
    private Long selectedRecordId;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvPatientName;
    private AppointmentAdapter adapter;
    private List<AppointmentDTO> appointmentList = new ArrayList<>();
    private ProjectViewModel viewModel;
    private long patientId;
    private String patientName;
    private Button btnAccept, btnReject, btnCreatePrescription, btnDownloadPrescription;
    private Long selectedAppointmentId = null;
    private int selectedPosition = -1;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_appointments, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.appointments_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        tvPatientName = view.findViewById(R.id.tv_patient_name);
        btnAccept = view.findViewById(R.id.btn_accept);
        btnReject = view.findViewById(R.id.btn_reject);
        btnCreatePrescription = view.findViewById(R.id.btn_create_prescription);
        btnDownloadPrescription = view.findViewById(R.id.btn_download_prescription);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            patientId = getArguments().getLong("patient_id");
            patientName = getArguments().getString("patient_name");
            tvPatientName.setText(patientName);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        setupRecyclerView();
        setupButtonListeners();
        observeViewModel();

        if (patientId != 0) {
            viewModel.getPatientAppointments(patientId);
        } else {
            Toast.makeText(getContext(), "Patient ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupButtonListeners() {
        btnAccept.setOnClickListener(v -> handleAppointmentAcceptance());
        btnReject.setOnClickListener(v -> handleAppointmentRejection());
        btnCreatePrescription.setOnClickListener(v -> handlePrescriptionCreation());
        btnDownloadPrescription.setOnClickListener(v -> handlePrescriptionDownload());
    }

    private void handleAppointmentAcceptance() {
        if (selectedAppointmentId != null) {
            viewModel.acceptAppointment(selectedAppointmentId);
        } else {
            showSelectionError();
        }
    }

    private void handleAppointmentRejection() {
        if (selectedAppointmentId != null) {
            viewModel.rejectAppointment(selectedAppointmentId);
        } else {
            showSelectionError();
        }
    }

    private void handlePrescriptionCreation() {
        if (selectedAppointmentId != null) {
            MedicalRecordDTO medicalRecord = new MedicalRecordDTO();
            medicalRecord.setDateCreated(LocalDate.now().toString());
            medicalRecord.setTitle("Medical Record for Appointment");
            medicalRecord.setDescription("Created from appointment");
            viewModel.createMedicalRecordForAppointment(selectedAppointmentId, medicalRecord);
        } else {
            showSelectionError();
        }
    }

    private void handlePrescriptionDownload() {
        if (selectedAppointmentId != null) {
            // First get the medical record for the appointment
            viewModel.getMedicalRecordByAppointment(selectedAppointmentId);
        } else {
            showSelectionError();
        }
    }

    private void showSelectionError() {
        Toast.makeText(getContext(), "Please select an appointment first", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppointmentClick(AppointmentDTO appointment) {
        selectedAppointmentId = appointment.getId();
        updateSelectedPosition(appointment);
        showAppointmentDetails(appointment);
    }

    private void updateSelectedPosition(AppointmentDTO appointment) {
        int newPosition = -1;
        for (int i = 0; i < appointmentList.size(); i++) {
            if (appointmentList.get(i).getId().equals(selectedAppointmentId)) {
                newPosition = i;
                break;
            }
        }

        int previousPosition = selectedPosition;
        selectedPosition = newPosition;

        if (previousPosition != -1) {
            adapter.notifyItemChanged(previousPosition);
        }
        if (selectedPosition != -1) {
            adapter.notifyItemChanged(selectedPosition);
        }
    }

    private void showAppointmentDetails(AppointmentDTO appointment) {
        Toast.makeText(getContext(),
                "Selected: " + appointment.getMotif() +
                        "\nStatus: " + appointment.getStatus(),
                Toast.LENGTH_SHORT).show();
    }

    private void observeViewModel() {
        // Appointments
        viewModel.getAppointmentsList().observe(getViewLifecycleOwner(), this::handleAppointmentsUpdate);
        viewModel.getAppointment().observe(getViewLifecycleOwner(), this::handleAppointmentUpdate);

        // Loading states
        viewModel.getLoading().observe(getViewLifecycleOwner(), this::handleLoadingState);
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), this::handleLoadingState);

        // Operation results
        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), this::handleOperationSuccess);
        viewModel.getOperationSuccessLiveData().observe(getViewLifecycleOwner(), this::handleOperationSuccess);

        // Medical Records
        viewModel.getMedicalRecord().observe(getViewLifecycleOwner(), this::handleMedicalRecord);
        viewModel.getMedicalRecordLiveData().observe(getViewLifecycleOwner(), this::handleMedicalRecordCreation);

        // Prescriptions
        viewModel.getPrescriptionLiveData().observe(getViewLifecycleOwner(), this::handlePrescriptionCreation);
        viewModel.getDownloadedPdfLiveData().observe(getViewLifecycleOwner(), this::handlePrescriptionDownload);

        // Errors
        viewModel.getError().observe(getViewLifecycleOwner(), this::handleError);
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), this::handleError);
        viewModel.getMedicalRecordErrorLiveData().observe(getViewLifecycleOwner(), this::handleMedicalRecordError);
        viewModel.getPrescriptionsErrorLiveData().observe(getViewLifecycleOwner(), this::handlePrescriptionError);

        // Drug search
        viewModel.getDrugsPageLiveData().observe(getViewLifecycleOwner(), this::handleDrugSearchResults);
    }

    private void handleAppointmentsUpdate(List<AppointmentDTO> appointments) {
        if (appointments != null && !appointments.isEmpty()) {
            appointmentList.clear();
            appointmentList.addAll(appointments);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            tvPatientName.setVisibility(View.VISIBLE);
            selectedAppointmentId = null;
            selectedPosition = -1;
        } else {
            hideAllViews();
        }
    }

    private void handleAppointmentUpdate(AppointmentDTO appointment) {
        if (appointment != null) {
            // Update the specific appointment in the list
            for (int i = 0; i < appointmentList.size(); i++) {
                if (appointmentList.get(i).getId().equals(appointment.getId())) {
                    appointmentList.set(i, appointment);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    private void handleMedicalRecord(MedicalRecordDTO medicalRecord) {
        if (medicalRecord != null && medicalRecord.getRecordId() != null) {
            // Now that we have the medical record, download the prescription PDF
            viewModel.downloadPrescriptionPdf(Long.valueOf(medicalRecord.getRecordId()));
        }
    }

    private void handleError(String errorMessage) {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLoadingState(Boolean isLoading) {
        if (isLoading != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void handleOperationSuccess(Boolean success) {
        if (success != null && success) {
            // Refresh appointments after successful operation
            viewModel.getPatientAppointments(patientId);
        }
    }

    private void handleMedicalRecordCreation(MedicalRecordDTO medicalRecord) {
        if (medicalRecord != null && medicalRecord.getRecordId() != null) {
            selectedRecordId = Long.valueOf(medicalRecord.getRecordId());
            showPrescriptionCreationDialog();
        }
    }

    private void handlePrescriptionCreation(PrescriptionDTO prescription) {
        if (prescription != null) {
            Toast.makeText(getContext(), "Prescription created successfully", Toast.LENGTH_SHORT).show();
            // Refresh appointments to show updated status
            viewModel.getPatientAppointments(patientId);
        }
    }

    private void handlePrescriptionDownload(File file) {
        if (file != null) {
            openPdfFile(file);
        }
    }

    private void handleMedicalRecordError(String errorMessage) {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            Toast.makeText(getContext(), "Medical record error: " + errorMessage,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePrescriptionError(String error) {
        if (error != null) {
            Toast.makeText(getContext(), "Prescription error: " + error,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDrugSearchResults(Page<DrugReference> drugPage) {
        if (drugPage != null && drugPage.getContent() != null) {
            // This is handled in the dialog's TextWatcher
        }
    }

    private void hideAllViews() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvPatientName.setVisibility(View.GONE);
    }

    private void showPrescriptionCreationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create New Prescription");

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_create_prescription, null);
        builder.setView(dialogView);

        AutoCompleteTextView actvMedications = dialogView.findViewById(R.id.actv_medications);
        EditText etValidityDays = dialogView.findViewById(R.id.et_validity_days);
        EditText etPharmacyDetails = dialogView.findViewById(R.id.et_pharmacy_details);
        CheckBox cbSendToPharmacy = dialogView.findViewById(R.id.cb_send_to_pharmacy);

        setupDrugSearch(actvMedications);

        builder.setPositiveButton("Create", (dialog, which) -> {
            try {
                createPrescription(actvMedications, etValidityDays, etPharmacyDetails, cbSendToPharmacy);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void setupDrugSearch(AutoCompleteTextView actvMedications) {
        DrugArrayAdapter adapter = new DrugArrayAdapter(getContext(), new ArrayList<>());
        actvMedications.setAdapter(adapter);

        actvMedications.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    viewModel.getDrugs(s.toString(), 0, 10, "name,asc");
                                });
                            }
                        }
                    }, 500);
                }
            }
        });
    }

    private void createPrescription(AutoCompleteTextView actvMedications, EditText etValidityDays,
                                    EditText etPharmacyDetails, CheckBox cbSendToPharmacy) {
        DrugReference selectedDrug = getSelectedDrug(actvMedications);

        PrescriptionDTO prescription = new PrescriptionDTO();
        prescription.setMedications(selectedDrug != null ?
                selectedDrug.getName() + " (" + selectedDrug.getAtcCode() + ")" :
                actvMedications.getText().toString());

        try {
            prescription.setValidityDays(Integer.parseInt(etValidityDays.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number of days", Toast.LENGTH_SHORT).show();
            return;
        }

        prescription.setPharmacyDetails(etPharmacyDetails.getText().toString());
        prescription.setSentToPharmacy(cbSendToPharmacy.isChecked());
        prescription.setDateIssued(LocalDate.now());
        prescription.setStatus("ACTIVE");

        if (selectedRecordId != null) {
            viewModel.createPrescription(Math.toIntExact(selectedRecordId), prescription);
        } else {
            Toast.makeText(getContext(), "No medical record selected", Toast.LENGTH_SHORT).show();
        }
    }

    private DrugReference getSelectedDrug(AutoCompleteTextView actvMedications) {
        if (actvMedications.getAdapter() != null) {
            for (int i = 0; i < actvMedications.getAdapter().getCount(); i++) {
                DrugReference drug = (DrugReference) actvMedications.getAdapter().getItem(i);
                if (drug.getName().equals(actvMedications.getText().toString())) {
                    return drug;
                }
            }
        }
        return null;
    }

    private void openPdfFile(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(getContext(),
                    getContext().getPackageName() + ".provider", file), "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No PDF viewer installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }
}