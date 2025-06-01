package com.hai811i.mobileproject.implementation;

import android.os.Environment;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.PdfDownloadCallback;
import com.hai811i.mobileproject.callback.PrescriptionCallback;
import com.hai811i.mobileproject.callback.PrescriptionsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.PrescriptionDTO;
import com.hai811i.mobileproject.repository.PrescriptionsRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionsRepositoryImpl implements PrescriptionsRepository {
    private final ApiService apiService;

    public PrescriptionsRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void createPrescription(Integer recordId, PrescriptionDTO prescription, PrescriptionCallback callback) {
        apiService.createPrescription(recordId, prescription).enqueue(new Callback<PrescriptionDTO>() {
            @Override
            public void onResponse(Call<PrescriptionDTO> call, Response<PrescriptionDTO> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Failed to create prescription";
                        callback.onFailure(errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Failed to create prescription");
                    }
                }
            }

            @Override
            public void onFailure(Call<PrescriptionDTO> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getPrescription(Long id, PrescriptionCallback callback) {
        apiService.getPrescription(id).enqueue(new Callback<PrescriptionDTO>() {
            @Override
            public void onResponse(Call<PrescriptionDTO> call, Response<PrescriptionDTO> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Prescription not found";
                        callback.onFailure(errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Prescription not found");
                    }
                }
            }

            @Override
            public void onFailure(Call<PrescriptionDTO> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getPrescriptionsForPatient(Long patientId, PrescriptionsListCallback callback) {
        apiService.listPrescriptionsForPatient(patientId).enqueue(new Callback<List<PrescriptionDTO>>() {
            @Override
            public void onResponse(Call<List<PrescriptionDTO>> call, Response<List<PrescriptionDTO>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Failed to load prescriptions";
                        callback.onFailure(errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Failed to load prescriptions");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PrescriptionDTO>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void deletePrescription(Long id, VoidCallback callback) {
        apiService.deletePrescription(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Failed to delete prescription";
                        callback.onFailure(errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Failed to delete prescription");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void downloadPrescriptionPdf(Long id, PdfDownloadCallback callback) {
        apiService.downloadPrescriptionPdf(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Save to a temporary file
                        File file = File.createTempFile("prescription_" + id, ".pdf");
                        try (InputStream inputStream = response.body().byteStream();
                             OutputStream outputStream = new FileOutputStream(file)) {
                            byte[] fileReader = new byte[4096];
                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) break;
                                outputStream.write(fileReader, 0, read);
                            }
                            outputStream.flush();
                            callback.onSuccess(file);
                        }
                    } catch (IOException e) {
                        callback.onFailure("Failed to save PDF file: " + e.getMessage());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Failed to download PDF";
                        callback.onFailure(errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Failed to download PDF");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void sendPrescriptionToPatient(Long id, VoidCallback callback) {
        apiService.sendPrescriptionToPatient(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Failed to send prescription";
                        callback.onFailure(errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Failed to send prescription");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}