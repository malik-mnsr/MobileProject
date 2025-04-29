// DoctorRepositoryImpl.java
package com.hai811i.mobileproject.implementation;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.dto.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.repository.DoctorCallback;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.DoctorsListCallback;
import com.hai811i.mobileproject.repository.LoginCallback;
import com.hai811i.mobileproject.repository.ProfilePictureCallback;
import com.hai811i.mobileproject.repository.VoidCallback;
import com.hai811i.mobileproject.response.LoginResponse;

import java.io.IOException;
import java.util.List;

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

    @Override
    public void loginDoctor(String email, String phone, LoginCallback callback) {
        apiService.loginDoctor(email, phone).enqueue(new Callback<Doctor>() {
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
    public void uploadProfilePicture(int id, MultipartBody.Part file, VoidCallback callback) {
        apiService.uploadProfilePicture(id, file).enqueue(new Callback<Void>() {
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
    public void getProfilePicture(int id, ProfilePictureCallback callback) {
        apiService.getProfilePicture(id).enqueue(new Callback<ResponseBody>() {
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
}