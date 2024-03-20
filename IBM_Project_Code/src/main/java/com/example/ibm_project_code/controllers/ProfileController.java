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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userAuth();
        model.addAttribute("user", user);
        model.addAttribute("dto", new DataTransferObject());
        String currentUsername = user.getUsername();
        List<User> potentialFriends = StreamSupport.stream(userRepo.findAll().spliterator(), false)
                .filter(u -> !u.getUsername().equals(currentUsername))
                .collect(Collectors.toList());

        model.addAttribute("potentialFriends", potentialFriends);

        return "profile";
    }
    @PostMapping("/addFriend")
    public String addFriend(@RequestParam("friendId") Long friendId) {
        User user = userAuth();
        User friend = userRepo.findById(friendId).orElse(null);
        if (friend != null) {
            user.getFriends().add(friend);
            userRepo.save(user);
        }
        return "redirect:/profile";
    }
    @GetMapping("/friendProfile/{id}")
    public String viewFriendProfile(@PathVariable Long id, Model model) {
        User friend = userRepo.findById(id).orElse(null);
        if (friend == null) {
            return "redirect:/profile";
        }
        model.addAttribute("friend", friend);
        return "friendProfile";
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
