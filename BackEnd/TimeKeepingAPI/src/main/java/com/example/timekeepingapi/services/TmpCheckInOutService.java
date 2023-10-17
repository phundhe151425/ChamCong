package com.example.timekeepingapi.services;

import com.example.timekeepingapi.models.TmpCheckInOut;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TmpCheckInOutService {
    List<TmpCheckInOut> findByBadgeNumber(String badgeNumber);
    List<TmpCheckInOut> findByDate(LocalDate checkDate);
}
