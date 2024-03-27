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
public class WelcomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String hello(Model model) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName(); // Get the username

            // Retrieve the user by username
            User user = userRepository.findByUsername(username).orElse(null);

            // Add the first name to the model, or default to "User" if not found
            String firstName = user != null ? user.getFirstName() : "User";
            model.addAttribute("firstName", firstName);
            //adding userId to use it when displaying the dashboard
            Long userId = user != null ? user.getId() : 1; //default is id=1 for test user.
            model.addAttribute("userId", userId);

            // redirect to /dashboard
            return "redirect:/dashboard";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }
}

