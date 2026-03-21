package com.nexus.controller;

import com.nexus.dto.weightClass.WeightClassResponse;
import com.nexus.service.WeightClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/weight-classes")
public class WeightClassController {

    private final WeightClassService weightClassService;

    @Autowired
    public WeightClassController(WeightClassService weightClassService) {
        this.weightClassService = weightClassService;
    }

    // Get all weight classes
    @GetMapping
    public ResponseEntity<List<WeightClassResponse>> getWeightClasses() {
        return ResponseEntity.ok(weightClassService.getWeightClasses());
    }

    // Get weight class by ID
    @GetMapping(path = "/{id}")
    public ResponseEntity<WeightClassResponse> getWeightClassById(@PathVariable Integer id) {
        return ResponseEntity.ok(weightClassService.getWeightClassById(id));
    }

    // Get weight class by name
    @GetMapping(path = "/name/{className}")
    public ResponseEntity<WeightClassResponse> getWeightClassByName(@PathVariable String className) {
        return ResponseEntity.ok(weightClassService.getWeightClassByName(className));
    }
}