package com.hai811i.mobileproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hai811i.mobileproject.fragments.AssistantFragment;

public class DoctorActivity extends AppCompatActivity {

    private TextView doctorName, doctorStatus;
    private ImageView imageViewProfile;
    private View statusBubble;
    private LinearLayout consultationOption, homeVisitOption, emergencyOption, dndOption;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity); // Make sure this is your main layout with BottomNavigationView

        // Initialize views
        doctorName = findViewById(R.id.doctorName);
        doctorStatus = findViewById(R.id.doctorStatus);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        statusBubble = findViewById(R.id.statusBubble);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Set doctor details
        doctorName.setText("Dr. John Doe");
        doctorStatus.setText("Available");
        doctorStatus.setTextColor(getResources().getColor(R.color.NeonGreen));

        // Profile picture click listener
        imageViewProfile.setOnClickListener(v -> showToast("Profile picture clicked"));

        // Action Grid click listeners
        consultationOption = findViewById(R.id.consultationButton);
        homeVisitOption = findViewById(R.id.homeVisitButton);
        emergencyOption = findViewById(R.id.emergencyButton);
        dndOption = findViewById(R.id.dndButton);

        consultationOption.setOnClickListener(v -> showToast("Consultation clicked"));
        homeVisitOption.setOnClickListener(v -> showToast("Home Visit clicked"));
        emergencyOption.setOnClickListener(v -> showToast("Emergency clicked"));
        dndOption.setOnClickListener(v -> showToast("Do Not Disturb clicked"));

        // Bottom Navigation setup
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_accueil); // Set default selected item
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
if (itemId == R.id.nav_accueil){
    showToast("Back To Home");
    showMainContent();
    return true;
}
               else if (itemId == R.id.nav_rdv) {
                    showToast("rendezvous selected");
                    showMainContent();
                    return true;
                }
                else if (itemId == R.id.nav_ai) {
                    showAssistantFragment();
                    return true;
                }
                else if (itemId == R.id.nav_fiche) {
                    showToast("Fiche selected");
                    return true;
                }

                return false;
            };

    private void showAssistantFragment() {
        // Hide original content safely
        View header = findViewById(R.id.headerLayout);
        if (header != null) header.setVisibility(View.GONE);

        View content = findViewById(R.id.action_grid_container);
        if (content != null) content.setVisibility(View.GONE);

        // Show fragment with error handling
        try {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, new AssistantFragment())
                    .addToBackStack("assistant")
                    .commit();
        } catch (IllegalStateException e) {

            if (!isFinishing()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new AssistantFragment())
                        .commitAllowingStateLoss();
            }
        }
    }

    private void showMainContent() {
        // Show original content
        View header = findViewById(R.id.headerLayout);
        if (header != null) header.setVisibility(View.VISIBLE);

        View content = findViewById(R.id.action_grid_container);
        if (content != null) content.setVisibility(View.VISIBLE);

        // Remove fragment safely
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && !isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .remove(fragment)
                    .commitNow();
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

// .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)