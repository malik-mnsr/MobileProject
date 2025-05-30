// DoctorRepositoryImpl.java
package com.hai811i.mobileproject.implementation;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.ModeCallback;
import com.hai811i.mobileproject.callback.SimpleCallback;
import com.hai811i.mobileproject.entity.WorkingMode;
import com.hai811i.mobileproject.request.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.callback.DoctorCallback;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.callback.DoctorsListCallback;
import com.hai811i.mobileproject.callback.LoginCallback;
import com.hai811i.mobileproject.callback.ProfilePictureCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.response.LoginResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRepositoryImpl implements DoctorRepository {
    private final ApiService apiService;

    public DoctorRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    public void loginDoctor(String email, String phone, LoginCallback callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        callback.onFailure("Failed to get FCM token: " + task.getException());
                        return;
                    }

                    String fcmToken = task.getResult();

                    apiService.loginDoctor(email, phone, fcmToken).enqueue(new Callback<Doctor>() {
                        @Override
                        public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                callback.onSuccess(new LoginResponse(
                                        response.body(),
                                        "Login successful",
                                        true
                                ));
                            } else {
                                callback.onFailure("Login failed: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Doctor> call, Throwable t) {
                            callback.onFailure("Network error: " + t.getMessage());
                        }
                    });
                });
    }


    @Override
    public void createDoctor(Doctor doctor, DoctorCallback callback) {
        apiService.createDoctor(doctor).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to create doctor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getDoctor(int id, DoctorCallback callback) {
        apiService.getDoctor(id).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get doctor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getAllDoctors(DoctorsListCallback callback) {
        apiService.getAllDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get doctors: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getDoctorsBySpecialty(String specialty, DoctorsListCallback callback) {
        apiService.getDoctorsBySpecialty(specialty).enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get doctors by specialty: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateDoctor(int id, Doctor doctor, DoctorCallback callback) {
        apiService.updateDoctor(id, doctor).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to update doctor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void deleteDoctor(int id, VoidCallback callback) {
        apiService.deleteDoctor(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to delete doctor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void uploadDoctorProfilePicture(int id, MultipartBody.Part file, VoidCallback callback) {
        apiService.uploadDoctorProfilePicture(id, file).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to upload profile picture: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getDoctorProfilePicture(int id, ProfilePictureCallback callback) {
        apiService.getDoctorProfilePicture(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] bytes = response.body().bytes();
                        callback.onSuccess(bytes);
                    } catch (IOException e) {
                        callback.onFailure("Failed to read profile picture: " + e.getMessage());
                    }
                } else {
                    callback.onFailure("Failed to get profile picture: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void createDoctorWithPicture(RequestBody doctorRequest, MultipartBody.Part picture, DoctorCallback callback) {
        apiService.createDoctorWithPicture(doctorRequest, picture).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to create doctor with picture: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
    public void createDoctorWithPictureBase64(DoctorRequestWithBase64 request, DoctorCallback callback) {
        apiService.createDoctorWithPictureBase64(request).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String error = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        callback.onFailure("Server error: " + error);
                    } catch (IOException e) {
                        callback.onFailure("Error parsing server response");
                    }
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void updateDoctorFcmToken(int doctorId, String token, VoidCallback callback) {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("token", token);

        Call<Void> call = apiService.updateDoctorFcmToken(doctorId, tokenRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to update FCM token: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getDoctorMode(long doctorId, ModeCallback callback) {
        apiService.getDoctorMode(doctorId).enqueue(new Callback<WorkingMode>() {
            @Override
            public void onResponse(Call<WorkingMode> call, Response<WorkingMode> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Failed to get mode: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<WorkingMode> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void updateDoctorMode(long doctorId, WorkingMode newMode, SimpleCallback callback) {
        apiService.updateDoctorMode(doctorId, newMode).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(new Exception("Failed to update mode: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


}