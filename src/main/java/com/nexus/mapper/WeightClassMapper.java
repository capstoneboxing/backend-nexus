package com.nexus.mapper;

import com.nexus.dto.weightClass.WeightClassResponse;
import com.nexus.model.WeightClass;
import org.springframework.stereotype.Component;

@Component
public class WeightClassMapper {

    public WeightClassResponse toResponse(WeightClass weightClass) {
        return new WeightClassResponse(
                weightClass.getWeightClassId(),
                weightClass.getClassName(),
                weightClass.getMaxWeightLb(),
                weightClass.getMinWeightLb()
        );
    }
}
