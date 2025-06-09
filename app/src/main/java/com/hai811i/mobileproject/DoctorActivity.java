package com.hai811i.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.hai811i.mobileproject.api.RetrofitClient;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.entity.WorkingMode;
import com.hai811i.mobileproject.fragments.AssistantFragment;
import com.hai811i.mobileproject.fragments.DndFragment;
import com.hai811i.mobileproject.fragments.DoctorCalendarFragment;
import com.hai811i.mobileproject.fragments.DoctorProfileFragment;
import com.hai811i.mobileproject.fragments.HomeVisitFragment;
import com.hai811i.mobileproject.fragments.PatientListFragment;
import com.hai811i.mobileproject.implementation.AppointmentRepositoryImpl;
import com.hai811i.mobileproject.implementation.DoctorRepositoryImpl;
import com.hai811i.mobileproject.implementation.DrugRepositoryImpl;
import com.hai811i.mobileproject.implementation.GoogleCalendarRepositoryImpl;
import com.hai811i.mobileproject.implementation.MedicalRecordRepositoryImpl;
import com.hai811i.mobileproject.implementation.NotificationRepositoryImpl;
import com.hai811i.mobileproject.implementation.PatientRepositoryImpl;
import com.hai811i.mobileproject.implementation.PrescriptionsRepositoryImpl;
import com.hai811i.mobileproject.implementation.SlotRepositoryImpl;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.DrugRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.MedicalRecordRepository;
import com.hai811i.mobileproject.repository.NotificationRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
import com.hai811i.mobileproject.repository.PrescriptionsRepository;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.utils.ProjectViewModelFactory;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

public class DoctorActivity extends AppCompatActivity {


    private TextView doctorName, doctorStatus;
    private ImageView imageViewProfile;
    private View statusBubble;
    private LinearLayout consultationOption, homeVisitOption, emergencyOption, dndOption;
    private BottomNavigationView bottomNav;
    private ProjectViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String doctorJson = getIntent().getStringExtra("doctor_data");
        Doctor doctor = new Gson().fromJson(doctorJson, Doctor.class);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String firstName = prefs.getString("doctor_firstName", "");
        String lastName = prefs.getString("doctor_lastName", "");
        String profilePic = prefs.getString("doctor_profilePicture", "");
        long doctorId = prefs.getLong("doctor_id", 0L);
        setContentView(R.layout.doctor_activity);

        // Initialize ViewModel
        GoogleCalendarRepository googleCalendarRepository = new GoogleCalendarRepositoryImpl(RetrofitClient.getApiService());
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl(RetrofitClient.getApiService());
        PatientRepository patientRepository = new PatientRepositoryImpl(RetrofitClient.getApiService());
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(RetrofitClient.getApiService());
        SlotRepository slotRepository = new SlotRepositoryImpl(RetrofitClient.getApiService());
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepositoryImpl(RetrofitClient.getApiService());
        PrescriptionsRepository prescriptionsRepository = new PrescriptionsRepositoryImpl(RetrofitClient.getApiService());
        NotificationRepository notificationRepository = new NotificationRepositoryImpl(RetrofitClient.getApiService());
        DrugRepository drugRepository = new DrugRepositoryImpl(RetrofitClient.getApiService());
        // Initialize ViewModel with factory
        ProjectViewModelFactory factory = new ProjectViewModelFactory(doctorRepository,patientRepository, slotRepository,appointmentRepository,googleCalendarRepository, medicalRecordRepository,prescriptionsRepository,notificationRepository,drugRepository);
        viewModel = new ViewModelProvider(this, factory).get(ProjectViewModel.class);


        // Initialize views
        doctorName = findViewById(R.id.doctorName);
        doctorStatus = findViewById(R.id.doctorStatus);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        statusBubble = findViewById(R.id.statusBubble);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Set doctor details
        doctorName.setText("Dr " + firstName + " " + lastName);
        doctorStatus.setText("Available");
        doctorStatus.setTextColor(getResources().getColor(R.color.NeonGreen));

        // Setup ViewModel observers
        setupViewModelObservers();

        // Fetch current mode
        viewModel.fetchDoctorMode(doctorId);

