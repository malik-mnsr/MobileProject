package com.hai811i.mobileproject.callback;

import java.io.File;

public  interface PdfDownloadCallback {
    void onSuccess(File pdfFile);
    void onFailure(String error);
}