package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.entity.WorkingMode;

public interface ModeCallback {
    void onSuccess(WorkingMode mode);
    void onFailure(Throwable error);
}