package com.nexus.service;

import com.nexus.dto.admin.AdminCreateRequest;
import com.nexus.dto.admin.AdminResponse;
import com.nexus.dto.admin.AdminUpdateRequest;
import com.nexus.model.Admin;
import com.nexus.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminResponse createAdmin(AdminCreateRequest request) {
        if (adminRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Admin admin = Admin.builder()
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        System.out.println("Created Admin: " + savedAdmin);
        return mapToResponse(savedAdmin);
    }

    public List<AdminResponse> getAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AdminResponse getAdminById(Integer id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + id));

        return mapToResponse(admin);
    }

    public Admin getAdminEntityByUsername(String username) {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with username: " + username));
    }

    @Transactional
    public AdminResponse updateAdmin(Integer adminId, AdminUpdateRequest request) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + adminId));

        if (request.username() != null && !request.username().isBlank()) {
            adminRepository.findByUsername(request.username()).ifPresent(admin -> {
                if (!admin.getAdminId().equals(adminId)) {
                    throw new IllegalArgumentException("Username is already taken");
                }
            });
            existingAdmin.setUsername(request.username());
        }

        if (request.password() != null && !request.password().isBlank()) {
            existingAdmin.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return mapToResponse(updatedAdmin);
    }

    public void deleteAdmin(Integer id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    private AdminResponse mapToResponse(Admin admin) {
        return new AdminResponse(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getCreatedAt()
        );
    }
}