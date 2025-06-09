package com.hai811i.mobileproject.repository;


import com.hai811i.mobileproject.callback.PdfDownloadCallback;
import com.hai811i.mobileproject.callback.PrescriptionCallback;
import com.hai811i.mobileproject.callback.PrescriptionsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.PrescriptionDTO;

import java.io.File;
import java.util.List;

public interface PrescriptionsRepository {
    void createPrescription(Integer recordId, PrescriptionDTO prescription, PrescriptionCallback callback);

    void getPrescription(Long id, PrescriptionCallback callback);

    void getPrescriptionsForPatient(Long patientId, PrescriptionsListCallback callback);

    void deletePrescription(Long id, VoidCallback callback);

    void downloadPrescriptionPdf(Long id, PdfDownloadCallback callback);

    void sendPrescriptionToPatient(Long id, VoidCallback callback);

    // Callback interfaces






}

