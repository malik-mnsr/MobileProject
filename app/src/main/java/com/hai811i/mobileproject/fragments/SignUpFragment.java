package com.hai811i.mobileproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hai811i.mobileproject.ChoixActivity;
import com.hai811i.mobileproject.DoctorActivity;
import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.api.RetrofitClient;
import com.hai811i.mobileproject.implementation.AppointmentRepositoryImpl;
import com.hai811i.mobileproject.implementation.GoogleCalendarRepositoryImpl;
import com.hai811i.mobileproject.implementation.PatientRepositoryImpl;
import com.hai811i.mobileproject.implementation.SlotRepositoryImpl;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.request.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.implementation.DoctorRepositoryImpl;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.utils.ProjectViewModelFactory;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MultipartBody;

public class SignUpFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private MultipartBody.Part profilePicturePart;
    private String profilePictureBase64;
    private String profilePictureContentType;
    private TextInputEditText editTextFirstName, editTextLastName, editTextAge,
            editTextEmail, editTextSpecialty, editTextPhone;
    private MaterialButton signUpButton;
    private ImageView imageViewProfile;
    private ProjectViewModel signUpViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextSpecialty = view.findViewById(R.id.editTextSpecialty);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        signUpButton = view.findViewById(R.id.signup_button);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);

        // Initialize ViewModel
        GoogleCalendarRepository googleCalendarRepository = new GoogleCalendarRepositoryImpl(RetrofitClient.getApiService());
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl(RetrofitClient.getApiService());
        PatientRepository patientRepository = new PatientRepositoryImpl(RetrofitClient.getApiService());
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(RetrofitClient.getApiService());
        SlotRepository slotRepository = new SlotRepositoryImpl(RetrofitClient.getApiService());
        signUpViewModel = new ViewModelProvider(this,
                new ProjectViewModelFactory(doctorRepository,patientRepository, slotRepository,appointmentRepository,googleCalendarRepository)).get(ProjectViewModel.class);

        // Set up observers
        setupObservers();

        // Handle back button click
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof ChoixActivity) {
                ((ChoixActivity) getActivity()).showMainContent();
            }
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Handle sign up button click
        signUpButton.setOnClickListener(v -> attemptSignUp());

        // Handle upload photo button click
        view.findViewById(R.id.buttonUploadPhoto).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
            Toast.makeText(getContext(), "Photo upload would be implemented here", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        // Observe sign up success
        signUpViewModel.getDoctor().observe(getViewLifecycleOwner(), doctor -> {
            if (doctor != null) {
                signUpButton.setEnabled(true);
                signUpButton.setText("Sign Up");
                saveUserDataAndNavigate(doctor);
            }
        });

        // Observe errors
        signUpViewModel.getDoctorError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                signUpButton.setEnabled(true);
                signUpButton.setText("Sign Up");
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptSignUp() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String specialty = editTextSpecialty.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // Validate inputs
        if (firstName.isEmpty()) {
            editTextFirstName.setError("First name is required");
            return;
        }
        if (lastName.isEmpty()) {
            editTextLastName.setError("Last name is required");
            return;
        }
        if (ageStr.isEmpty()) {
            editTextAge.setError("Age is required");
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (specialty.isEmpty()) {
            editTextSpecialty.setError("Specialty is required");
            return;
        }
        if (phone.isEmpty()) {
            editTextPhone.setError("Phone is required");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            editTextAge.setError("Please enter a valid age");
            return;
        }

        // Create doctor object
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setAge(age);
        doctor.setEmail(email);
        doctor.setSpecialty(specialty);
        doctor.setPhone(phone);
        doctor.setCurrentMode("NORMAL"); // Default mode

        DoctorRequestWithBase64 request = new DoctorRequestWithBase64();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setAge(Integer.parseInt(ageStr));
        request.setEmail(email);
        request.setSpecialty(specialty);
        request.setPhone(phone);
        request.setCurrentMode("NORMAL");

        // Add Base64 image if available
        if (selectedImageUri != null) {
            try {
                String base64Image = convertImageToBase64(selectedImageUri);
                request.setProfilePictureBase64(base64Image);
            } catch (IOException e) {
                Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Disable button during request
        signUpButton.setEnabled(false);
        signUpButton.setText("Creating account...");


        signUpViewModel.createDoctorWithPictureBase64(request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewProfile.setImageURI(selectedImageUri); // Preview image
            Toast.makeText(getContext(), "Photo selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDataAndNavigate(Doctor doctor) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("doctor_id", doctor.getId());
        editor.putString("doctor_firstName", doctor.getFirstName());
        editor.putString("doctor_lastName", doctor.getLastName());
        editor.putInt("doctor_age", doctor.getAge());
        editor.putString("doctor_email", doctor.getEmail());
        editor.putString("doctor_specialty", doctor.getSpecialty());
        editor.putString("doctor_phone", doctor.getPhone());
        editor.putString("doctor_profilePicture", doctor.getProfilePicture());
        editor.putString("doctor_profilePictureContentType", doctor.getProfilePictureContentType());
        editor.putString("doctor_currentMode", doctor.getCurrentMode());
        editor.putBoolean("is_logged_in", true);

        editor.apply();

        navigateToMainScreen();
    }



    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    private String getRealPathFromURI(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }

        if (result == null) {
            result = getFileFromUri(uri).getAbsolutePath();
        }

        return result;
    }

    private File getFileFromUri(Uri uri) {
        File file = null;
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            String fileName = "temp_profile_image_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(requireContext().getCacheDir(), fileName);
            FileOutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
            file = tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    private String convertImageToBase64(Uri imageUri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Compress the image first (recommended for mobile)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream); // 70% quality
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String mimeType = requireContext().getContentResolver().getType(imageUri);

        return "data:" + mimeType + ";base64," + base64;
    }
                private void navigateToMainScreen () {
                    Intent intent = new Intent(getActivity(), DoctorActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
