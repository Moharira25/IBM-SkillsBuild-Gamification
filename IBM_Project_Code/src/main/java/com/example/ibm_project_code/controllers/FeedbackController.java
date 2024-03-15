//package com.example.ibm_project_code.controllers;
//
//import com.example.ibm_project_code.database.Feedback;
//import com.example.ibm_project_code.repositories.FeedbackRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//
//@RestController
//@RequestMapping("/feedback")
//public class FeedbackController {
//
//    @Autowired
//    private FeedbackRepository feedbackRepository;
//
//    @PostMapping("/submit")
//    public ResponseEntity<?> submitFeedback(@RequestBody Feedback feedback) {
//        feedbackRepository.save(feedback);
//        // Logic to reset the justCompleted flag should be here
//        return ResponseEntity.ok().body("Feedback submitted successfully");
//    }
//}
//
