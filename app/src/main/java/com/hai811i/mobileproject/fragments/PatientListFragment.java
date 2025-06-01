package com.hai811i.mobileproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.utils.PatientAdapter;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.utils.PatientAdapter;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.util.ArrayList;
import java.util.List;

public class PatientListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PatientAdapter adapter;
    private List<PatientDTO> patientList = new ArrayList<>();
    private ProjectViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_list, container, false);
        recyclerView = view.findViewById(R.id.patient_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the same ViewModel instance used by the activity
        viewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        // Get doctorId from shared preferences
        long doctorId = requireActivity().getSharedPreferences("user_prefs", 0)
                .getLong("doctor_id", 0L);

        if (doctorId != 0) {
            viewModel.loadPatientsByDoctor(doctorId);
        } else {
            Toast.makeText(getContext(), "Doctor ID not found", Toast.LENGTH_SHORT).show();
        }

        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new PatientAdapter(patientList, patient -> {
            // This is the existing click handler for the patient item
            Toast.makeText(getContext(),
                    "Selected: " + patient.getFirstName() + " " + patient.getLastName(),
                    Toast.LENGTH_SHORT).show();
        }, patient -> {
            // This is the new click handler for the appointment button
            openPatientAppointmentsFragment(patient);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)
        );
    }

    private void openPatientAppointmentsFragment(PatientDTO patient) {
        // Create a bundle to pass patient data to the new fragment
        Bundle bundle = new Bundle();
        bundle.putLong("patient_id", patient.getId());
        bundle.putString("patient_name", patient.getFirstName() + " " + patient.getLastName());


         Fragment fragment = new PatientAppointmentsFragment();
         fragment.setArguments(bundle);
         requireActivity().getSupportFragmentManager().beginTransaction()
                 .replace(R.id.fragment_container, fragment)
                 .addToBackStack(null)
                 .commit();
    }

    private void observeViewModel() {
        viewModel.getPatientsLiveData().observe(getViewLifecycleOwner(), patients -> {
            if (patients != null) {
                patientList.clear();
                patientList.addAll(patients);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }
}