package com.hai811i.mobileproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hai811i.mobileproject.ChoixActivity;
import com.hai811i.mobileproject.DoctorActivity;
import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.api.RetrofitClient;
import com.hai811i.mobileproject.implementation.AppointmentRepositoryImpl;
import com.hai811i.mobileproject.implementation.DoctorRepositoryImpl;
import com.hai811i.mobileproject.implementation.GoogleCalendarRepositoryImpl;
import com.hai811i.mobileproject.implementation.PatientRepositoryImpl;
import com.hai811i.mobileproject.implementation.SlotRepositoryImpl;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.utils.ProjectViewModelFactory;

import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.response.LoginResponse;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;
public class SignInFragment extends Fragment {

    private TextInputEditText editTextEmail, editTextPassword;
    private MaterialButton loginButton;
    private ProjectViewModel signInViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_2, container, false);

        // Initialize views
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        loginButton = view.findViewById(R.id.login_button);
        GoogleCalendarRepository googleCalendarRepository = new GoogleCalendarRepositoryImpl(RetrofitClient.getApiService());
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl(RetrofitClient.getApiService());
        PatientRepository patientRepository = new PatientRepositoryImpl(RetrofitClient.getApiService());
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(RetrofitClient.getApiService());
        SlotRepository slotRepository = new SlotRepositoryImpl(RetrofitClient.getApiService());


        signInViewModel = new ViewModelProvider(this, new ProjectViewModelFactory(doctorRepository,patientRepository, slotRepository,appointmentRepository,googleCalendarRepository)).get(ProjectViewModel.class);

        // Handle back button click
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof ChoixActivity) {
                ((ChoixActivity) getActivity()).showMainContent();
            }
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Handle login button click
        loginButton.setOnClickListener(v -> attemptLogin());

        // Handle create account button click
        MaterialButton createAccountButton = view.findViewById(R.id.signup_redirect_button);
        createAccountButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SignUpFragment());
            requireActivity().getSupportFragmentManager().popBackStack();
            transaction.addToBackStack("sign_up_fragment");
            transaction.commit();
        });

        return view;
    }

    private void attemptLogin() {
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            return;
        }

        if (phone.isEmpty()) {
            editTextPassword.setError("Phone number is required");
            return;
        }

        // Call handleLogin if validation passes
        handleLogin(email, phone);
    }

    private void handleLogin(String email, String phone) {
        // Disable button during request
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Observe ViewModel LiveData
        signInViewModel.getLoginResponse().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                loginButton.setEnabled(true);
                loginButton.setText("Secure Login");

                if (response.isSuccess()) {
                    saveUserData(response);
                    navigateToMainScreen();
                } else {
                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        signInViewModel.getLoginError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                loginButton.setEnabled(true);
                loginButton.setText("Secure Login");
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Trigger the login
        signInViewModel.loginDoctor(email, phone);
    }

    private void saveUserData(LoginResponse response) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("doctor_id", response.getDoctor().getId());
        editor.putString("doctor_firstName", response.getDoctor().getFirstName());
        editor.putString("doctor_lastName", response.getDoctor().getLastName());
        editor.putInt("doctor_age", response.getDoctor().getAge());
        editor.putString("doctor_email", response.getDoctor().getEmail());
        editor.putString("doctor_specialty", response.getDoctor().getSpecialty());
        editor.putString("doctor_phone", response.getDoctor().getPhone());
        editor.putString("doctor_profilePicture", response.getDoctor().getProfilePicture());
        editor.putString("doctor_profilePictureContentType", response.getDoctor().getProfilePictureContentType());
        editor.putString("doctor_currentMode", response.getDoctor().getCurrentMode());
        editor.putBoolean("is_logged_in", true);

        editor.apply();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(getActivity(), DoctorActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}