        // Profile picture click listener
        imageViewProfile.setOnClickListener(v -> {
            DoctorProfileFragment profileFragment = new DoctorProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, profileFragment)
                    .addToBackStack("doctor_profile")
                    .commit();
            hideMainContent();
            // After initializing viewModel and setting up views
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            showToast("Failed to get FCM token");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        if (doctorId != 0 && token != null && !token.isEmpty()) {
                            viewModel.updateDoctorFcmToken((int) doctorId, token);
                        }
                    });
        });

        // Initialize action grid options
        consultationOption = findViewById(R.id.consultationButton);
        homeVisitOption = findViewById(R.id.homeVisitButton);
        emergencyOption = findViewById(R.id.emergencyButton);
        dndOption = findViewById(R.id.dndButton);

        // Set click listeners for mode changes
        consultationOption.setOnClickListener(v ->
                viewModel.updateDoctorMode(doctorId, WorkingMode.CONSULTATION));

        homeVisitOption.setOnClickListener(v -> {
            // 1. Update the doctor's mode
            viewModel.updateDoctorMode(doctorId, WorkingMode.HOME_VISIT);

            // 2. Hide the action grid if it's visible
            View actionGrid = findViewById(R.id.action_grid_container);
            if (actionGrid != null) {
                actionGrid.setVisibility(View.GONE);
            }

            // 3. Launch the HomeVisitFragment
            HomeVisitFragment homeVisitFragment = new HomeVisitFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeVisitFragment)
                    .addToBackStack(null)
                    .commit();
        });



        emergencyOption.setOnClickListener(v ->
                viewModel.updateDoctorMode(doctorId, WorkingMode.EMERGENCY));

        dndOption.setOnClickListener(v -> {
            // Update the view model (if needed)
            viewModel.updateDoctorMode(doctorId, WorkingMode.DND);
            // Hide action grid
            View actionGrid = findViewById(R.id.action_grid_container);
            if (actionGrid != null) {
                actionGrid.setVisibility(View.GONE);
            }
            // Launch the DndFragment
            DndFragment dndFragment = new DndFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, dndFragment) // Replace with your container ID
                    .addToBackStack(null)
                    .commit();
        });

        loadProfilePicture(profilePic);

        // Bottom Navigation setup
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_accueil);
        }
    }

    private void setupViewModelObservers() {
        viewModel.getCurrentMode().observe(this, this::updateModeUI);

        viewModel.getErrorMessage().observe(this, error ->
                showToast("Error: " + error));

        viewModel.getCurrentMode().observe(this, success -> {

                showToast("Mode updated successfully");

        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            // You can show/hide a progress bar here if needed
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_accueil) {
                    showMainContent();
                    return true;
                } else if (itemId == R.id.nav_rdv) {
                    View actionGrid = findViewById(R.id.action_grid_container);
                    if (actionGrid != null) {
                        actionGrid.setVisibility(View.GONE);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new DoctorCalendarFragment())
                            .commit();
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
    private void updateModeUI(WorkingMode mode) {
        if (mode == null) return;

        switch (mode) {
            case CONSULTATION:
                doctorStatus.setText("In Consultation");
                doctorStatus.setTextColor(getResources().getColor(R.color.Cream));
                break;
            case HOME_VISIT:
                doctorStatus.setText("On Home Visit");
                doctorStatus.setTextColor(getResources().getColor(R.color.salt_blue));
                break;
            case EMERGENCY:
                doctorStatus.setText("Emergency");
                doctorStatus.setTextColor(getResources().getColor(R.color.crayola));
                break;
            case DND:
                doctorStatus.setText("Do Not Disturb");
                doctorStatus.setTextColor(getResources().getColor(R.color.PersianGreen));
                break;
            case NORMAL:
            default:
                doctorStatus.setText("Available");
                doctorStatus.setTextColor(getResources().getColor(R.color.NeonGreen));
                break;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri data = intent.getData();
        if (data != null && data.getScheme().equals("your_redirect_scheme")) {
            String code = data.getQueryParameter("code");
            String state = data.getQueryParameter("state");

            // Find your fragment and pass the callback
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("doctor_calendar");
            if (fragment instanceof DoctorCalendarFragment) {
                ((DoctorCalendarFragment) fragment).handleOAuthCallback(code, state);
            }
        }
    }
}
