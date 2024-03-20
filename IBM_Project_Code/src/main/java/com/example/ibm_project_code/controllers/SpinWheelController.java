package com.example.ibm_project_code.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpinWheelController {
    @GetMapping("/spin_wheel")
    public String showSpinWheel() {
        return "spin_wheel";
    }
}