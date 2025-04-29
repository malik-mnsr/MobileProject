package com.hai811i.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class PatientWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_or_schedule); // Make sure this matches your XML file name


        CardView cardPrescription = findViewById(R.id.cardPrescription);
        CardView cardAppointment = findViewById(R.id.cardAppointment);

        // Set click listeners
        cardPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // openPrescriptionScreen();
                showToast("Precription Pressed");
            }
        });

        cardAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle appointment card click
            //    openAppointmentScreen();
                showToast("Appoitment Pressed");
            }
        });

        // You can customize the welcome text with the patient's name if available
        // String patientName = getIntent().getStringExtra("PATIENT_NAME");
        // if (patientName != null) {
        //     welcomeText.setText("Welcome, " + patientName + "!");
        // }
    }

    private void openPrescriptionScreen() {


        // OR if using Fragments:
        // getSupportFragmentManager().beginTransaction()
        //     .replace(R.id.fragment_container, new PrescriptionFragment())
        //     .addToBackStack(null)
        //     .commit();
    }

    private void openAppointmentScreen() {


        // OR if using Fragments:
        // getSupportFragmentManager().beginTransaction()
        //     .replace(R.id.fragment_container, new AppointmentFragment())
        //     .addToBackStack(null)
        //     .commit();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}