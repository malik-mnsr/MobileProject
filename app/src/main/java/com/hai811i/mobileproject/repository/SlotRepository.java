package com.hai811i.mobileproject.repository;



import com.hai811i.mobileproject.callback.SlotAppointmentsCallback;
import com.hai811i.mobileproject.callback.SlotCallback;
import com.hai811i.mobileproject.callback.SlotsListCallback;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository {
    void createSlots(long doctorId, List<SlotCreateDTO> slots, SlotAppointmentsCallback callback);
    void getFreeSlots(long doctorId, LocalDate date, SlotAppointmentsCallback callback);
}