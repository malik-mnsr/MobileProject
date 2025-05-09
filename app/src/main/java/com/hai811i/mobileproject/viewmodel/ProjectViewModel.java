package com.hai811i.mobileproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hai811i.mobileproject.callback.PatientCallback;
import com.hai811i.mobileproject.callback.SlotCallback;
import com.hai811i.mobileproject.callback.SlotsListCallback;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.entity.AppointmentSlot;
import com.hai811i.mobileproject.callback.SlotAppointmentCallback;
import com.hai811i.mobileproject.callback.SlotAppointmentsCallback;
import com.hai811i.mobileproject.entity.Patient;
import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
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

    // LiveData for login
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationCompleted = new MutableLiveData<>();
    private final MutableLiveData<String> operationError = new MutableLiveData<>();
    private final MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    // LiveData for doctor operations
    private final MutableLiveData<Doctor> doctor = new MutableLiveData<>();
    private final MutableLiveData<String> doctorError = new MutableLiveData<>();

    // LiveData for doctors list
    private final MutableLiveData<List<Doctor>> doctorsList = new MutableLiveData<>();
    private final MutableLiveData<String> doctorsListError = new MutableLiveData<>();

    // LiveData for profile picture
    private final MutableLiveData<byte[]> profilePicture = new MutableLiveData<>();
    private final MutableLiveData<String> profilePictureError = new MutableLiveData<>();


    public ProjectViewModel(DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            SlotRepository slotRepository,
                            AppointmentRepository appointmentRepository,
                            GoogleCalendarRepository googleCalendarRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarRepository = googleCalendarRepository;
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

    // Loading state
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Operation status
    public LiveData<Boolean> getOperationCompleted() {
        return operationCompleted;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    // Reset LiveData
    public void resetOperationStatus() {
        operationCompleted.setValue(false);
        operationError.setValue(null);
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


}
