package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.dto.ListingSummary;
import com.example.ibm_project_code.repositories.ListingRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MarketController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ListingRepository listingRepository;

    @ModelAttribute
    public void addUserToModel(Model model) {
        User user = userAuth();
        Long id = user != null ? user.getId() : null;
        model.addAttribute("userId", id);
        model.addAttribute("user", user);
    }

    @GetMapping("/market")
    public String marketplace() {
        return "market";
    }

    @GetMapping("/market/listings")
    public String showListings(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListingSummary> listingsPage = listingRepository.findSummaryOfActiveListings(pageable);

        model.addAttribute("listingsPage", listingsPage);
        return "listings";
    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepo.findByUsername(username).orElse(null);
    }
}

