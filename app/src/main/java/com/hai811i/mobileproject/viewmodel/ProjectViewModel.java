package com.hai811i.mobileproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hai811i.mobileproject.callback.AppointmentCallback;
import com.hai811i.mobileproject.callback.AppointmentsListCallback;
import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.CalendarEventCallback;
import com.hai811i.mobileproject.callback.CalendarStatusCallback;
import com.hai811i.mobileproject.callback.DrugCallback;
import com.hai811i.mobileproject.callback.DrugListCallback;
import com.hai811i.mobileproject.callback.DrugPageCallback;
import com.hai811i.mobileproject.callback.MedicalRecordCallback;
import com.hai811i.mobileproject.callback.MedicalRecordsListCallback;
import com.hai811i.mobileproject.callback.ModeCallback;
import com.hai811i.mobileproject.callback.NotificationCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;
import com.hai811i.mobileproject.callback.PatientCallback;
import com.hai811i.mobileproject.callback.PatientsListCallback;
import com.hai811i.mobileproject.callback.PdfDownloadCallback;
import com.hai811i.mobileproject.callback.PrescriptionCallback;
import com.hai811i.mobileproject.callback.PrescriptionsListCallback;
import com.hai811i.mobileproject.callback.RawPatientCallback;
import com.hai811i.mobileproject.callback.SimpleCallback;
import com.hai811i.mobileproject.dto.AppointmentDTO;
import com.hai811i.mobileproject.dto.MedicalRecordDTO;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.PatientRequestWithBase64;
import com.hai811i.mobileproject.dto.PrescriptionDTO;
import com.hai811i.mobileproject.dto.ReserveRequest;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.callback.SlotAppointmentsCallback;
import com.hai811i.mobileproject.dto.TestNotificationRequest;
import com.hai811i.mobileproject.entity.Appointment;
import com.hai811i.mobileproject.entity.DrugReference;
import com.hai811i.mobileproject.entity.Motif;
import com.hai811i.mobileproject.entity.Page;
import com.hai811i.mobileproject.entity.Patient;
import com.hai811i.mobileproject.entity.WorkingMode;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.DrugRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.MedicalRecordRepository;
import com.hai811i.mobileproject.repository.NotificationRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
import com.hai811i.mobileproject.repository.PrescriptionsRepository;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.request.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.callback.DoctorCallback;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.callback.DoctorsListCallback;
import com.hai811i.mobileproject.callback.LoginCallback;
import com.hai811i.mobileproject.callback.ProfilePictureCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.response.LoginResponse;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProjectViewModel extends ViewModel {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarRepository googleCalendarRepository;
    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionsRepository prescriptionsRepository;
    private final NotificationRepository notificationRepository;
    private final DrugRepository drugRepository;

    // LiveData declarations
    private MutableLiveData<List<PatientDTO>> patientsLiveData = new MutableLiveData<List<PatientDTO>>();
    // LiveData for prescriptions
    private final MutableLiveData<PrescriptionDTO> prescriptionLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PrescriptionDTO>> prescriptionsListLiveData = new MutableLiveData<>();
    private final MutableLiveData<File> downloadedPdfLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> prescriptionsErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<PatientDTO> createdPatient = new MutableLiveData<>();
    private final MutableLiveData<String> authUrlLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> oauthResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationSuccessLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<MedicalRecordDTO> medicalRecordLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MedicalRecordDTO>> medicalRecordsHistoryLiveData = new MutableLiveData<>();
    private final MutableLiveData<AppointmentDTO> appointment = new MutableLiveData<>();
    private final MutableLiveData<List<AppointmentDTO>> appointmentsList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationCompleted = new MutableLiveData<>();
    private final MutableLiveData<String> operationError = new MutableLiveData<>();
    private final MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> fcmTokenUpdateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> fcmTokenUpdateError = new MutableLiveData<>();
    private final MutableLiveData<Doctor> doctor = new MutableLiveData<>();
    private final MutableLiveData<String> doctorError = new MutableLiveData<>();
    private final MutableLiveData<List<Doctor>> doctorsList = new MutableLiveData<>();
    private final MutableLiveData<String> doctorsListError = new MutableLiveData<>();
    private final MutableLiveData<byte[]> profilePicture = new MutableLiveData<>();
    private final MutableLiveData<String> profilePictureError = new MutableLiveData<>();
    private final MutableLiveData<WorkingMode> currentMode = new MutableLiveData<>();
    private final MutableLiveData<String> authUrl = new MutableLiveData<>();
    private final MutableLiveData<String> oAuthResult = new MutableLiveData<>();
    private final MutableLiveData<String> calendarEventResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> calendarConnectionStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<DrugReference> drugLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DrugReference>> drugsListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Page<DrugReference>> drugsPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> googleAuthUrl = new MutableLiveData<>();
    private final MutableLiveData<String> googleOAuthResult = new MutableLiveData<>();
    private final MutableLiveData<String> googleCalendarEventResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> googleCalendarConnectionStatus = new MutableLiveData<>();
    private final MutableLiveData<String> googleCalendarError = new MutableLiveData<>();
    private final MutableLiveData<String> notificationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> notificationError = new MutableLiveData<>();

    public ProjectViewModel(DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            SlotRepository slotRepository,
                            AppointmentRepository appointmentRepository,
                            GoogleCalendarRepository googleCalendarRepository,
                            MedicalRecordRepository medicalRecordRepository,
                            PrescriptionsRepository prescriptionsRepository,
                            NotificationRepository notificationRepository,
                            DrugRepository drugRepository) {  // Add prescriptions repository
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarRepository = googleCalendarRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionsRepository = prescriptionsRepository;  // Initialize prescriptions repository
        this.notificationRepository = notificationRepository;
        this.drugRepository = drugRepository;
    }

    // Login
    public void loginDoctor(String email, String phone) {
        doctorRepository.loginDoctor(email, phone, new LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                loginResponse.postValue(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                loginError.postValue(errorMessage);
            }
        });
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    // Doctor CRUD operations
    public void createDoctor(Doctor doctor) {
        doctorRepository.createDoctor(doctor, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor createdDoctor) {
                ProjectViewModel.this.doctor.postValue(createdDoctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void getDoctor(int id) {
        doctorRepository.getDoctor(id, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor doctor) {
                ProjectViewModel.this.doctor.postValue(doctor);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
            }
        });
    }

    public LiveData<Doctor> getDoctor() {
        return doctor;
    }

    public LiveData<String> getDoctorError() {
        return doctorError;
    }

    public void getAllDoctors() {
        doctorRepository.getAllDoctors(new DoctorsListCallback() {
            @Override
            public void onSuccess(List<Doctor> doctors) {
                doctorsList.postValue(doctors);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorsListError.postValue(errorMessage);
            }
        });
    }

    public void getDoctorsBySpecialty(String specialty) {
        doctorRepository.getDoctorsBySpecialty(specialty, new DoctorsListCallback() {
            @Override
            public void onSuccess(List<Doctor> doctors) {
                doctorsList.postValue(doctors);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorsListError.postValue(errorMessage);
            }
        });
    }

    public LiveData<List<Doctor>> getDoctorsList() {
        return doctorsList;
    }

    public LiveData<String> getDoctorsListError() {
        return doctorsListError;
    }

    public void updateDoctor(int id, Doctor doctor) {
        doctorRepository.updateDoctor(id, doctor, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor updatedDoctor) {
                ProjectViewModel.this.doctor.postValue(updatedDoctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void deleteDoctor(int id) {
        doctorRepository.deleteDoctor(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                operationError.postValue(errorMessage);
            }
        });
    }

    // Profile picture operations
    public void uploadDoctorProfilePicture(int id, MultipartBody.Part file) {
        doctorRepository.uploadDoctorProfilePicture(id, file, new VoidCallback() {
            @Override
            public void onSuccess() {
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                operationError.postValue(errorMessage);
            }
        });
    }

    public void getDoctorProfilePicture(int id) {
        doctorRepository.getDoctorProfilePicture(id, new ProfilePictureCallback() {
            @Override
            public void onSuccess(byte[] imageBytes) {
                profilePicture.postValue(imageBytes);
            }

            @Override
            public void onFailure(String errorMessage) {
                profilePictureError.postValue(errorMessage);
            }
        });
    }

    public LiveData<byte[]> getProfilePicture() {
        return profilePicture;
    }

    public LiveData<String> getProfilePictureError() {
        return profilePictureError;
    }

    // Create doctor with picture
    public void createDoctorWithPicture(RequestBody doctorRequest, MultipartBody.Part picture) {
        doctorRepository.createDoctorWithPicture(doctorRequest, picture, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor doctor) {
                ProjectViewModel.this.doctor.postValue(doctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void createDoctorWithPictureBase64(DoctorRequestWithBase64 request) {
        isLoading.setValue(true);

        doctorRepository.createDoctorWithPictureBase64(request, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor createdDoctor) {
                isLoading.postValue(false);
                doctor.postValue(createdDoctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }


    // Operation status

    public LiveData<String> getOperationError() {
        return operationError;
    }


    public void resetLoginStatus() {
        loginResponse.setValue(null);
        loginError.setValue(null);
    }

    private final MutableLiveData<PatientDTO> currentPatient = new MutableLiveData<>();
    private final MutableLiveData<String> patientError = new MutableLiveData<>();
    private final MutableLiveData<List<PatientDTO>> patientsList = new MutableLiveData<>();

    // Patient methods
    public void createPatient(Long doctorId, Patient patient) {
        patientRepository.createPatient(doctorId, patient, new PatientCallback() {
            @Override
            public void onSuccess(PatientDTO patientDTO) {
                currentPatient.postValue(patientDTO);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                patientError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void getPatient(Long id) {
        patientRepository.getPatient(id, new PatientCallback() {
            @Override
            public void onSuccess(PatientDTO patientDTO) {
                currentPatient.postValue(patientDTO);
            }

            @Override
            public void onFailure(String errorMessage) {
                patientError.postValue(errorMessage);
            }
        });
    }


    // Slot-related LiveData
    private final MutableLiveData<List<SlotDTO>> createdSlots = new MutableLiveData<>();
    private final MutableLiveData<List<SlotDTO>> availableSlots = new MutableLiveData<>();
    private final MutableLiveData<String> slotError = new MutableLiveData<>();

    public void createSlots(long doctorId, List<SlotCreateDTO> slots) {
        isLoading.setValue(true);
        slotRepository.createSlots(doctorId, slots, new SlotAppointmentsCallback() {
            @Override
            public void onSuccess(List<SlotDTO> slots) {
                isLoading.postValue(false);
                createdSlots.postValue(slots);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                slotError.postValue(errorMessage);
            }
        });
    }

    public void getFreeSlots(long doctorId, LocalDate date) {
        isLoading.setValue(true);
        slotRepository.getFreeSlots(doctorId, date, new SlotAppointmentsCallback() {
            @Override
            public void onSuccess(List<SlotDTO> slots) {
                isLoading.postValue(false);
                availableSlots.postValue(slots);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                slotError.postValue(errorMessage);
            }
        });
    }

    public void updateDoctorFcmToken(int doctorId, String token) {
        isLoading.setValue(true);
        doctorRepository.updateDoctorFcmToken(doctorId, token, new VoidCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                fcmTokenUpdateSuccess.postValue(true);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                fcmTokenUpdateError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }


    // LiveData getters
    public LiveData<List<SlotDTO>> getCreatedSlots() {
        return createdSlots;
    }

    public LiveData<List<SlotDTO>> getAvailableSlots() {
        return availableSlots;
    }

    public LiveData<String> getSlotError() {
        return slotError;
    }

    public LiveData<Boolean> getFcmTokenUpdateSuccess() {
        return fcmTokenUpdateSuccess;
    }

    public LiveData<String> getFcmTokenUpdateError() {
        return fcmTokenUpdateError;
    }

    public void resetFcmTokenStatus() {
        fcmTokenUpdateSuccess.setValue(false);
        fcmTokenUpdateError.setValue(null);
    }

    public void reserveAppointment(Long slotId, ReserveRequest request) {
        isLoading.setValue(true);
        appointmentRepository.reserveAppointment(slotId, request, new AppointmentCallback() {
            @Override
            public void onSuccess(AppointmentDTO appointmentDTO) {
                isLoading.postValue(false);
                appointment.postValue(appointmentDTO);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // Cancel appointment
    public void cancelAppointment(Long id) {
        isLoading.setValue(true);
        appointmentRepository.cancelAppointment(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // Accept appointment
    public void acceptAppointment(Long id) {
        isLoading.setValue(true);
        appointmentRepository.acceptAppointment(id, new AppointmentCallback() {
            @Override
            public void onSuccess(AppointmentDTO appointmentDTO) {
                isLoading.postValue(false);
                appointment.postValue(appointmentDTO);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // Reject appointment
    public void rejectAppointment(Long id) {
        isLoading.setValue(true);
        appointmentRepository.rejectAppointment(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // Get doctor's appointments
    public void getDoctorAppointments(Long doctorId) {
        isLoading.setValue(true);
        appointmentRepository.getDoctorAppointments(doctorId, new AppointmentsListCallback() {
            @Override
            public void onSuccess(List<AppointmentDTO> appointments) {
                isLoading.postValue(false);
                appointmentsList.postValue(appointments);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // Get patient's appointments
    public void getPatientAppointments(Long patientId) {
        isLoading.setValue(true);
        appointmentRepository.getPatientAppointments(patientId, new AppointmentsListCallback() {
            @Override
            public void onSuccess(List<AppointmentDTO> appointments) {
                isLoading.postValue(false);
                appointmentsList.postValue(appointments);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // LiveData Getters
    public LiveData<AppointmentDTO> getAppointment() {
        return appointment;
    }

    public LiveData<List<AppointmentDTO>> getAppointmentsList() {
        return appointmentsList;
    }


    // Reset methods
    public void resetOperationStatus() {
        operationSuccess.setValue(false);
        errorMessage.setValue(null);
    }


// ================ Medical Record Methods ================

    private final MutableLiveData<List<MedicalRecordDTO>> medicalRecordsListLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> medicalRecordErrorLiveData = new MutableLiveData<>();

    // Create medical record for appointment
    public void createMedicalRecordForAppointment(Long appointmentId, MedicalRecordDTO medicalRecordDTO) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.createMedicalRecordForAppointment(appointmentId, medicalRecordDTO,
                new MedicalRecordCallback() {
                    @Override
                    public void onSuccess(MedicalRecordDTO medicalRecord) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordLiveData.postValue(medicalRecord);
                        operationSuccessLiveData.postValue(true);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                        operationSuccessLiveData.postValue(false);
                    }
                });
    }

    // Get medical record by appointment ID
    public void getMedicalRecordByAppointment(Long appointmentId) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.getMedicalRecordByAppointment(appointmentId,
                new MedicalRecordCallback() {
                    @Override
                    public void onSuccess(MedicalRecordDTO medicalRecord) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordLiveData.postValue(medicalRecord);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                    }
                });
    }

    // Update medical record
    public void updateMedicalRecord(Integer recordId, MedicalRecordDTO medicalRecordDTO) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.updateMedicalRecord(recordId, medicalRecordDTO,
                new MedicalRecordCallback() {
                    @Override
                    public void onSuccess(MedicalRecordDTO medicalRecord) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordLiveData.postValue(medicalRecord);
                        operationSuccessLiveData.postValue(true);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                        operationSuccessLiveData.postValue(false);
                    }
                });
    }

    // Get medical record by ID
    public void getMedicalRecordById(Integer recordId) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.getMedicalRecordById(recordId,
                new MedicalRecordCallback() {
                    @Override
                    public void onSuccess(MedicalRecordDTO medicalRecord) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordLiveData.postValue(medicalRecord);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                    }
                });
    }

    // Delete medical record
    public void deleteMedicalRecord(Integer recordId) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.deleteMedicalRecord(recordId,
                new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        isLoadingLiveData.postValue(false);
                        operationSuccessLiveData.postValue(true);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                        operationSuccessLiveData.postValue(false);
                    }
                });
    }

    // Get patient medical history
    public void getPatientMedicalHistory(Long patientId) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.getPatientMedicalHistory(patientId,
                new MedicalRecordsListCallback() {
                    @Override
                    public void onSuccess(List<MedicalRecordDTO> records) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordsListLiveData.postValue(records);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                    }
                });
    }

    // Get filtered medical records for doctor
    public void getFilteredMedicalRecords(Long doctorId, String date, Motif motif) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.getFilteredMedicalRecords(doctorId, date, motif,
                new MedicalRecordsListCallback() {
                    @Override
                    public void onSuccess(List<MedicalRecordDTO> records) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordsListLiveData.postValue(records);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                    }
                });
    }

    // Get today's medical records by motif
    public void getTodayMedicalRecordsByMotif(Long doctorId, Motif motif) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.getTodayMedicalRecordsByMotif(doctorId, motif,
                new MedicalRecordsListCallback() {
                    @Override
                    public void onSuccess(List<MedicalRecordDTO> records) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordsListLiveData.postValue(records);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                    }
                });
    }

    // Get today's medical records
    public void getTodayMedicalRecords(Long doctorId, Motif motif) {
        isLoadingLiveData.setValue(true);
        medicalRecordRepository.getTodayMedicalRecords(doctorId, motif,
                new MedicalRecordsListCallback() {
                    @Override
                    public void onSuccess(List<MedicalRecordDTO> records) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordsListLiveData.postValue(records);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        isLoadingLiveData.postValue(false);
                        medicalRecordErrorLiveData.postValue(errorMessage);
                    }
                });
    }

    // LiveData getters
    public LiveData<MedicalRecordDTO> getMedicalRecordLiveData() {
        return medicalRecordLiveData;
    }

    public LiveData<List<MedicalRecordDTO>> getMedicalRecordsListLiveData() {
        return medicalRecordsListLiveData;
    }

    public LiveData<String> getMedicalRecordErrorLiveData() {
        return medicalRecordErrorLiveData;
    }

    public void clearMedicalRecordData() {
        medicalRecordLiveData.setValue(null);
        medicalRecordsListLiveData.setValue(null);
        medicalRecordErrorLiveData.setValue(null);
    }




    public void createPatientWithBase64(PatientRequestWithBase64 request) {
        isLoading.setValue(true);
        patientRepository.createPatientWithPictureBase64(request, new RawPatientCallback() {
            @Override
            public void onSuccess(Patient patient) {
                isLoading.postValue(false);
                base64CreatedPatient.postValue(patient);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                patientError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    private final MutableLiveData<Patient> base64CreatedPatient = new MutableLiveData<>();

    public LiveData<Patient> getBase64CreatedPatient() {
        return base64CreatedPatient;
    }

    public void clearBase64CreatedPatient() {
        base64CreatedPatient.setValue(null);
    }

    private MutableLiveData<Doctor> currentDoctor = new MutableLiveData<>();
    private MutableLiveData<String> patientPhotoBase64 = new MutableLiveData<>("");

    public void setCurrentDoctor(Doctor doctor) {
        currentDoctor.setValue(doctor);
    }

    public MutableLiveData<Doctor> getCurrentDoctor() {
        return currentDoctor;
    }

    public void setPatientPhotoBase64(String base64) {
        patientPhotoBase64.setValue(base64);
    }

    public MutableLiveData<String> getPatientPhotoBase64() {
        return patientPhotoBase64;
    }

    public LiveData<String> getAuthUrlLiveData() {
        return authUrlLiveData;
    }

    public LiveData<String> getOauthResultLiveData() {
        return oauthResultLiveData;
    }


    public void fetchDoctorMode(long doctorId) {
        isLoading.setValue(true);
        doctorRepository.getDoctorMode(doctorId, new ModeCallback() {
            @Override
            public void onSuccess(WorkingMode mode) {
                isLoading.postValue(false);
                currentMode.postValue(mode);
            }

            @Override
            public void onFailure(Throwable error) {
                isLoading.postValue(false);
                errorMessage.postValue(error.getMessage());
            }
        });
    }

    public void updateDoctorMode(long doctorId, WorkingMode newMode) {
        isLoading.setValue(true);
        doctorRepository.updateDoctorMode(doctorId, newMode, new SimpleCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                updateSuccess.postValue(true);
                currentMode.postValue(newMode); // Update the local mode
            }

            @Override
            public void onFailure(Throwable error) {
                isLoading.postValue(false);
                errorMessage.postValue(error.getMessage());
                updateSuccess.postValue(false);
            }
        });
    }

    public LiveData<PatientDTO> getCreatedPatient() {
        return createdPatient;
    }

    public void clearCreatedPatient() {
        createdPatient.setValue(null);
    }

    public LiveData<WorkingMode> getCurrentMode() {
        return currentMode;
    }


    // Loading LiveData
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    // Update success LiveData
    public LiveData<Boolean> isUpdateSuccess() {
        return updateSuccess;
    }

    // New Google Calendar methods
    public void fetchGoogleAuthUrl(Long doctorId) {
        isLoading.postValue(true);
        googleCalendarRepository.getGoogleAuthUrl(doctorId, new AuthUrlCallback() {
            @Override
            public void onSuccess(String url) {
                isLoading.postValue(false);
                googleAuthUrl.postValue(url);
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                googleCalendarError.postValue(error);
            }
        });
    }

    public void handleOAuthCallback(String code, String state) {
        isLoading.postValue(true);
        googleCalendarRepository.handleGoogleOAuthCallback(code, state, new OAuthCallback() {
            @Override
            public void onSuccess(String result) {
                isLoading.postValue(false);
                googleOAuthResult.postValue(result);
                checkGoogleCalendarConnection(Long.valueOf(state)); // state contains doctorId
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                googleCalendarError.postValue(error);
            }
        });
    }

    public void checkGoogleCalendarConnection(Long doctorId) {
        isLoading.postValue(true);
        googleCalendarRepository.checkCalendarConnection(doctorId, new CalendarStatusCallback() {
            @Override
            public void onSuccess(boolean isConnected) {
                isLoading.postValue(false);
                googleCalendarConnectionStatus.postValue(isConnected);
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                googleCalendarError.postValue(error);
            }
        });
    }

    public void addAppointmentToCalendar(Long appointmentId) {
        isLoading.postValue(true);
        googleCalendarRepository.addAppointmentToCalendar(appointmentId, new CalendarEventCallback() {
            @Override
            public void onSuccess(String eventId) {
                isLoading.postValue(false);
                googleCalendarEventResult.postValue(eventId);
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                googleCalendarError.postValue(error);
            }
        });
    }




    // New LiveData getters for Google Calendar
    public LiveData<String> getGoogleAuthUrl() {
        return googleAuthUrl;
    }

    public LiveData<String> getGoogleOAuthResult() {
        return googleOAuthResult;
    }

    public LiveData<String> getGoogleCalendarEventResult() {
        return googleCalendarEventResult;
    }

    public LiveData<Boolean> getGoogleCalendarConnectionStatus() {
        return googleCalendarConnectionStatus;
    }

    public LiveData<String> getGoogleCalendarError() {
        return googleCalendarError;
    }

    public LiveData<PrescriptionDTO> getPrescriptionLiveData() {
        return prescriptionLiveData;
    }

    public LiveData<List<PrescriptionDTO>> getPrescriptionsListLiveData() {
        return prescriptionsListLiveData;
    }

    public LiveData<File> getDownloadedPdfLiveData() {
        return downloadedPdfLiveData;
    }

    public LiveData<String> getPrescriptionsErrorLiveData() {
        return prescriptionsErrorLiveData;
    }

    public LiveData<List<PatientDTO>> getPatientsLiveData() {
        return patientsLiveData;
    }


    public void loadPatientsByDoctor(Long doctorId) {
        loadingLiveData.setValue(true);

        patientRepository.getPatientsByDoctor(doctorId, new PatientsListCallback() {
            @Override
            public void onSuccess(List<PatientDTO> patients) {
                loadingLiveData.postValue(false);
                patientsLiveData.postValue(patients);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    public void notifyDoctor(Appointment appointment) {
        isLoading.setValue(true);
        notificationRepository.notifyDoctor(appointment, new NotificationCallback() {
            @Override
            public void onSuccess(String response) {
                isLoading.postValue(false);
                notificationSuccess.postValue(response);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                notificationError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void sendTestNotification(long doctorId, Motif motif, String customMessage) {
        isLoading.setValue(true);
        TestNotificationRequest request = new TestNotificationRequest();
        notificationRepository.sendTestNotification(request, new NotificationCallback() {
            @Override
            public void onSuccess(String response) {
                isLoading.postValue(false);
                notificationSuccess.postValue(response);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                notificationError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    // LiveData getters
    public LiveData<String> getNotificationSuccess() {
        return notificationSuccess;
    }

    public LiveData<String> getNotificationError() {
        return notificationError;
    }

    // Reset notification status
    public void resetNotificationStatus() {
        notificationSuccess.setValue(null);
        notificationError.setValue(null);
    }

    // LiveData getters
    public LiveData<DrugReference> getDrug() {
        return drugLiveData;
    }

    public LiveData<List<DrugReference>> getDrugsList() {
        return drugsListLiveData;
    }

    public LiveData<Page<DrugReference>> getDrugsPage() {
        return drugsPageLiveData;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccessLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    // Repository method implementations
    public void getDrugs(String query, int page, int size, String sort) {
        loadingLiveData.setValue(true);
        drugRepository.getDrugs(query, page, size, sort, new DrugPageCallback() {
            @Override
            public void onSuccess(Page<DrugReference> drugPage) {
                drugsPageLiveData.postValue(drugPage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    public void getDrugById(long id) {
        loadingLiveData.setValue(true);
        drugRepository.getDrugById(id, new DrugCallback() {
            @Override
            public void onSuccess(DrugReference drug) {
                drugLiveData.postValue(drug);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    public void createDrug(DrugReference drug) {
        loadingLiveData.setValue(true);
        drugRepository.createDrug(drug, new DrugCallback() {
            @Override
            public void onSuccess(DrugReference createdDrug) {
                drugLiveData.postValue(createdDrug);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    public void updateDrug(long id, DrugReference drug) {
        loadingLiveData.setValue(true);
        drugRepository.updateDrug(id, drug, new DrugCallback() {
            @Override
            public void onSuccess(DrugReference updatedDrug) {
                drugLiveData.postValue(updatedDrug);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    public void deleteDrug(long id) {
        loadingLiveData.setValue(true);
        drugRepository.deleteDrug(id, new DrugCallback() {
            @Override
            public void onSuccess(DrugReference drug) {
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    public void autocompleteDrugs(String term, int limit) {
        loadingLiveData.setValue(true);
        drugRepository.autocompleteDrugs(term, limit, new DrugListCallback() {
            @Override
            public void onSuccess(List<DrugReference> drugs) {
                drugsListLiveData.postValue(drugs);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    // LiveData getters
    public LiveData<DrugReference> getDrugLiveData() {
        return drugLiveData;
    }

    public LiveData<List<DrugReference>> getDrugsListLiveData() {
        return drugsListLiveData;
    }

    public LiveData<Page<DrugReference>> getDrugsPageLiveData() {
        return drugsPageLiveData;
    }

    public LiveData<Boolean> getOperationSuccessLiveData() {
        return operationSuccessLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    // ================ Medical Record Methods ================

    // LiveData declarations (already in your class)
    private final MutableLiveData<MedicalRecordDTO> medicalRecord = new MutableLiveData<>();
    private final MutableLiveData<List<MedicalRecordDTO>> medicalRecordsHistory = new MutableLiveData<>();


    // LiveData getters
    public LiveData<MedicalRecordDTO> getMedicalRecord() {
        return medicalRecordLiveData;
    }

    public LiveData<List<MedicalRecordDTO>> getMedicalRecordsHistory() {
        return medicalRecordsHistoryLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }



    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }
    // ================ Prescriptions Methods ================
    // Create a new prescription
    public void createPrescription(Integer recordId, PrescriptionDTO prescription) {
        loadingLiveData.setValue(true);
        prescriptionsRepository.createPrescription(recordId, prescription, new PrescriptionCallback() {
            @Override
            public void onSuccess(PrescriptionDTO prescriptionDTO) {
                loadingLiveData.postValue(false);
                prescriptionLiveData.postValue(prescriptionDTO);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    // Get a single prescription
    public void getPrescription(Long id) {
        loadingLiveData.setValue(true);
        prescriptionsRepository.getPrescription(id, new PrescriptionCallback() {
            @Override
            public void onSuccess(PrescriptionDTO prescriptionDTO) {
                loadingLiveData.postValue(false);
                prescriptionLiveData.postValue(prescriptionDTO);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    // Get all prescriptions for a patient
    public void getPrescriptionsForPatient(Long patientId) {
        loadingLiveData.setValue(true);
        prescriptionsRepository.getPrescriptionsForPatient(patientId, new PrescriptionsListCallback() {
            @Override
            public void onSuccess(List<PrescriptionDTO> prescriptions) {
                loadingLiveData.postValue(false);
                prescriptionsListLiveData.postValue(prescriptions);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    // Delete a prescription
    public void deletePrescription(Long id) {
        loadingLiveData.setValue(true);
        prescriptionsRepository.deletePrescription(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    // Download prescription PDF
    public void downloadPrescriptionPdf(Long id) {
        loadingLiveData.setValue(true);
        prescriptionsRepository.downloadPrescriptionPdf(id, new PdfDownloadCallback() {
            @Override
            public void onSuccess(File file) {
                loadingLiveData.postValue(false);
                downloadedPdfLiveData.postValue(file);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
                operationSuccessLiveData.postValue(false);
            }
        });
    }

    // Send prescription to patient
    public void sendPrescriptionToPatient(Long id) {
        loadingLiveData.setValue(true);
        prescriptionsRepository.sendPrescriptionToPatient(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                loadingLiveData.postValue(false);
                operationSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
                operationSuccessLiveData.postValue(false);
            }
        });
    }
}
