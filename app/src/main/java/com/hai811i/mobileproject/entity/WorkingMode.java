package com.hai811i.mobileproject.entity;

import java.io.Serializable;

public enum WorkingMode implements Serializable {
    NORMAL,          // mode par défaut
    CONSULTATION,    // en consultation
    HOME_VISIT,      // visite à domicile
    DND,             // “Do Not Disturb” / Ne pas déranger
    EMERGENCY,
    ABSENT//       // absent
}
