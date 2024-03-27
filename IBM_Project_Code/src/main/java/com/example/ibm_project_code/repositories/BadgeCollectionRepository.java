package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Badge;
import com.example.ibm_project_code.database.BadgeCollection;
import com.example.ibm_project_code.database.User;
import org.springframework.data.repository.CrudRepository;

public interface BadgeCollectionRepository extends CrudRepository<BadgeCollection, Integer> {
    public BadgeCollection findById(int id);

    public BadgeCollection findByUserAndBadge(User user, Badge badge);

    public BadgeCollection findByUser(User user);
}
