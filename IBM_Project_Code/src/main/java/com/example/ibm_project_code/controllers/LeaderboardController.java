package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class LeaderboardController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<User> users = (List<User>) userRepo.findAll();
        //sorting users by overall points
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getOverallPoints() - o1.getOverallPoints();
            }
        });
        User user = userAuth();
        Long id = user != null ? user.getId():1;
        model.addAttribute("userId", id);
        model.addAttribute("users", users);
        return "leaderboard";
    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
