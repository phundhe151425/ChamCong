package com.vmg.scrum.controller;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogCheckInOut;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.request.FaceKeepRequest;
import com.vmg.scrum.payload.request.ImageLogRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.payload.response.UserLogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.LogCheckInOutService;
import com.vmg.scrum.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logCheckInOut")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LogCheckInOutController {
    @Autowired
    LogCheckInOutService logCheckInOutService;

    @GetMapping("data")
    public ResponseEntity<Page<LogCheckInOut>> getLogsByDateUserCode(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                     @RequestParam(name = "size", defaultValue = "30") int size,
                                                                     @RequestParam(name = "code", required = false) String code,
                                                                     @RequestParam(name = "departmentId", required = false) String departmentId,
                                                                     @RequestParam(name = "search", required = false) String search,
                                                                     @RequestParam(name = "from", required = false) String from,
                                                                     @RequestParam(name = "to", required = false) String to) throws ParseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<LogCheckInOut> pageLogs = logCheckInOutService.getData(code, departmentId,search, from, to, pageable);

        return new ResponseEntity<>(pageLogs, HttpStatus.OK);
    }
}
