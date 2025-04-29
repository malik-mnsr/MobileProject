package com.hai811i.mobileproject;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.fragments.AssistantFragment;
import com.hai811i.mobileproject.fragments.DoctorProfileFragment;
import com.hai811i.mobileproject.fragments.PatientListFragment;

public class DoctorActivity extends AppCompatActivity {

    private TextView doctorName, doctorStatus;
    private ImageView imageViewProfile;
    private View statusBubble;
    private LinearLayout consultationOption, homeVisitOption, emergencyOption, dndOption;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String doctorJson = getIntent().getStringExtra("doctor_data");
        Doctor doctor = new Gson().fromJson(doctorJson, Doctor.class);
        // Method 2: Get from SharedPreferences (useful if activity gets recreated)
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String firstName = prefs.getString("doctor_firstName", "");
        String lastName = prefs.getString("doctor_lastName", "");
        String profilePic = prefs.getString("doctor_profilePicture", "");
        setContentView(R.layout.doctor_activity);

        // Initialize views
        doctorName = findViewById(R.id.doctorName);
        doctorStatus = findViewById(R.id.doctorStatus);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        statusBubble = findViewById(R.id.statusBubble);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Set doctor details
        doctorName.setText("Dr "+firstName + " "+ lastName);
        doctorStatus.setText("Available");
        doctorStatus.setTextColor(getResources().getColor(R.color.NeonGreen));

        // Profile picture click listener
        // Replace the existing profile picture click listener with:
        imageViewProfile.setOnClickListener(v -> {
            DoctorProfileFragment profileFragment = new DoctorProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, profileFragment)
                    .addToBackStack("doctor_profile")
                    .commit();
            hideMainContent();
        });

        // Action Grid click listeners
        consultationOption = findViewById(R.id.consultationButton);
        homeVisitOption = findViewById(R.id.homeVisitButton);
        emergencyOption = findViewById(R.id.emergencyButton);
        dndOption = findViewById(R.id.dndButton);

        consultationOption.setOnClickListener(v -> showToast("Consultation clicked"));
        homeVisitOption.setOnClickListener(v -> showToast("Home Visit clicked"));
        emergencyOption.setOnClickListener(v -> showToast("Emergency clicked"));
        dndOption.setOnClickListener(v -> showToast("Do Not Disturb clicked"));
        loadProfilePicture(profilePic);
        // Bottom Navigation setup
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_accueil);
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_accueil) {
                    showToast("Back To Home");
                    showMainContent();
                    return true;
                } else if (itemId == R.id.nav_rdv) {
                    showToast("Rendez-vous selected");
                    showMainContent();
                    return true;
                } else if (itemId == R.id.nav_ai) {
                    showAssistantFragment();
                    return true;
                } else if (itemId == R.id.nav_fiche) {
                    showPatientListFragment();
                    return true;
                }
                return false;
            };

    private void showAssistantFragment() {
        hideMainContent();
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

    private void showPatientListFragment() {
        hideMainContent();
        try {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, new PatientListFragment())
                    .addToBackStack("patient_list")
                    .commit();
        } catch (IllegalStateException e) {
            if (!isFinishing()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new PatientListFragment())
                        .commitAllowingStateLoss();
            }
        }
    }

    private void hideMainContent() {
        View header = findViewById(R.id.headerLayout);
        if (header != null) header.setVisibility(View.GONE);

        View content = findViewById(R.id.action_grid_container);
        if (content != null) content.setVisibility(View.GONE);
    }

    private void showMainContent() {
        // Show original content
        View header = findViewById(R.id.headerLayout);
        if (header != null) header.setVisibility(View.VISIBLE);

        View content = findViewById(R.id.action_grid_container);
        if (content != null) content.setVisibility(View.VISIBLE);

        // Remove any fragments
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            showMainContent();
            bottomNav.setSelectedItemId(R.id.nav_accueil);
        } else {
            super.onBackPressed();
        }
    }
    private void loadProfilePicture(String profilePicData) {
        if (profilePicData != null && !profilePicData.isEmpty()) {
            try {
                // Check if the data is Base64 encoded
                if (isBase64(profilePicData)) {
                    // Decode Base64 string to byte array
                    byte[] imageBytes = Base64.decode(profilePicData, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(
                            getResources(),
                            bitmap
                    );
                    roundedDrawable.setCircular(true);
                    roundedDrawable.setAntiAlias(true);
                    imageViewProfile.setImageDrawable(roundedDrawable);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Fallback to default
        imageViewProfile.setImageResource(R.drawable.ic_default_profile);
    }

    private boolean isBase64(String str) {
        // Simple check for Base64 string
        if (str == null) return false;
        try {
            Base64.decode(str, Base64.DEFAULT);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
