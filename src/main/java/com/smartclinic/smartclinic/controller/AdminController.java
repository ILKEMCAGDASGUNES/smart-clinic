package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.model.mysql.Admin;
import com.smartclinic.smartclinic.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 1️⃣ Admin oluştur
    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminService.createAdmin(admin);
    }

    // 2️⃣ Email ile admin getir (login için altyapı)
    @GetMapping("/email")
    public Admin getAdminByEmail(@RequestParam String email) {
        return adminService.getByEmail(email);
    }

    // 3️⃣ Admin sil
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }
}
