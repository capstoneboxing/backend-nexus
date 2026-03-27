package com.nexus.mapper;

import com.nexus.dto.admin.AdminResponse;
import com.nexus.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public AdminResponse toResponse(Admin admin) {
        return new AdminResponse(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getCreatedAt()
        );
    }
}