package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.*;
import com.example.ibm_project_code.repositories.BadgeRepository;
import com.example.ibm_project_code.repositories.FriendRequestRepository;
import com.example.ibm_project_code.repositories.UserCourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import com.example.ibm_project_code.repositories.BadgeCollectionRepository;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Autowired
    private BadgeRepository badgeRepository;
    @Autowired
    private BadgeCollectionRepository badgeCollectionRepository;

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        try {
            User user = userAuth();
            model.addAttribute("user", user);
            model.addAttribute("dto", new DataTransferObject());
            Set<User> friends = new HashSet<>(user.getFriends());
            friends.add(user);
            List<User> sortedFriends = friends.stream()
                    .sorted((friend1, friend2) -> Integer.compare(friend2.getOverallPoints(), friend1.getOverallPoints()))
                    .collect(Collectors.toList());

            model.addAttribute("sortedFriends", sortedFriends);


            List<User> allUsers = StreamSupport.stream(userRepo.findAll().spliterator(), false)
                    .toList();
            List<User> potentialFriends = new ArrayList<>();

            for (User u : allUsers) {
                boolean isFriend = user.getFriends().contains(u);
                boolean hasPendingRequest = !friendRequestRepository.findBySenderAndReceiver(user, u).isEmpty() ||
                        !friendRequestRepository.findBySenderAndReceiver(u, user).isEmpty();
                if (!u.equals(user) && !isFriend && !hasPendingRequest) {
                    potentialFriends.add(u);
                }
            }
            List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverAndStatus(user, "PENDING");
            List<Badge> badges = (List<Badge>) badgeRepository.findAll();
            List<BadgeCollection> badgeCollections = user.getBadgeCollection();
            model.addAttribute("potentialFriends", potentialFriends);
            model.addAttribute("friendRequests", friendRequests);
            model.addAttribute("userId", user.getId());
            model.addAttribute("badges", badges);
            model.addAttribute("badgeCollections", badgeCollections);
            return "profile";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }

    @PostMapping("/sendFriendRequest")
    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId, Model model) {
        try {
            User sender = userAuth();
            User receiver = userRepo.findById(receiverId).orElse(null);
            if (receiver != null && !receiver.equals(sender)) {
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
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }

    @PostMapping("/acceptFriendRequest")
    public String acceptFriendRequest(@RequestParam("requestId") Long requestId, Model model) {
        try {
            FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElse(null);
            if (friendRequest != null && "PENDING".equals(friendRequest.getStatus())) {
                User sender = friendRequest.getSender();
                User receiver = friendRequest.getReceiver();
                sender.getFriends().add(receiver);
                receiver.getFriends().add(sender);
                friendRequest.setStatus("ACCEPTED");
                userRepo.save(sender);
                userRepo.save(receiver);
                friendRequestRepository.save(friendRequest);
            }
            return "redirect:/profile";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }
    @GetMapping("/friendProfile/{id}")
    public String viewFriendProfile(@PathVariable Long id, Model model) {
        try {
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
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }
    @PostMapping("/removeFriend")
    public String removeFriend(@RequestParam("friendId") Long friendId, Model model) {
        try {
            User user = userAuth();
            User friend = userRepo.findById(friendId).orElse(null);

            if (friend != null && user.getFriends().contains(friend)) {
                user.getFriends().remove(friend);
                // Optional: Remove the user from the friend's list for bidirectional removal

                userRepo.save(user);

                userRepo.save(friend);
            }

            return "redirect:/profile";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }
    }

    @PostMapping("/profile")
    public String editingProfile(Model model, @ModelAttribute DataTransferObject dto){

        try{
            User user = userAuth();
            String bio = dto.getBio() != null && !StringUtils.isBlank(dto.getBio()) ? dto.getBio() : user.getBio();
            String firstName = dto.getFirstName() != null && !StringUtils.isBlank(dto.getFirstName()) ? dto.getFirstName() : user.getFirstName();
            String lastName = dto.getLastName() != null && !StringUtils.isBlank(dto.getLastName()) ? dto.getLastName() : user.getLastName();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setBio(bio);
            model.addAttribute("user", user);
            userRepo.save(user);
            // updates the "stylish" badge, showing the user has updated the profile
            BadgeCollection badgeCollection = badgeCollectionRepository.findByUserAndBadge(user, badgeRepository.findById(10));
            badgeCollection.updateCounter();
            badgeCollectionRepository.save(badgeCollection);
            return "redirect:/profile";
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "errors";
        }


    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        return userRepo.findByUsername(username).orElse(null);
    }


}
