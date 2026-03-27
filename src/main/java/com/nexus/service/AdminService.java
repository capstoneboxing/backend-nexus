package com.nexus.service;

import com.nexus.dto.admin.AdminCreateRequest;
import com.nexus.dto.admin.AdminResponse;
import com.nexus.dto.admin.AdminUpdateRequest;
import com.nexus.exception.ConflictException;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.AdminMapper;
import com.nexus.model.Admin;
import com.nexus.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminMapper = adminMapper;
    }

    public AdminResponse createAdmin(AdminCreateRequest request) {
        if (adminRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already taken");
        }

        Admin admin = Admin.builder()
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toResponse(savedAdmin);
    }

    @Transactional
    public AdminResponse updateAdmin(Integer adminId, AdminUpdateRequest request) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

        if (request.username() != null && !request.username().isBlank()) {
            adminRepository.findByUsername(request.username()).ifPresent(admin -> {
                if (!admin.getAdminId().equals(adminId)) {
                    throw new ConflictException("Username is already taken");
                }
            });
            existingAdmin.setUsername(request.username());
        }

        if (request.password() != null && !request.password().isBlank()) {
            existingAdmin.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return adminMapper.toResponse(updatedAdmin);
    }

    public AdminResponse getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        return adminMapper.toResponse(admin);
    }
}