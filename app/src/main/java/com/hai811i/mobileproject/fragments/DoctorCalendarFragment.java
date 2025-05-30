package com.hai811i.mobileproject.fragments;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.api.RetrofitClient;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.implementation.AppointmentRepositoryImpl;
import com.hai811i.mobileproject.implementation.DoctorRepositoryImpl;
import com.hai811i.mobileproject.implementation.GoogleCalendarRepositoryImpl;
import com.hai811i.mobileproject.implementation.MedicalRecordRepositoryImpl;
import com.hai811i.mobileproject.implementation.PatientRepositoryImpl;
import com.hai811i.mobileproject.implementation.SlotRepositoryImpl;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.MedicalRecordRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.utils.ProjectViewModelFactory;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class DoctorCalendarFragment extends Fragment {
    private Calendar currentCalendar;
    private TextView monthYearText;
    private GridLayout calendarGrid;
    private GridLayout timeSlotsGrid;
    private Button btnAddTimeSlot, btnSaveSlots;
    private ProjectViewModel viewModel;
    private Long currentDoctorId;
    private int selectedDay = -1;
    private List<SlotCreateDTO> slotsToCreate = new ArrayList<>();
    private Button btnConnectGoogleCalendar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get doctor ID from shared preferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentDoctorId = sharedPreferences.getLong("doctor_id", -1);

        // Initialize ViewModel
        GoogleCalendarRepository googleCalendarRepository = new GoogleCalendarRepositoryImpl(RetrofitClient.getApiService());
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl(RetrofitClient.getApiService());
        PatientRepository patientRepository = new PatientRepositoryImpl(RetrofitClient.getApiService());
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(RetrofitClient.getApiService());
        SlotRepository slotRepository = new SlotRepositoryImpl(RetrofitClient.getApiService());
        MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepositoryImpl(RetrofitClient.getApiService());

        viewModel = new ViewModelProvider(this,
                new ProjectViewModelFactory(
                        doctorRepository,
                        patientRepository,
                        slotRepository,
                        appointmentRepository,
                        googleCalendarRepository,
                        medicalRecordRepository
                )
        ).get(ProjectViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_calendar, container, false);

        // Initialize views
        monthYearText = view.findViewById(R.id.tv_month_year);
        calendarGrid = view.findViewById(R.id.calendar_grid);
        timeSlotsGrid = view.findViewById(R.id.time_slots_grid);
        btnAddTimeSlot = view.findViewById(R.id.btn_add_time_slot);
        btnSaveSlots = view.findViewById(R.id.btn_save_slots);

        // Set current month
        currentCalendar = Calendar.getInstance();
        updateMonthYear();
        setupCalendar();

        // Month navigation
        view.findViewById(R.id.btn_prev_month).setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateMonthYear();
            setupCalendar();
        });

        view.findViewById(R.id.btn_next_month).setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateMonthYear();
            setupCalendar();
        });

        // Add time slot button
        btnAddTimeSlot.setOnClickListener(v -> showAddTimeSlotDialog());

        // Save slots button
        btnSaveSlots.setOnClickListener(v -> saveTimeSlots());

        btnConnectGoogleCalendar = view.findViewById(R.id.btn_connect_google_calendar);
        btnConnectGoogleCalendar.setOnClickListener(v -> connectGoogleCalendar());

        observeViewModel();

        return view;
    }
    private void connectGoogleCalendar() {
        if (currentDoctorId == -1) {
            Toast.makeText(getContext(), "Doctor not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.fetchGoogleAuthUrl(currentDoctorId);
    }

    private void updateMonthYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(sdf.format(currentCalendar.getTime()));
    }

    private void setupCalendar() {
        calendarGrid.removeAllViews();
        selectedDay = -1; // Reset selection when month changes

        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty views for days before the first day of month
        for (int i = 1; i < firstDayOfWeek; i++) {
            addEmptyDayToCalendar();
        }

        // Add day buttons
        for (int day = 1; day <= daysInMonth; day++) {
            addDayToCalendar(day);
        }
    }

    private void addEmptyDayToCalendar() {
        View emptyView = new View(getContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = getResources().getDimensionPixelSize(R.dimen.calendar_day_size);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        emptyView.setLayoutParams(params);
        calendarGrid.addView(emptyView);
    }

    private void addDayToCalendar(final int day) {
        Button dayButton = new Button(getContext());
        dayButton.setText(String.valueOf(day));
        dayButton.setBackgroundResource(R.drawable.calendar_day_bg);
        dayButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        // Set button dimensions
        int buttonSize = getResources().getDimensionPixelSize(R.dimen.calendar_day_size);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = buttonSize;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);
        dayButton.setLayoutParams(params);

        // Highlight today's date
        Calendar today = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day == today.get(Calendar.DAY_OF_MONTH)) {
            dayButton.setBackgroundResource(R.drawable.calendar_today_bg);
        }

        // Highlight selected date
        if (day == selectedDay) {
            dayButton.setBackgroundResource(R.drawable.calendar_selected_bg);
            dayButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        dayButton.setOnClickListener(v -> {
            // Reset previously selected day's appearance
            for (int i = 0; i < calendarGrid.getChildCount(); i++) {
                View child = calendarGrid.getChildAt(i);
                if (child instanceof Button) {
                    Button btn = (Button) child;
                    try {
                        int btnDay = Integer.parseInt(btn.getText().toString());
                        if (btnDay == selectedDay) {
                            // Reset to default or today's style
                            if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                    currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                    btnDay == today.get(Calendar.DAY_OF_MONTH)) {
                                btn.setBackgroundResource(R.drawable.calendar_today_bg);
                            } else {
                                btn.setBackgroundResource(R.drawable.calendar_day_bg);
                            }
                            btn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        }
                    } catch (NumberFormatException e) {
                        // Ignore non-day buttons
                    }
                }
            }

            // Set new selected day
            selectedDay = day;
            dayButton.setBackgroundResource(R.drawable.calendar_selected_bg);
            dayButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

            // Load time slots for selected day
            loadTimeSlotsForDay(day);
        });

        calendarGrid.addView(dayButton);
    }

    private void showAddTimeSlotDialog() {
        if (selectedDay == -1) {
            Toast.makeText(getContext(), "Please select a date first", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Time Slot");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_time_slot, null);
        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(false);

        builder.setView(dialogView);
        builder.setPositiveButton("Add", (dialog, which) -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Create proper LocalDateTime objects
            LocalDateTime startAt = LocalDateTime.of(
                    currentCalendar.get(Calendar.YEAR),
                    currentCalendar.get(Calendar.MONTH) + 1,
                    selectedDay,
                    hour,
                    minute
            );

            LocalDateTime endAt = startAt.plusMinutes(30); // Assuming 30-minute slots

            // Debug log the created slot
            Log.d("SlotCreation", "Creating slot from " + startAt + " to " + endAt);

            // Create SlotCreateDTO
            SlotCreateDTO slot = new SlotCreateDTO(startAt, endAt);
            slotsToCreate.add(slot);

            // Add to UI
            addTimeSlotView(formatTime(startAt, endAt));
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private String formatTime(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
        return start.format(timeFormatter) + " - " + end.format(timeFormatter);
    }

    private void addTimeSlotView(String time) {
        Button slotButton = new Button(getContext());
        slotButton.setText(time);
        slotButton.setBackgroundResource(R.drawable.time_slot_bg);
        slotButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        slotButton.setPadding(8, 4, 8, 4); // Add padding inside the button
        slotButton.setTextSize(12); // Smaller text size

        // Calculate the width to fit 3 items in a row with margins
        int margin = 8; // 8dp margin
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int totalMargins = margin * 4; // margins on both sides (left+right) for 3 items
        int itemWidth = (screenWidth - totalMargins) / 3;

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = itemWidth;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(margin, margin, margin, margin);

        // This will automatically position the slots in a grid
        int childCount = timeSlotsGrid.getChildCount();
        params.rowSpec = GridLayout.spec(childCount / 3);
        params.columnSpec = GridLayout.spec(childCount % 3);

        slotButton.setLayoutParams(params);

        slotButton.setOnLongClickListener(v -> {
            showDeleteSlotDialog(time);
            return true;
        });

        timeSlotsGrid.addView(slotButton);
    }

    private void showDeleteSlotDialog(String timeRange) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Time Slot")
                .setMessage("Delete " + timeRange + " slot?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    for (int i = 0; i < timeSlotsGrid.getChildCount(); i++) {
                        View child = timeSlotsGrid.getChildAt(i);
                        if (child instanceof Button && timeRange.equals(((Button) child).getText().toString())) {
                            timeSlotsGrid.removeViewAt(i);

                            // Parse the "HH:mm - HH:mm" format into LocalTime
                            String[] times = timeRange.split(" - ");
                            if (times.length == 2) {
                                LocalTime startTime = LocalTime.parse(times[0], DateTimeFormatter.ofPattern("HH:mm"));
                                LocalTime endTime = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("HH:mm"));

                                slotsToCreate.removeIf(slot -> {
                                    LocalTime slotStart = slot.getStartAt().toLocalTime();
                                    LocalTime slotEnd = slot.getEndAt().toLocalTime();
                                    return slotStart.equals(startTime) && slotEnd.equals(endTime);
                                });
                            }

                            break;
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void saveTimeSlots() {
        if (currentDoctorId == -1) {
            Toast.makeText(getContext(), "Doctor not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (slotsToCreate.isEmpty()) {
            Toast.makeText(getContext(), "No slots to save", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.createSlots(currentDoctorId, slotsToCreate);
    }

    private void loadTimeSlotsForDay(int day) {
        if (currentDoctorId == -1) {
            Toast.makeText(getContext(), "Doctor not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate selectedDate = LocalDate.of(
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH) + 1,
                day
        );

        viewModel.getFreeSlots(currentDoctorId, selectedDate);
    }

    private void observeViewModel() {
        viewModel.getAvailableSlots().observe(getViewLifecycleOwner(), slots -> {
            timeSlotsGrid.removeAllViews();

            if (slots == null || slots.isEmpty()) {
                TextView noSlotsText = new TextView(getContext());
                noSlotsText.setText("No available slots for this date");
                noSlotsText.setTextSize(14);
                noSlotsText.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                noSlotsText.setGravity(Gravity.CENTER);
                noSlotsText.setPadding(0, 16, 0, 16);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.MATCH_PARENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(0, 3);
                noSlotsText.setLayoutParams(params);

                timeSlotsGrid.addView(noSlotsText);
                return;
            }

            for (SlotDTO slot : slots) {
                addTimeSlotView(formatTime(slot.getStartAt(), slot.getEndAt()));
            }
        });

        viewModel.getSlotError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                Log.e("SlotError", "API Error: " + error);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            btnAddTimeSlot.setEnabled(!isLoading);
            btnSaveSlots.setEnabled(!isLoading);
        });
        viewModel.getGoogleAuthUrl().observe(getViewLifecycleOwner(), url -> {
            if (url != null) {
                openAuthUrl(url);
            }
        });
        viewModel.getGoogleOAuthResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Toast.makeText(getContext(), "Google Calendar connected successfully", Toast.LENGTH_SHORT).show();
                btnConnectGoogleCalendar.setVisibility(View.GONE);
            }
        });
        viewModel.getGoogleCalendarConnectionStatus().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected != null) {
                btnConnectGoogleCalendar.setVisibility(isConnected ? View.GONE : View.VISIBLE);
            }
        });
        viewModel.getGoogleCalendarError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("GoogleCalendar", "Google Calendar error: " + error);
                Toast.makeText(getContext(), "Google Calendar error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void handleOAuthCallback(String code, String state) {
        viewModel.handleOAuthCallback(code, state);
    }
    // Add this method to handle the auth URL
    private void openAuthUrl(String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome"); // Force Chrome
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

}