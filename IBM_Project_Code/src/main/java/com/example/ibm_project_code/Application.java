package com.example.ibm_project_code;

import com.example.ibm_project_code.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public static void main(String[] args) {
        SpringApplication.run(IbmProjectCodeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        userDetailsService.addTestUser();
    }
}
