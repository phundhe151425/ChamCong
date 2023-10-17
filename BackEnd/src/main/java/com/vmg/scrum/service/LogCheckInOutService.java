package com.vmg.scrum.service;

import com.vmg.scrum.model.excel.LogCheckInOut;
import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogCheckInOutService {

    Page<LogCheckInOut> getData(String code, String departmentId,String searchInput, String from, String to, Pageable pageable);
}
