package com.nexus.service;

import com.nexus.dto.categoryweight.CategoryWeightResponse;
import com.nexus.dto.categoryweight.CategoryWeightUpdateRequest;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.model.CategoryWeight;
import com.nexus.repository.CategoryWeightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryWeightService {

    private static final double EPSILON = 0.000001;

    private final CategoryWeightRepository categoryWeightRepository;

    public CategoryWeightService(CategoryWeightRepository categoryWeightRepository) {
        this.categoryWeightRepository = categoryWeightRepository;
    }

    public List<CategoryWeightResponse> getAll() {
        return categoryWeightRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryWeightResponse getByWeightClassId(Integer weightClassId) {
        CategoryWeight categoryWeight = categoryWeightRepository.findById(weightClassId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category weight for weight class id " + weightClassId + " does not exist"
                ));

        return mapToResponse(categoryWeight);
    }

    public CategoryWeightResponse updateByWeightClassId(Integer weightClassId, CategoryWeightUpdateRequest request) {
        validateWeightsSumToOne(request);

        CategoryWeight existingCategoryWeight = categoryWeightRepository.findById(weightClassId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category weight for weight class id " + weightClassId + " does not exist"
                ));

        existingCategoryWeight.setPhysicalWeight(request.physicalWeight());
        existingCategoryWeight.setTechnicalWeight(request.technicalWeight());
        existingCategoryWeight.setTacticalWeight(request.tacticalWeight());
        existingCategoryWeight.setPsychologicalWeight(request.psychologicalWeight());
        existingCategoryWeight.setExperienceWeight(request.experienceWeight());

        CategoryWeight savedCategoryWeight = categoryWeightRepository.save(existingCategoryWeight);
        return mapToResponse(savedCategoryWeight);
    }

    public CategoryWeight getEntityByWeightClassId(Integer weightClassId) {
        return categoryWeightRepository.findById(weightClassId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category weight for weight class id " + weightClassId + " does not exist"
                ));
    }

    private void validateWeightsSumToOne(CategoryWeightUpdateRequest request) {
        double sum =
                request.physicalWeight()
                        + request.technicalWeight()
                        + request.tacticalWeight()
                        + request.psychologicalWeight()
                        + request.experienceWeight();

        if (Math.abs(sum - 1.0) > EPSILON) {
            throw new IllegalArgumentException("Category weights must add up to 1.0");
        }
    }

    private CategoryWeightResponse mapToResponse(CategoryWeight categoryWeight) {
        return new CategoryWeightResponse(
                categoryWeight.getWeightClassId(),
                categoryWeight.getPhysicalWeight(),
                categoryWeight.getTechnicalWeight(),
                categoryWeight.getTacticalWeight(),
                categoryWeight.getPsychologicalWeight(),
                categoryWeight.getExperienceWeight()
        );
    }
}