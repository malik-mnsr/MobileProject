package com.hai811i.mobileproject.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.PatientRequestWithBase64;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.entity.Patient;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class AppointmentDetailsFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1002;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final int PERMISSION_REQUEST_CAMERA = 3;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private ProjectViewModel projectViewModel;

    private ImageView ivDoctorPhoto, ivPatientPhoto;
    private TextView tvDoctorName, tvDoctorSpecialty, tvAppointmentDate, tvAppointmentTime, tvDetectedAddress;
    private EditText etFirstName, etLastName, etEmail, etAge, etPhone;
    private Button btnGetLocation, btnSubmitRequest, btnUploadPhoto, btnTakePhoto;

    private String appointmentDate, appointmentTime;
    private String patientPhotoBase64 = "";
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private Long doctorId;
    private Long slotId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        if (getArguments() != null) {
            doctorId = getArguments().getLong("doctorId");
            slotId = getArguments().getLong("slotId");
            appointmentDate = getArguments().getString("appointmentDate");
            appointmentTime = getArguments().getString("appointmentTime");
            // Fetch doctor data using ViewModel
            projectViewModel.getDoctor(Math.toIntExact(doctorId));
        }
    }

    public static AppointmentDetailsFragment newInstance(Long doctorId, Long selectedSlot, String appointmentDate, String appointmentTime) {
        AppointmentDetailsFragment fragment = new AppointmentDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("doctorId", doctorId);
        args.putLong("slotId", selectedSlot);
        args.putString("appointmentDate", appointmentDate);
        args.putString("appointmentTime", appointmentTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_request, container, false);
        initViews(view);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        geocoder = new Geocoder(requireContext(), Locale.getDefault());

        tvAppointmentDate.setText(appointmentDate);
        tvAppointmentTime.setText(appointmentTime);

        setupButtonListeners();
        setupViewModelObservers();
        return view;
    }

    private void setupViewModelObservers() {
        // Observe doctor data
        projectViewModel.getDoctor().observe(getViewLifecycleOwner(), doctor -> {
            if (doctor != null) {
                tvDoctorName.setText(doctor.getFirstName());
                tvDoctorSpecialty.setText(doctor.getSpecialty());
                loadDoctorPhoto(doctor.getProfilePicture());
            }
        });

        // Observe doctor loading error
        projectViewModel.getDoctorError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Error loading doctor: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe patient creation
        // In your fragment's setupViewModelObservers()
        projectViewModel.getBase64CreatedPatient().observe(getViewLifecycleOwner(), patient -> {
            if (patient != null) {
                navigateWithBasicInfo(
                        (long)patient.getId(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getAge()
                );
                projectViewModel.clearBase64CreatedPatient(); // Clear after use
            }
        });

        projectViewModel.getOperationError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        projectViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            btnSubmitRequest.setEnabled(!isLoading);
        });
    }

    private void clearForm() {
        etFirstName.setText("");
        etLastName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etAge.setText("");
        tvDetectedAddress.setText("");
        ivPatientPhoto.setImageResource(R.drawable.ic_default_profile);
        patientPhotoBase64 = "";
    }

    private void loadDoctorPhoto(String doctorPhotoUrl) {
        if (doctorPhotoUrl != null && !doctorPhotoUrl.isEmpty()) {
            try {
                String base64Data = doctorPhotoUrl.contains(",")
                        ? doctorPhotoUrl.split(",")[1]
                        : doctorPhotoUrl;

                byte[] decodedString = Base64.decode(base64Data, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivDoctorPhoto.setImageBitmap(decodedByte != null ? decodedByte :
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_profile));
            } catch (Exception e) {
                e.printStackTrace();
                ivDoctorPhoto.setImageResource(R.drawable.ic_default_profile);
            }
        } else {
            ivDoctorPhoto.setImageResource(R.drawable.ic_default_profile);
        }
    }

    private void setupButtonListeners() {
        btnUploadPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_STORAGE);
            } else {
                openImagePicker();
            }
        });

        btnTakePhoto.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                return;
            }
            openCamera();
        });

        btnGetLocation.setOnClickListener(v -> getCurrentLocation());
        btnSubmitRequest.setOnClickListener(v -> submitAppointmentRequest());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(getContext(), "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        // Move geocoding to background thread
                        new Thread(() -> {
                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1);

                                requireActivity().runOnUiThread(() -> {
                                    if (!addresses.isEmpty()) {
                                        Address address = addresses.get(0);
                                        String fullAddress = address.getAddressLine(0);
                                        tvDetectedAddress.setText(fullAddress);
                                    } else {
                                        tvDetectedAddress.setText("No address found");
                                    }
                                });
                            } catch (IOException e) {
                                requireActivity().runOnUiThread(() -> {
                                    tvDetectedAddress.setText("Failed to get address");
                                    Toast.makeText(getContext(), "Geocoder service unavailable", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }).start();
                    } else {
                        tvDetectedAddress.setText("Location not available");
                    }
                })
                .addOnFailureListener(e -> {
                    tvDetectedAddress.setText("Failed to get location");
                });
    }

    private void submitAppointmentRequest() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String address = tvDetectedAddress.getText().toString();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || ageStr.isEmpty() || address.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            etAge.setError("Please enter a valid age");
            return;
        }
        Doctor doctor = projectViewModel.getDoctor().getValue();
        // Create patient request
        PatientRequestWithBase64 request = new PatientRequestWithBase64();
        request.setDoctorId(doctor);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPhone(phone);
        request.setAge(age);
        request.setAddress(address);
        request.setProfilePictureBase64(patientPhotoBase64);

        // Store patient in database
        projectViewModel.createPatientWithBase64(request);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = null;

            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().getContentResolver(),
                            selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            }

            if (bitmap != null) {
                ivPatientPhoto.setImageBitmap(bitmap);
                patientPhotoBase64 = convertBitmapToBase64(bitmap);
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSION_REQUEST_LOCATION:
                    getCurrentLocation();
                    break;
                case PERMISSION_REQUEST_STORAGE:
                    openImagePicker();
                    break;
                case PERMISSION_REQUEST_CAMERA:
                    openCamera();
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews(View view) {
        ivDoctorPhoto = view.findViewById(R.id.iv_doctor_photo);
        tvDoctorName = view.findViewById(R.id.tv_doctor_name);
        tvDoctorSpecialty = view.findViewById(R.id.tv_doctor_specialty);
        tvAppointmentDate = view.findViewById(R.id.tv_appointment_date);
        tvAppointmentTime = view.findViewById(R.id.tv_appointment_time);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etAge = view.findViewById(R.id.et_age);
        tvDetectedAddress = view.findViewById(R.id.tv_detected_address);
        btnGetLocation = view.findViewById(R.id.btn_get_location);
        btnSubmitRequest = view.findViewById(R.id.btn_submit_request);
        ivPatientPhoto = view.findViewById(R.id.iv_patient_photo);
        btnUploadPhoto = view.findViewById(R.id.btn_upload_photo);
        btnTakePhoto = view.findViewById(R.id.btn_take_photo);
    }

    private void navigateWithBasicInfo(Long patientId, String firstName, String lastName, int age) {
        AppointmentConfirmationFragment fragment = AppointmentConfirmationFragment.newInstance(
                doctorId,
                slotId,
                patientId,
                appointmentDate,
                appointmentTime,
                firstName,
                lastName,
                age
        );

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


}