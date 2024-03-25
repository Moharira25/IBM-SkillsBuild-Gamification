package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.*;
import com.example.ibm_project_code.repositories.FriendRequestRepository;
import com.example.ibm_project_code.repositories.UserCourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userAuth();
        model.addAttribute("user", user);
        model.addAttribute("dto", new DataTransferObject());

        // Update potential friends to exclude users who have sent or received a pending friend request
        List<User> allUsers = StreamSupport.stream(userRepo.findAll().spliterator(), false)
                .toList();
        List<User> potentialFriends = new ArrayList<>();

        for (User u : allUsers) {
            //boolean isFriend = user.getFriends().contains(u);
            boolean hasPendingRequest = !friendRequestRepository.findBySenderAndReceiver(user, u).isEmpty() ||
                    !friendRequestRepository.findBySenderAndReceiver(u, user).isEmpty();
           // if (!u.equals(user) && !isFriend && !hasPendingRequest) {
                potentialFriends.add(u);
            }
        //}
        // Fetch and display incoming friend requests
        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverAndStatus(user, "PENDING");
        model.addAttribute("potentialFriends", potentialFriends);
        model.addAttribute("friendRequests", friendRequests);

        return "profile";
    }

    @PostMapping("/sendFriendRequest")
    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId) {
        User sender = userAuth();
        User receiver = userRepo.findById(receiverId).orElse(null);
        if (receiver != null && !receiver.equals(sender)) {
            // Prevent duplicate friend requests
            boolean requestExists = friendRequestRepository.findBySenderAndReceiver(sender, receiver).stream()
                    .anyMatch(request -> "PENDING".equals(request.getStatus()));
            if (!requestExists) {
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setSender(sender);
                friendRequest.setReceiver(receiver);
                friendRequest.setStatus("PENDING");
                friendRequestRepository.save(friendRequest);
            }
        }
        return "redirect:/profile";
    }

    @PostMapping("/acceptFriendRequest")
    public String acceptFriendRequest(@RequestParam("requestId") Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElse(null);
        if (friendRequest != null && "PENDING".equals(friendRequest.getStatus())) {
            User sender = friendRequest.getSender();
            User receiver = friendRequest.getReceiver();
            //sender.getFriends().add(receiver);
            //receiver.getFriends().add(sender); // Assuming mutual friendship
            friendRequest.setStatus("ACCEPTED");
            userRepo.save(sender);
            userRepo.save(receiver);
            friendRequestRepository.save(friendRequest);
        }
        return "redirect:/profile";
    }
    @GetMapping("/friendProfile/{id}")
    public String viewFriendProfile(@PathVariable Long id, Model model) {
        User friend = userRepo.findById(id).orElse(null);
        if (friend == null) {
            return "redirect:/profile";
        }
        List<UserCourse> friendCourses = userCourseRepository.findByUser(friend);
        List<Course> courses = friendCourses.stream().map(UserCourse::getCourse).collect(Collectors.toList());

        model.addAttribute("friend", friend);
        model.addAttribute("courses", courses);

        return "friendProfile";
    }
    @PostMapping("/removeFriend")
    public String removeFriend(@RequestParam("friendId") Long friendId) {
        User user = userAuth();
        User friend = userRepo.findById(friendId).orElse(null);

        //if (friend != null && user.getFriends().contains(friend)) {
            //user.getFriends().remove(friend);
            // Optional: Remove the user from the friend's list for bidirectional removal
            // friend.getFriends().remove(user);
            userRepo.save(user);

            // userRepo.save(friend);
        //}

        return "redirect:/profile";
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
        return userRepo.findByUsername(username).orElse(null);
    }


}
