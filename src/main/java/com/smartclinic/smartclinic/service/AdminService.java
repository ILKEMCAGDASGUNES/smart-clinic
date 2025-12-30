package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.model.mysql.Admin;
import com.smartclinic.smartclinic.repository.mysql.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin getByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}
