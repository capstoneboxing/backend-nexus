package com.nexus.service;

import com.nexus.dto.weightClass.WeightClassResponse;
import com.nexus.model.WeightClass;
import com.nexus.repository.WeightClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeightClassService {
    private final WeightClassRepository weightClassRepository;

    @Autowired
    public WeightClassService(WeightClassRepository weightClassRepository) {
        this.weightClassRepository = weightClassRepository;
    }

    // Get all weight classes
    public List<WeightClassResponse> getWeightClasses() {
        return weightClassRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get weight class by ID
    public WeightClassResponse getWeightClassById(Integer id) {
        WeightClass weightClass = weightClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Weight Class not found with id: " + id));

        return mapToResponse(weightClass);

    }

    // Get weight class by Name
    public WeightClassResponse getWeightClassByName(String className) {
        WeightClass weightClass = weightClassRepository.findByClassName(className)
                .orElseThrow(() -> new IllegalArgumentException("Weight Class not found with className: " + className));

        return mapToResponse(weightClass);
    }

    private WeightClassResponse mapToResponse(WeightClass weightClass) {
        return new WeightClassResponse(
                weightClass.getWeightClassId(),
                weightClass.getClassName(),
                weightClass.getMaxWeightLb(),
                weightClass.getMinWeightLb()
        );
    }
}
