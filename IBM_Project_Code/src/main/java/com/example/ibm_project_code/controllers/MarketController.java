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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpHeaders;

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
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               @RequestParam(name = "search", required = false) String search,
                               @RequestHeader HttpHeaders headers) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListingSummary> listingsPage;

        if (search != null && !search.trim().isEmpty()) {
            listingsPage = listingRepository.findBySearchTerm("%" + search.trim() + "%", pageable);
        } else {
            listingsPage = listingRepository.findSummaryOfActiveListings(pageable);
        }

        model.addAttribute("listingsPage", listingsPage);
        model.addAttribute("search", search); // Keep the search term in the model

        // Check if the request is an AJAX request by looking for the "X-Requested-With" header
        if ("XMLHttpRequest".equals(headers.getFirst("X-Requested-With"))) {
            // Return ONLY the listingsTableFragment
            return "fragments/listingsTableFragment";
        } else {
            // Return the full page for non-AJAX requests
            return "listings";
        }
    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepo.findByUsername(username).orElse(null);
    }
}

