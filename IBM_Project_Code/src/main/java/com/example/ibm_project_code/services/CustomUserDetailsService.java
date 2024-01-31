package com.example.ibm_project_code.services;


import com.example.ibm_project_code.database.Role;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.RoleRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        // Convert your User entity to UserDetails
        assert getAuthorities(user) != null;
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Objects.requireNonNull(getAuthorities(user)));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    public void addTestUser() {
        // create 3 new roles and add one to the user
        Role role1 = new Role();
        role1.setName("USER");
        Role role2 = new Role();
        role2.setName("ADMIN");
        Role role3 = new Role();
        role3.setName("OWNER");

        User testUser = new User();
        testUser.setUsername("user");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("TestUser@testuser.com");
        testUser.setCreatedDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
        testUser.setLastLoginDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
        testUser.setEmailVerified(false);
        testUser.setEnabled(true);
        testUser.setLastModifiedDate(Timestamp.valueOf(java.time.LocalDateTime.now()));

        testUser.setRoles(Collections.singletonList(role1));

        userRepository.save(testUser);
        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);
    }

}
