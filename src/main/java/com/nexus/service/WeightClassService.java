package com.nexus.service;

import com.nexus.dto.weightClass.WeightClassResponse;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.WeightClassMapper;
import com.nexus.model.WeightClass;
import com.nexus.repository.WeightClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightClassService {
    private final WeightClassRepository weightClassRepository;
    private final WeightClassMapper weightClassMapper;

    @Autowired
    public WeightClassService(WeightClassRepository weightClassRepository, WeightClassMapper weightClassMapper) {
        this.weightClassRepository = weightClassRepository;
        this.weightClassMapper = weightClassMapper;
    }

    // Get all weight classes
    public List<WeightClassResponse> getWeightClasses() {
        return weightClassRepository.findAll()
                .stream()
                .map(weightClassMapper::toResponse)
                .toList();
    }

    // Get weight class by ID
    public WeightClassResponse getWeightClassById(Integer id) {
        WeightClass weightClass = weightClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weight Class not found with id: " + id));

        return weightClassMapper.toResponse(weightClass);

    }

    // Get weight class by Name
    public WeightClassResponse getWeightClassByName(String className) {
        WeightClass weightClass = weightClassRepository.findByClassName(className)
                .orElseThrow(() -> new ResourceNotFoundException("Weight Class not found with className: " + className));

        return weightClassMapper.toResponse(weightClass);
    }

}
