package com.example.ibm_project_code.controllers;


import com.example.ibm_project_code.database.DataTransferObject;
import com.example.ibm_project_code.database.User;

import com.example.ibm_project_code.repositories.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userAuth();
        model.addAttribute("user", user);
        model.addAttribute("dto", new DataTransferObject());

        return "profile";
    }

    @PostMapping("/profile")
    public String editingProfile(Model model, @ModelAttribute DataTransferObject dto){
        User user = userAuth();
        //making sure the values are not null or empty spaces
        String bio = dto.getBio() != null && !StringUtils.isBlank(dto.getBio()) ? dto.getBio(): user.getBio();
        String firstName = dto.getFirstName() != null && !StringUtils.isBlank(dto.getFirstName()) ? dto.getFirstName(): user.getFirstName();
        String lastName = dto.getLastName() != null && !StringUtils.isBlank(dto.getLastName()) ? dto.getLastName(): user.getLastName();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);
        model.addAttribute("user", user);
        userRepo.save(user);

        return "redirect:/profile";

    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
