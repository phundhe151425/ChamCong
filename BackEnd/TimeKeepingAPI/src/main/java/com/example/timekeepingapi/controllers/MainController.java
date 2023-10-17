package com.example.timekeepingapi.controllers;

import com.example.timekeepingapi.models.TblInLateOutEarly;
import com.example.timekeepingapi.models.TmpCheckInOut;
import com.example.timekeepingapi.services.TblInLateOutEarlyService;
import com.example.timekeepingapi.services.TmpCheckInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {
    @Autowired
    private TmpCheckInOutService tmpCheckInOutService;
    @Autowired
    private TblInLateOutEarlyService tblInLateOutEarlyService;

    @GetMapping("logCheckInOutByBadgeNumber/{badgeNumber}")
    public List<TmpCheckInOut> listCheckInOut (@PathVariable String badgeNumber) {
        List<TmpCheckInOut> list = tmpCheckInOutService.findByBadgeNumber(badgeNumber);
        return list ;
    }

    @GetMapping("logCheckInOutByToday")
    public List<TmpCheckInOut> logCheckInOutByToday () {
        LocalDate currentDate = LocalDate.now();
        LocalDate yesterday = currentDate.minusDays(0);
        List<TmpCheckInOut> list = tmpCheckInOutService.findByDate(yesterday);
        return list ;
    }

    @GetMapping("logInLateOutEarlyByToday")
    public List<TblInLateOutEarly> logInLateOutEarlyByToday () {
        LocalDate currentDate = LocalDate.now();
        LocalDate yesterday = currentDate.minusDays(0);
        List<TblInLateOutEarly> list = tblInLateOutEarlyService.findByDate(yesterday);
        return list ;
    }
}
