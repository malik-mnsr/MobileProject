package com.hai811i.mobileproject.implementation;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.DrugCallback;
import com.hai811i.mobileproject.callback.DrugListCallback;
import com.hai811i.mobileproject.callback.DrugPageCallback;
import com.hai811i.mobileproject.entity.DrugReference;
import com.hai811i.mobileproject.entity.Page;
import com.hai811i.mobileproject.repository.DrugRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrugRepositoryImpl implements DrugRepository {
    private final ApiService apiService;

    public DrugRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void getDrugs(String query, int page, int size, String sort, DrugPageCallback callback) {
        apiService.getDrugs(query, page, size, sort).enqueue(new Callback<Page<DrugReference>>() {
            @Override
            public void onResponse(Call<Page<DrugReference>> call, Response<Page<DrugReference>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get drugs: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Page<DrugReference>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getDrugById(long id, DrugCallback callback) {
        apiService.getDrugById(id).enqueue(new Callback<DrugReference>() {
            @Override
            public void onResponse(Call<DrugReference> call, Response<DrugReference> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Drug not found");
                }
            }

            @Override
            public void onFailure(Call<DrugReference> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void createDrug(DrugReference drug, DrugCallback callback) {
        apiService.createDrug(drug).enqueue(new Callback<DrugReference>() {
            @Override
            public void onResponse(Call<DrugReference> call, Response<DrugReference> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to create drug: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DrugReference> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateDrug(long id, DrugReference drug, DrugCallback callback) {
        apiService.updateDrug(id, drug).enqueue(new Callback<DrugReference>() {
            @Override
            public void onResponse(Call<DrugReference> call, Response<DrugReference> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to update drug: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DrugReference> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void deleteDrug(long id, DrugCallback callback) {
        apiService.deleteDrug(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess((DrugReference) null); // Or create a success message
                } else {
                    callback.onFailure("Failed to delete drug: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void autocompleteDrugs(String term, int limit, DrugListCallback callback) {
        apiService.autocompleteDrugs(term, limit).enqueue(new Callback<List<com.example.mobileproject.dto.DrugLiteDTO>>() {
            @Override
            public void onResponse(Call<List<com.example.mobileproject.dto.DrugLiteDTO>> call, Response<List<com.example.mobileproject.dto.DrugLiteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Convert DrugLiteDTO list to DrugReference list
                    List<DrugReference> drugs = new ArrayList<>();
                    for (com.example.mobileproject.dto.DrugLiteDTO dto : response.body()) {
                        DrugReference drug = new DrugReference();
                        drug.setId(dto.getId());
                        drug.setName(dto.getName());
                        // Set other common fields if needed
                        drugs.add(drug);
                    }
                    callback.onSuccess(drugs);
                } else {
                    callback.onFailure("Autocomplete failed: " + (response != null ? response.message() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<List<com.example.mobileproject.dto.DrugLiteDTO>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }


}