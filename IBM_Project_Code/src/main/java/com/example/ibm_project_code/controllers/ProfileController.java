package com.example.ibm_project_code.controllers;


import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.repositories.UserCourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserCourseRepository userCourseRepo;


    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userAuth();
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());

        return "profile";
    }

    @PostMapping("/profile")
    public String editingProfile(Model model, @ModelAttribute User user1){
        user1.setEmail("email@email.com");
        user1.setUsername("username");
        user1.setPassword("password");
        user1.setEnabled(true); // Assuming you want to enable the user right away
        user1.setEmailVerified(false); // Set to true as appropriate
        Timestamp currentTime = Timestamp.from(Instant.now());
        user1.setCreatedDate(currentTime);
        user1.setLastModifiedDate(currentTime);

        User user = userAuth();
        user.setFirstName(user1.getFirstName());
        user.setLastName(user1.getLastName());
        user.setBio(user1.getBio());
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
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
