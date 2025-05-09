package com.hai811i.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.hai811i.mobileproject.fragments.AppointmentBookingFragment;

public class PatientWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_or_schedule);

        CardView cardPrescription = findViewById(R.id.cardPrescription);
        CardView cardAppointment = findViewById(R.id.cardAppointment);

        cardPrescription.setOnClickListener(v -> {
            showToast("Prescription Pressed");
            // openPrescriptionScreen();
        });

        cardAppointment.setOnClickListener(v -> {
            showToast("Appointment Pressed");
            openAppointmentScreen();
        });
    }

    private void openAppointmentScreen() {
        // Get references to the main content and fragment container
        LinearLayout mainContent = findViewById(R.id.main_content);
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        if (mainContent != null && fragmentContainer != null) {
            // Hide all children of the main LinearLayout (everything except fragment_container)
            for (int i = 0; i < mainContent.getChildCount(); i++) {
                View child = mainContent.getChildAt(i);
                if (child.getId() != R.id.fragment_container) {
                    child.setVisibility(View.GONE);
                }
            }

            // Show the fragment container
            fragmentContainer.setVisibility(View.VISIBLE);

            // Add the fragment
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                    )
                    .replace(R.id.fragment_container, new AppointmentBookingFragment())
                    .addToBackStack("appointment_fragment")
                    .commit();
        } else {
            Toast.makeText(this, "Error loading appointment screen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
            // Show all children of the main LinearLayout again
            LinearLayout mainContent = findViewById(R.id.main_content);
            if (mainContent != null) {
                for (int i = 0; i < mainContent.getChildCount(); i++) {
                    View child = mainContent.getChildAt(i);
                    child.setVisibility(View.VISIBLE);
                }
            }

            // Hide the fragment container
            fragmentContainer.setVisibility(View.GONE);

            // Remove fragment from back stack
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}