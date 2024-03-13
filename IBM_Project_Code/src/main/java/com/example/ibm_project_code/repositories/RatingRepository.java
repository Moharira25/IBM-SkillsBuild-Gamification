package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Rating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating,Integer> {
    public List<Rating> getRatingsBy();
}
