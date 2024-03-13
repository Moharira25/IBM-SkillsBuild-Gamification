package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public List<Review> getReviewsBy();
}
