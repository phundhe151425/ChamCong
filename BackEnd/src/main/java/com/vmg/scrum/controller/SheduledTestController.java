package com.vmg.scrum.controller;

import com.vmg.scrum.payload.response.TblInLateOutEarly;
import com.vmg.scrum.payload.response.TmpCheckInOut;
import com.vmg.scrum.scheduled.CallApi;
import com.vmg.scrum.scheduled.FurloughSheduled;
import com.vmg.scrum.scheduled.SalarySheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SheduledTestController {
    @Autowired
    SalarySheduled salarySheduled;
    @Autowired
    FurloughSheduled furloughSheduled;

    @Autowired
    CallApi callApi;

    @GetMapping("furlough")
    public void setFurloughSheduled(){
        furloughSheduled.calculateFurloughReport();
    }
    @GetMapping("salary")
    public void setSalarySheduledSheduled(){
        salarySheduled.convertSignFace();
    }

    @GetMapping("getCheckInOut")
    public List<TmpCheckInOut> getCheckInOut(){
        List<TmpCheckInOut> tmpCheckInOutList =
                callApi.getLogCheckInOut();
        return tmpCheckInOutList;
    }

    @GetMapping("readLogCheck")
    public void readLogCheck(){
        salarySheduled.readLogYesterday();
    }

    @GetMapping("getInLateOutEarly")
    public List<TblInLateOutEarly> getInLateOutEarly(){
        List<TblInLateOutEarly> tblInLateOutEarlies =
                callApi.getLogInLateOutEarly();
        return tblInLateOutEarlies;
    }

}
