package com.nexus.service;

import com.nexus.dto.admin.AdminResponse;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.AdminMapper;
import com.nexus.model.Admin;
import com.nexus.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminService(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    public AdminResponse getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        return adminMapper.toResponse(admin);
    }
}