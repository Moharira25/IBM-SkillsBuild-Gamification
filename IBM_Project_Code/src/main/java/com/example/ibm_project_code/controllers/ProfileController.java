package com.example.ibm_project_code.controllers;


import com.example.ibm_project_code.database.Course;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.database.UserCourse;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.repositories.UserCourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;
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
    public String editingProfile(Model model,

                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String secondName,
                                 @RequestParam("bio") String bio){
        User user = userAuth();

        user.setBio(bio);
        user.setFirstName(firstName);
        user.setLastName(secondName);
        userRepo.save(user);
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());

        return "redirect:/profile";

    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
