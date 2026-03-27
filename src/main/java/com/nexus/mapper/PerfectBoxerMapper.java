package com.nexus.mapper;

import com.nexus.dto.perfectboxer.PerfectBoxerResponse;
import com.nexus.model.PerfectBoxer;
import org.springframework.stereotype.Component;

@Component
public class PerfectBoxerMapper {

    public PerfectBoxerResponse toResponse(PerfectBoxer perfectBoxer) {
        return new PerfectBoxerResponse(
                perfectBoxer.getPerfectBoxerId(),
                perfectBoxer.getBatchId(),
                perfectBoxer.getWeightClassId(),
                perfectBoxer.getCreatedAt()
        );
    }
}