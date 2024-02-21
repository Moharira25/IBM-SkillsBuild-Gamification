package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LandingPageController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String leaderboard(Model model) {

        User user = userAuth();
        Long id = user != null ? user.getId(): null;
        model.addAttribute("userId", id);
        return "landing";
    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
