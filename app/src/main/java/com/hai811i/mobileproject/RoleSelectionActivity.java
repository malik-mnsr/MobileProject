package com.hai811i.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.hai811i.mobileproject.ChoixActivity;
import com.hai811i.mobileproject.DoctorActivity;
import com.hai811i.mobileproject.PatientWelcomeActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    private MaterialButton btnPatient;
    private MaterialButton btnDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient); // Replace with your actual layout file name

        // Initialize buttons
        btnPatient = findViewById(R.id.btnPatient);
        btnDoctor = findViewById(R.id.btnDoctor);

        // Set click listeners
        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePatientSelection();
            }
        });

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctorSelection();
            }
        });
    }

    private void handlePatientSelection() {
        // Start Patient activity or show patient-specific UI
        Toast.makeText(this, "Patient selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PatientWelcomeActivity.class); // Replace with your actual Patient activity
        startActivity(intent);
    }

    private void handleDoctorSelection() {
        // Start Doctor activity or show doctor-specific UI
        Toast.makeText(this, "Doctor selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChoixActivity.class); // Replace with your actual Doctor activity
        startActivity(intent);
    }

    // Optional: Add animation to button clicks
    private void animateButtonClick(View view) {
        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start())
                .start();
    }
}