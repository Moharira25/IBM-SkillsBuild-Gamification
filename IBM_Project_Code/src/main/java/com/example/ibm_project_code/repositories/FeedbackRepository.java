package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUserId(Long userId);
    List<Feedback> findByCourseId(int course_id);
}
