package com.vmg.scrum.controller;

import com.vmg.scrum.model.Position;
import com.vmg.scrum.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/position")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PositionController {
    @Autowired
    PositionRepository positionRepository;

    @GetMapping("")
    public List<Position> getPositions(){
        return positionRepository.findAll();
    }
}
