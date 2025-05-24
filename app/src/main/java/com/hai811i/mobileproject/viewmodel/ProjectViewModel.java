package com.hai811i.mobileproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hai811i.mobileproject.callback.AppointmentCallback;
import com.hai811i.mobileproject.callback.AppointmentsListCallback;
import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.MedicalRecordCallback;
import com.hai811i.mobileproject.callback.MedicalRecordsListCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;
import com.hai811i.mobileproject.callback.PatientCallback;
import com.hai811i.mobileproject.callback.RawPatientCallback;
import com.hai811i.mobileproject.callback.SlotCallback;
import com.hai811i.mobileproject.callback.SlotsListCallback;
import com.hai811i.mobileproject.dto.AppointmentDTO;
import com.hai811i.mobileproject.dto.MedicalRecordDTO;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.PatientRequestWithBase64;
import com.hai811i.mobileproject.dto.ReserveRequest;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.entity.AppointmentSlot;
import com.hai811i.mobileproject.callback.SlotAppointmentCallback;
import com.hai811i.mobileproject.callback.SlotAppointmentsCallback;
import com.hai811i.mobileproject.entity.Patient;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.MedicalRecordRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
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
    private final MutableLiveData<PatientDTO> createdPatient = new MutableLiveData<>();
    private final MutableLiveData<String> authUrlLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> oauthResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<MedicalRecordDTO> medicalRecord = new MutableLiveData<>();
    private final MutableLiveData<List<MedicalRecordDTO>> medicalRecordsHistory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
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


    public ProjectViewModel(DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            SlotRepository slotRepository,
                            AppointmentRepository appointmentRepository,
                            GoogleCalendarRepository googleCalendarRepository,
                            MedicalRecordRepository medicalRecordRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarRepository = googleCalendarRepository;
        this.medicalRecordRepository=medicalRecordRepository;
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
    public LiveData<Boolean> getOperationCompleted() {
        return operationCompleted;
    }

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
    public void createMedicalRecord(Long appointmentId, MedicalRecordDTO dto) {
        isLoading.setValue(true);
        medicalRecordRepository.createMedicalRecord(appointmentId, dto, new MedicalRecordCallback() {
            @Override
            public void onSuccess(MedicalRecordDTO record) {
                isLoading.postValue(false);
                medicalRecord.postValue(record);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void getMedicalRecordByAppointment(Long appointmentId) {
        isLoading.setValue(true);
        medicalRecordRepository.getMedicalRecordByAppointment(appointmentId, new MedicalRecordCallback() {
            @Override
            public void onSuccess(MedicalRecordDTO record) {
                isLoading.postValue(false);
                medicalRecord.postValue(record);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void updateMedicalRecord(Integer recordId, MedicalRecordDTO dto) {
        isLoading.setValue(true);
        medicalRecordRepository.updateMedicalRecord(recordId, dto, new MedicalRecordCallback() {
            @Override
            public void onSuccess(MedicalRecordDTO record) {
                isLoading.postValue(false);
                medicalRecord.postValue(record);
                operationSuccess.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void getMedicalRecordsHistory(Long patientId) {
        isLoading.setValue(true);
        medicalRecordRepository.getMedicalRecordsHistory(patientId, new MedicalRecordsListCallback() {
            @Override
            public void onSuccess(List<MedicalRecordDTO> records) {
                isLoading.postValue(false);
                medicalRecordsHistory.postValue(records);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void getMedicalRecordById(Integer recordId) {
        isLoading.setValue(true);
        medicalRecordRepository.getMedicalRecordById(recordId, new MedicalRecordCallback() {
            @Override
            public void onSuccess(MedicalRecordDTO record) {
                isLoading.postValue(false);
                medicalRecord.postValue(record);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void deleteMedicalRecord(Integer recordId) {
        isLoading.setValue(true);
        medicalRecordRepository.deleteMedicalRecord(recordId, new VoidCallback() {
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

    // LiveData Getters
    public LiveData<MedicalRecordDTO> getMedicalRecord() {
        return medicalRecord;
    }

    public LiveData<List<MedicalRecordDTO>> getMedicalRecordsHistory() {
        return medicalRecordsHistory;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
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

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchGoogleAuthUrl(Long doctorId) {
        googleCalendarRepository.getGoogleAuthUrl(doctorId, new AuthUrlCallback() {
            @Override
            public void onSuccess(String authUrl) {
                authUrlLiveData.postValue(authUrl);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    public void handleOAuthCallback(String code, String state) {
        googleCalendarRepository.handleGoogleOAuthCallback(code, state, new OAuthCallback() {
            @Override
            public void onSuccess(String resultMessage) {
                oauthResultLiveData.postValue(resultMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
            }
        });
    }
    public LiveData<PatientDTO> getCreatedPatient() {
        return createdPatient;
    }
    public void clearCreatedPatient() {
        createdPatient.setValue(null);
    }
}


