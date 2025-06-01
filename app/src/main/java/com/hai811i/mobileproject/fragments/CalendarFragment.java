package com.hai811i.mobileproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
    private SlotDTO selectedSlot;
    private Button bookbtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        selectedDate = Calendar.getInstance();
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
        currentDoctorId = selectedDoctor.getId();

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

        // Initialize ViewModel

        viewModel = new ViewModelProvider(this,
                new ProjectViewModelFactory(
                        doctorRepository, // doctorRepository
                        patientRepository, // patientRepository
                        slotRepository,
                        appointmentRepository, // appointmentRepository
                        googleCalendarRepository  // googleCalendarRepository
                        ,medicalRecordRepository
                        ,prescriptionsRepository,
                        notificationRepository,
                        drugRepository
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
        Button bookButton = view.findViewById(R.id.btn_book_appointment);
        bookButton.setOnClickListener(v -> bookAppointment());

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
            // Create a new Calendar instance for the selected date
            selectedDate = (Calendar) currentCalendar.clone();
            selectedDate.set(Calendar.DAY_OF_MONTH, day);

            // Clear any previous slot selection
            selectedSlot = null;

            // Load time slots for selected date
            loadTimeSlots();

            // Refresh calendar to reflect the new selection
            updateCalendar();

            // Show feedback that date was selected
            try {
                String dateString = new SimpleDateFormat("EEE, MMM d", Locale.getDefault())
                        .format(selectedDate.getTime());
                Toast.makeText(requireContext(),
                        "Selected date: " + dateString,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("CalendarFragment", "Error formatting date", e);
            }
        });

        calendarGrid.addView(dayButton);
    }
    private void loadTimeSlots() {
        if (currentDoctorId == -1) {
            Toast.makeText(requireContext(), "Doctor not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // This should never be null now, but just in case:
        if (selectedDate == null) {
            selectedDate = Calendar.getInstance();
            Log.w("CalendarFragment", "selectedDate was null, defaulting to today");
        }

        // Format date as yyyy-MM-dd
        String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DAY_OF_MONTH));

        Log.d("CalendarFragment", "Loading slots for: " + dateString);
        viewModel.getFreeSlots(currentDoctorId, LocalDate.parse(dateString));
    }

    private void observeViewModel() {
        viewModel.getAvailableSlots().observe(getViewLifecycleOwner(), slots -> {
            timeSlotsGrid.removeAllViews();

            if (slots != null && !slots.isEmpty()) {
                for (SlotDTO slot : slots) {
                    addTimeSlotView(slot);
                }
                updateSlotButtons(); // Update all buttons after loading
            } else {
                Toast.makeText(requireContext(), "No available slots for this date", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // You can show/hide progress bar here if needed
        });
    }

    private void addTimeSlotView(SlotDTO slot) {
        Button slotButton = new Button(requireContext());

        // Store the slot object with the button
        slotButton.setTag(slot);

        // Format the time display
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
        String timeText = slot.getStartAt().format(timeFormatter) + " - " +
                slot.getEndAt().format(timeFormatter);

        slotButton.setText(timeText);

        // Set fixed width for each time slot button
        int buttonWidth = getResources().getDimensionPixelSize(R.dimen.time_slot_width);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = buttonWidth;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.setMargins(8, 8, 8, 8);

        slotButton.setLayoutParams(params);

        // Set initial background (will be updated by updateSlotButtons)
        slotButton.setBackgroundResource(R.drawable.time_slot_bg);
        slotButton.setTextColor(getResources().getColor(R.color.black));

        slotButton.setOnClickListener(v -> {
            selectedSlot = slot;
            updateSlotButtons(); // Update all buttons when one is clicked

            try {
                String dateString = new SimpleDateFormat("EEE, MMM d", Locale.getDefault())
                        .format(selectedDate.getTime());
                Toast.makeText(requireContext(),
                        "Selected: " + dateString + " at " + timeText,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("CalendarFragment", "Error showing selection", e);
            }
        });

        timeSlotsGrid.addView(slotButton);

        // Set initial selection state
        if (selectedSlot != null && selectedSlot.getId() == slot.getId()) {
            slotButton.setBackgroundResource(R.drawable.time_slot_selected_bg);
            slotButton.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private void refreshAllTimeSlotViews() {
        // Store the current scroll position
        int scrollX = timeSlotsGrid.getScrollX();

        // Force a layout pass to ensure all views are created
        timeSlotsGrid.requestLayout();

        // After layout pass, update all buttons
        timeSlotsGrid.post(() -> {
            for (int i = 0; i < timeSlotsGrid.getChildCount(); i++) {
                View child = timeSlotsGrid.getChildAt(i);
                if (child instanceof Button) {
                    Button button = (Button) child;
                    // Compare based on slot time rather than ID
                    String buttonText = button.getText().toString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
                    String selectedTime = selectedSlot != null ?
                            selectedSlot.getStartAt().format(formatter) : "";

                    if (selectedSlot != null && buttonText.contains(selectedTime)) {
                        button.setBackgroundResource(R.drawable.time_slot_selected_bg);
                        button.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        button.setBackgroundResource(R.drawable.time_slot_bg);
                        button.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
            // Restore scroll position
            timeSlotsGrid.scrollTo(scrollX, 0);
        });
    }
    private void clearTimeSlots() {
        selectedSlot = null;
        timeSlotsGrid.removeAllViews();
    }

    public static CalendarFragment newInstance(Doctor doctor) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putSerializable("doctor", doctor);
        fragment.setArguments(args);
        return fragment;
    }
    private void bookAppointment() {
        if (selectedDoctor == null) {
            Toast.makeText(requireContext(), "No doctor selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Please select a date first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSlot == null) {
            Toast.makeText(requireContext(), "Please select a time slot", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Format date
            String appointmentDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH) + 1,
                    selectedDate.get(Calendar.DAY_OF_MONTH));

            // Format time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
            String appointmentTime = selectedSlot.getStartAt().format(timeFormatter);

            // Navigate to AppointmentDetailsFragment
            AppointmentDetailsFragment fragment = AppointmentDetailsFragment.newInstance(
                    Long.valueOf(selectedDoctor.getId()),
                    selectedSlot.getId(),
                    appointmentDate,
                    appointmentTime

            );

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to create appointment", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSlotButtons() {
        for (int i = 0; i < timeSlotsGrid.getChildCount(); i++) {
            View child = timeSlotsGrid.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;

                // Get the slot object associated with this button
                SlotDTO slot = (SlotDTO) button.getTag();

                if (selectedSlot != null && selectedSlot.getId() == slot.getId()) {
                    button.setBackgroundResource(R.drawable.time_slot_selected_bg);
                    button.setTextColor(getResources().getColor(R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.time_slot_bg);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }
        }
    }

}