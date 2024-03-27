package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Item;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.database.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    /**
     * Finds a UserItem by its user and item.
     *
     * @param user the user owning the item.
     * @param item the item to find.
     * @return an Optional containing the UserItem if found, or empty otherwise.
     */
    Optional<UserItem> findByUserAndItem(User user, Item item);

    /**
     * Finds all UserItems owned by a specific user.
     *
     * @param user the user whose items to find.
     * @return a list of UserItem objects.
     */
    List<UserItem> findByUser(User user);

    UserItem findByUserIdAndItemId(Long id, Long id1);
}
