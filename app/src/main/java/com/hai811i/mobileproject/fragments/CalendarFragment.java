package com.hai811i.mobileproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.api.RetrofitClient;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.implementation.SlotRepositoryImpl;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.utils.ProjectViewModelFactory;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private GridLayout calendarGrid;
    private GridLayout timeSlotsGrid;
    private TextView monthYearText;
    private ImageButton prevMonthBtn, nextMonthBtn;
    private ImageView ivDoctor;
    private TextView tvDoctorName, tvSpecialty;

    private Calendar currentCalendar;
    private Calendar selectedDate;
    private Doctor selectedDoctor;
    private ProjectViewModel viewModel;
    private Long currentDoctorId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize views
        calendarGrid = view.findViewById(R.id.calendar_grid);
        timeSlotsGrid = view.findViewById(R.id.time_slots_grid);
        monthYearText = view.findViewById(R.id.tv_month_year);
        prevMonthBtn = view.findViewById(R.id.btn_prev_month);
        nextMonthBtn = view.findViewById(R.id.btn_next_month);

        // Doctor info views
        ivDoctor = view.findViewById(R.id.iv_doctor_photo);
        tvDoctorName = view.findViewById(R.id.tv_doctor_name);
        tvSpecialty = view.findViewById(R.id.tv_doctor_specialty);

        // Get doctor from arguments
        if (getArguments() != null) {
            selectedDoctor = (Doctor) getArguments().getSerializable("doctor");
            displayDoctorInfo();
        }

        // Get doctor ID from shared preferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentDoctorId = sharedPreferences.getLong("doctor_id", -1);

        // Initialize ViewModel
        SlotRepository slotRepository = new SlotRepositoryImpl(RetrofitClient.getApiService());
        viewModel = new ViewModelProvider(this,
                new ProjectViewModelFactory(
                        null, // doctorRepository
                        null, // patientRepository
                        slotRepository,
                        null, // appointmentRepository
                        null  // googleCalendarRepository
                )).get(ProjectViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize with current month
        currentCalendar = Calendar.getInstance();
        setupCalendar();

        // Set up observers
        observeViewModel();

        prevMonthBtn.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        nextMonthBtn.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });
    }

    private void displayDoctorInfo() {
        if (selectedDoctor != null) {
            tvDoctorName.setText(selectedDoctor.getFirstName());
            tvSpecialty.setText(selectedDoctor.getSpecialty());

            String base64Image = selectedDoctor.getProfilePicture();
            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    String base64Data = base64Image.contains(",")
                            ? base64Image.split(",")[1]
                            : base64Image;
                    byte[] decodedString = Base64.decode(base64Data, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivDoctor.setImageBitmap(decodedByte != null ? decodedByte :
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_profile));
                } catch (Exception e) {
                    e.printStackTrace();
                    ivDoctor.setImageResource(R.drawable.ic_default_profile);
                }
            } else {
                ivDoctor.setImageResource(R.drawable.ic_default_profile);
            }
        }
    }

    private void setupCalendar() {
        updateMonthYearDisplay();
        populateCalendar();
    }

    private void updateCalendar() {
        updateMonthYearDisplay();
        populateCalendar();
        clearTimeSlots();
    }

    private void updateMonthYearDisplay() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(monthFormat.format(currentCalendar.getTime()));
    }

    private void populateCalendar() {
        calendarGrid.removeAllViews();

        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Add empty views for days before the first day of the month
        for (int i = 1; i < firstDayOfWeek; i++) {
            addEmptyDayToCalendar();
        }

        // Add day buttons
        for (int day = 1; day <= daysInMonth; day++) {
            addDayButtonToCalendar(day);
        }
    }

    private void addEmptyDayToCalendar() {
        View emptyView = new View(requireContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = getResources().getDimensionPixelSize(R.dimen.calendar_day_size);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        emptyView.setLayoutParams(params);
        calendarGrid.addView(emptyView);
    }

    private void addDayButtonToCalendar(final int day) {
        Button dayButton = new Button(requireContext());
        dayButton.setText(String.valueOf(day));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = getResources().getDimensionPixelSize(R.dimen.calendar_day_size);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        dayButton.setLayoutParams(params);

        // Style the button
        dayButton.setBackgroundResource(R.drawable.calendar_day_bg);

        // Highlight today's date
        Calendar today = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day == today.get(Calendar.DAY_OF_MONTH)) {
            dayButton.setBackgroundResource(R.drawable.calendar_today_bg);
        }

        // Highlight the selected date
        if (selectedDate != null && selectedDate.get(Calendar.DAY_OF_MONTH) == day &&
                selectedDate.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                selectedDate.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
            dayButton.setBackgroundResource(R.drawable.calendar_selected_bg);
            dayButton.setTextColor(getResources().getColor(R.color.white));
        }

        dayButton.setOnClickListener(v -> {
            // Store selected date
            selectedDate = (Calendar) currentCalendar.clone();
            selectedDate.set(Calendar.DAY_OF_MONTH, day);

            // Load time slots for selected date
            loadTimeSlots();
            updateCalendar(); // Refresh calendar to reflect the new selection
        });

        calendarGrid.addView(dayButton);
    }

    private void loadTimeSlots() {
        if (currentDoctorId == -1 || selectedDate == null) {
            Toast.makeText(requireContext(), "Doctor not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format date as yyyy-MM-dd
        String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DAY_OF_MONTH));

        viewModel.getFreeSlots(currentDoctorId, LocalDate.parse(dateString));
    }

    private void observeViewModel() {
        viewModel.getAvailableSlots().observe(getViewLifecycleOwner(), slots -> {
            timeSlotsGrid.removeAllViews();

            if (slots != null && !slots.isEmpty()) {
                for (SlotDTO slot : slots) {
                    addTimeSlotView(slot);
                }
            } else {
                Toast.makeText(requireContext(), "No available slots for this date", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSlotError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // You can show/hide progress bar here if needed
        });
    }

    private void addTimeSlotView(SlotDTO slot) {
        Button slotButton = new Button(requireContext());

        // Format the time display
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
        String timeText = slot.getStartAt().format(timeFormatter) + " - " +
                slot.getEndAt().format(timeFormatter);

        slotButton.setText(timeText);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        slotButton.setLayoutParams(params);

        slotButton.setBackgroundResource(R.drawable.time_slot_bg);

        slotButton.setOnClickListener(v -> {
            // Handle slot selection
            String message = String.format("Selected: %s at %s",
                    selectedDate.getTime().toString(),
                    timeText);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        timeSlotsGrid.addView(slotButton);
    }

    private void clearTimeSlots() {
        timeSlotsGrid.removeAllViews();
        selectedDate = null;
    }

    public static CalendarFragment newInstance(Doctor doctor) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putSerializable("doctor", doctor);
        fragment.setArguments(args);
        return fragment;
    }
}