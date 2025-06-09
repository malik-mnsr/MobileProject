package com.hai811i.mobileproject.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.api.RetrofitClient;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.entity.DrugReference;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppointmentBookingFragment extends Fragment {

    private TextView tvSelectedDate, tvSelectedTime, tvDoctorsHeader;
    private Spinner specialtySpinner;
    private Button btnSearchDoctors;
    private RecyclerView recyclerDoctors;
    private DoctorAdapter doctorAdapter;

    private ProjectViewModel viewModel;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ProjectViewModelFactory factory = new ProjectViewModelFactory(doctorRepository,patientRepository, slotRepository,appointmentRepository,googleCalendarRepository, medicalRecordRepository,prescriptionsRepository, notificationRepository, drugRepository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(ProjectViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_appointment, container, false);

        initializeViews(view);
        setupSpecialtySpinner();
        setupDateTimeSelection();
        setupRecyclerView();
        observeViewModel();
//        setInitialDateTime();

        return view;
    }

    private void initializeViews(View view) {
        //  tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        // tvSelectedTime = view.findViewById(R.id.tv_selected_time);
        specialtySpinner = view.findViewById(R.id.spinner_specialty);
        btnSearchDoctors = view.findViewById(R.id.btn_search_doctors);
        tvDoctorsHeader = view.findViewById(R.id.tv_doctors_header);
        recyclerDoctors = view.findViewById(R.id.recycler_doctors);
    }

    private void setupSpecialtySpinner() {
        List<String> specialties = new ArrayList<>();
        specialties.add("General Physician");
        specialties.add("Cardiologist");
        specialties.add("Dermatologist");
        specialties.add("Pediatrician");
        specialties.add("Neurologist");
        specialties.add("Orthopedic Surgeon");
        specialties.add("Gynecologist");
        specialties.add("ENT Specialist");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,  // Default Android layout
                specialties
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Default dropdown
        specialtySpinner.setAdapter(adapter);
    }

    private void setupDateTimeSelection() {
//        tvSelectedDate.setOnClickListener(v -> showDatePicker());
        //      tvSelectedTime.setOnClickListener(v -> showTimePicker());
        btnSearchDoctors.setOnClickListener(v -> searchDoctors());
    }

    private void setupRecyclerView() {
        doctorAdapter = new DoctorAdapter(new ArrayList<>(), doctor -> {
            // Handle doctor selection
            Toast.makeText(getContext(), "Selected: " + doctor.getFirstName(), Toast.LENGTH_SHORT).show();
            // In a real app, you might navigate to a booking confirmation screen
            navigateToCalendarFragment(doctor);
        });

        recyclerDoctors.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerDoctors.setAdapter(doctorAdapter);
    }


    private void navigateToCalendarFragment(Doctor doctor) {
        // Verify doctor data before passing
        if (doctor == null) {
            Toast.makeText(requireContext(), "Doctor data is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        CalendarFragment fragment = CalendarFragment.newInstance(doctor);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("calendar")
                .commit();
    }

    private void observeViewModel() {
        viewModel.getDoctorsList().observe(getViewLifecycleOwner(), doctors -> {
            if (doctors != null && !doctors.isEmpty()) {
                tvDoctorsHeader.setVisibility(View.VISIBLE);
                recyclerDoctors.setVisibility(View.VISIBLE);
                doctorAdapter.updateDoctors(doctors);
            } else {
                Toast.makeText(getContext(), "No doctors available for the selected criteria", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getDoctorsListError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Show/hide progress bar based on loading state
            if (isLoading != null && isLoading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
            }
        });
    }

    private void setInitialDateTime() {
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);
//        updateDateTimeDisplay();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    updateDateTimeDisplay();
                },
                selectedYear, selectedMonth, selectedDay
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    updateDateTimeDisplay();
                },
                selectedHour, selectedMinute, false
        );
        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        String dateStr = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
        String timeStr = String.format("%02d:%02d", selectedHour, selectedMinute);

//        tvSelectedDate.setText(dateStr);
        //      tvSelectedTime.setText(timeStr);
    }

    private void searchDoctors() {
        String selectedSpecialty = specialtySpinner.getSelectedItem().toString();


        // Fetch doctors by specialty from ViewModel
        viewModel.getDoctorsBySpecialty(selectedSpecialty);
    }

    // Doctor adapter for RecyclerView
    private static class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

        private List<Doctor> doctors;
        private final DoctorViewHolder.OnDoctorClickListener listener;

        public DoctorAdapter(List<Doctor> doctors, DoctorViewHolder.OnDoctorClickListener listener) {
            this.doctors = doctors;
            this.listener = listener;
        }

        public void updateDoctors(List<Doctor> newDoctors) {
            this.doctors = newDoctors;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor_card, parent, false);
            return new DoctorViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            Doctor doctor = doctors.get(position);
            holder.bind(doctor, listener); // ✅ Pass both the doctor and listener
        }


        @Override
        public int getItemCount() {
            return doctors.size();
        }
        static class DoctorViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDoctor;
            TextView tvDoctorName, tvSpecialty;
            Button btnBook;

            public DoctorViewHolder(@NonNull View itemView) {
                super(itemView);
                ivDoctor = itemView.findViewById(R.id.iv_doctor);
                tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
                tvSpecialty = itemView.findViewById(R.id.tv_specialty);
                btnBook = itemView.findViewById(R.id.btn_book); // Make sure this ID matches
            }

            public void bind(Doctor doctor, OnDoctorClickListener listener) {
                tvDoctorName.setText(doctor.getFirstName());
                tvSpecialty.setText(doctor.getSpecialty());

                String base64Image = doctor.getProfilePicture();
                if (base64Image != null && !base64Image.isEmpty()) {
                    try {
                        byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                        Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ivDoctor.setImageBitmap(decodedByte);
                    } catch (Exception e) {
                        ivDoctor.setImageResource(R.drawable.circle_background);
                    }
                } else {
                    ivDoctor.setImageResource(R.drawable.ic_default_profile);
                }

                btnBook.setOnClickListener(v -> listener.onDoctorClick(doctor));
            }

            interface OnDoctorClickListener {
                void onDoctorClick(Doctor doctor);
            }
        }

    }
}