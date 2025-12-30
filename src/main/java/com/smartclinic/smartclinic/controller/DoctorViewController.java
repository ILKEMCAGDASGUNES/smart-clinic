package com.smartclinic.smartclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctor")
public class DoctorViewController {

    @GetMapping("/dashboard")
    public String doctorDashboard() {
        return "doctor/doctorDashboard";
    }
}
