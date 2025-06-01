package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.callback.DrugCallback;
import com.hai811i.mobileproject.callback.DrugListCallback;
import com.hai811i.mobileproject.callback.DrugPageCallback;
import com.hai811i.mobileproject.entity.DrugReference;

public interface DrugRepository {
    void getDrugs(String query, int page, int size, String sort, DrugPageCallback callback);
    void getDrugById(long id, DrugCallback callback);
    void createDrug(DrugReference drug, DrugCallback callback);
    void updateDrug(long id, DrugReference drug, DrugCallback callback);
    void deleteDrug(long id, DrugCallback callback);
    void autocompleteDrugs(String term, int limit, DrugListCallback callback);
}