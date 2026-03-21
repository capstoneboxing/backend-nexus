package com.nexus.service;

import com.nexus.model.PerfectBoxer;
import com.nexus.repository.PerfectBoxerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfectBoxerService {

    private final PerfectBoxerRepository perfectBoxerRepository;

    public PerfectBoxerService(PerfectBoxerRepository perfectBoxerRepository) {
        this.perfectBoxerRepository = perfectBoxerRepository;
    }

    public List<PerfectBoxer> findAll() {
        return perfectBoxerRepository.findAll();
    }

    public PerfectBoxer getPerfectBoxerById(Integer id) {
        return perfectBoxerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Perfect boxer not found: " + id));
    }

    public PerfectBoxer getByWeightClassId(Integer weightClassId) {
        return perfectBoxerRepository.findByWeightClassId(weightClassId)
                .orElseThrow(() -> new IllegalArgumentException("Perfect boxer not found for weight class: " + weightClassId));
    }

    public void deletePerfectBoxer(Integer id) {
        perfectBoxerRepository.deleteById(id);
    }
}