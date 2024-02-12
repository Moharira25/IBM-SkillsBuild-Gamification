package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class LeaderboardController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<User> users =  userRepo.findAll();
        //sorting users by overall points
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getOverallPoints() - o1.getOverallPoints();
            }
        });
        model.addAttribute("users", users);
        return "leaderboard";
    }
}
