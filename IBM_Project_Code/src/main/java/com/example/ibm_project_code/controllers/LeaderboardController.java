package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.TimeTrial;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.TimeTrialRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
public class LeaderboardController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TimeTrialRepository timeTrialRepository;

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        try {
            List<User> users = (List<User>) userRepo.findAll();
            //sorting users by overall points
            users.sort((o1, o2) -> o2.getOverallPoints() - o1.getOverallPoints());
            User user = userAuth();
            Long id = user != null ? user.getId() : 1;
            model.addAttribute("userId", id);
            model.addAttribute("users", users);
            model.addAttribute("trial", null);
            return "leaderboard";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }

    @GetMapping("/leaderboard/timeTrial/{trialId}")
    public String leaderboardTimeTrial(Model model, @PathVariable long trialId) {
        try{
            List<User> users = (List<User>) userRepo.findAll();
            TimeTrial timeTrial = timeTrialRepository.findById(trialId).get();

            //Removing users that have not stared the trial
            users.removeIf(user -> user.getUserTimeTrial(timeTrial) == null);
            //for active trials we use the attribute trialScore to sort users
            if (!timeTrial.isEnded()) {
                //sorting users by their latest trial score.
                users.sort((o1, o2) -> o2.getTrialScore() - o1.getTrialScore());
            } else {
                //sorting users by their highest score for the old trial.
                users.sort((o1, o2) -> o2.getOldTrialScore(timeTrial) - o1.getOldTrialScore(timeTrial));
            }

            User user = userAuth();
            Long id = user != null ? user.getId() : 1;
            model.addAttribute("userId", id);
            model.addAttribute("users", users);
            model.addAttribute("trialLeaderboard", true);
            model.addAttribute("trial", timeTrial);
            return "leaderboard";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
