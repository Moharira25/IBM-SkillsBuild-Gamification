package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.FriendRequest;
import com.example.ibm_project_code.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, String status);
    List<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findByReceiverAndStatusOrderBySentAtDesc(User receiver, String status);

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, String status);
}