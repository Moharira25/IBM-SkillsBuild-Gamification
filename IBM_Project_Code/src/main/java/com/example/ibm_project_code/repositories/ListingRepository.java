package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Listing;
import com.example.ibm_project_code.dto.ListingSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByStatus(Listing.ListingStatus status);

    List<Listing> findBySellerId(Long sellerId);

    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(l.item.name, COUNT(l), MIN(l.price)) " +
           "FROM Listing l WHERE l.status = 'ACTIVE' GROUP BY l.item.name")
    Page<ListingSummary> findSummaryOfActiveListings(Pageable pageable);

    // add a new query method to search for listings by item name
    Page<ListingSummary> findByItemNameContainingIgnoreCase(String search, Pageable pageable);
}
