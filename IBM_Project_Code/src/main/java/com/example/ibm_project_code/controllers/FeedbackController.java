package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.Course;
import com.example.ibm_project_code.database.Feedback;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.repositories.FeedbackRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/submitFeedback/{courseId}")
    public String submitFeedback(@ModelAttribute Feedback feedback, @PathVariable int courseId, // Receive courseId as parameter
                                 RedirectAttributes redirectAttributes) {

        User user = userAuth();
        Course course = courseRepository.findById(courseId);
        if (user != null && course != null) { // Make sure both user and course are found
            feedback.setCourse(course);
            feedback.setUser(user);
            feedbackRepository.save(feedback);
            redirectAttributes.addFlashAttribute("successMessage", "Feedback submitted successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Feedback submission failed. User must be logged in and course must exist.");
        }
        return "redirect:/dashboard";
    }

    @ModelAttribute
    public void addUserToModel(Model model) {
        User user = userAuth();
        model.addAttribute("user", user);
    }

    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}