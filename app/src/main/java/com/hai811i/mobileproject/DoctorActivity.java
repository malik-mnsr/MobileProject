package com.hai811i.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hai811i.mobileproject.fragments.AssistantFragment;
import com.hai811i.mobileproject.fragments.AppointmentListFragment;

public class DoctorActivity extends AppCompatActivity {

    private TextView doctorName, doctorStatus;
    private ImageView imageViewProfile;
    private LinearLayout consultationOption, homeVisitOption, emergencyOption, dndOption;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);

        // Initialisation des vues
        doctorName        = findViewById(R.id.doctorName);
        doctorStatus      = findViewById(R.id.doctorStatus);
        imageViewProfile  = findViewById(R.id.imageViewProfile);
        bottomNav         = findViewById(R.id.bottom_navigation);

        // Détails du médecin
        doctorName.setText("Dr. John Doe");
        doctorStatus.setText("Available");
        doctorStatus.setTextColor(getResources().getColor(R.color.NeonGreen));

        // Clic sur la photo
        imageViewProfile.setOnClickListener(v -> showToast("Profile picture clicked"));

        // Grid d’actions (consultation / visite / urgence / DND)
        consultationOption = findViewById(R.id.consultationButton);
        homeVisitOption    = findViewById(R.id.homeVisitButton);
        emergencyOption    = findViewById(R.id.emergencyButton);
        dndOption          = findViewById(R.id.dndButton);

        consultationOption.setOnClickListener(v -> showToast("Consultation clicked"));
        homeVisitOption   .setOnClickListener(v -> showToast("Home Visit clicked"));
        emergencyOption   .setOnClickListener(v -> showToast("Emergency clicked"));
        dndOption         .setOnClickListener(v -> showToast("Do Not Disturb clicked"));

        // Setup BottomNavigation
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Onglet par défaut
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_accueil);
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int id = item.getItemId();
                if (id == R.id.nav_accueil) {
                    showMainContent();
                    return true;
                }
                else if (id == R.id.nav_fiche) {
                    showToast("Fiche selected");
                    showMainContent();
                    return true;
                }
                else if (id == R.id.nav_ai) {
                    showAssistantFragment();
                    return true;
                }
                else if (id == R.id.nav_rdv) {
                    showAppointmentFragment();
                    return true;
                }
                return false;
            };

    private void showAssistantFragment() {
        // Cache header + grid
        View header  = findViewById(R.id.headerLayout);
        View grid    = findViewById(R.id.action_grid_container);
        if (header != null) header.setVisibility(View.GONE);
        if (grid   != null) grid  .setVisibility(View.GONE);

        // Injecte l’AssistantFragment
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

    private void showAppointmentFragment() {
        // Cache header + grid
        View header  = findViewById(R.id.headerLayout);
        View grid    = findViewById(R.id.action_grid_container);
        if (header != null) header.setVisibility(View.GONE);
        if (grid   != null) grid  .setVisibility(View.GONE);

        // Injecte l’AppointmentListFragment
        try {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, new AppointmentListFragment())
                    .addToBackStack("rdv")
                    .commit();
        } catch (IllegalStateException e) {
            if (!isFinishing()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new AppointmentListFragment())
                        .commitAllowingStateLoss();
            }
        }
    }

    private void showMainContent() {
        // Réaffiche header + grid
        View header  = findViewById(R.id.headerLayout);
        View grid    = findViewById(R.id.action_grid_container);
        if (header != null) header.setVisibility(View.VISIBLE);
        if (grid   != null) grid  .setVisibility(View.VISIBLE);

        // Supprime tout fragment injecté
        Fragment current = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (current != null && !isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .remove(current)
                    .commitNow();
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
