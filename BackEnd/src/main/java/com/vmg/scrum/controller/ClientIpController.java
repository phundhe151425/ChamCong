package com.vmg.scrum.controller;


import com.vmg.scrum.Utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/client-ip")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClientIpController {

    @Value("${app.ip.whitelist}")
    protected String whilelistIP;

    @GetMapping("")
    public ResponseEntity getClientIPAddress(HttpServletRequest request) {
        String ip = HttpUtils.getRequestIP(request);
        boolean isValid = false;
        if (whilelistIP.indexOf(ip) != -1){
            isValid = true;
        }
        Map<String, Object> rs = new HashMap<>();
        rs.put("ip", ip);
        rs.put("valid", isValid);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
