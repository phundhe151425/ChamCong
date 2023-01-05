package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.impl.FileManagerService;
import com.vmg.scrum.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("api/file")
public class FileController {
    @Autowired
    FileManagerService fileManagerService;

    @Autowired
    UserRepository userRepository;
    @GetMapping("info")
    public Map<String,String> download () {
        List<User> user = userRepository.findAll();
        Map<String,String> listInfo = new HashMap<>();
        for(User u : user){
        String pathFile = "http://localhost:8080/"+u.getCover();
        listInfo.put(pathFile,u.getCode());
        }
        return listInfo;
    }
}
