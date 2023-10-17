package com.example.timekeepingapi.services;

import com.example.timekeepingapi.models.TblInLateOutEarly;
import com.example.timekeepingapi.models.TmpCheckInOut;

import java.time.LocalDate;
import java.util.List;

public interface TblInLateOutEarlyService {
    List<TblInLateOutEarly> findByDate(LocalDate checkDate);
}
