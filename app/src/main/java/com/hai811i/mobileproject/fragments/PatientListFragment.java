package com.hai811i.mobileproject.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.utils.PatientAdapter;
import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.entity.Patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class PatientListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PatientAdapter adapter;
    private List<Patient> patientList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_list, container, false);

        recyclerView = view.findViewById(R.id.patient_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        setupRecyclerView();
        loadPatients();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new PatientAdapter(patientList, patient -> {
            // Handle patient item click
            Toast.makeText(getContext(), "Selected: " + patient.getFirstName(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void loadPatients() {
        progressBar.setVisibility(View.VISIBLE);

        // Simulate network/database loading
        new Handler().postDelayed(() -> {
            patientList.clear();





            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }, 1500);
    }
}