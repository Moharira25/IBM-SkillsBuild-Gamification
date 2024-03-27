package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Item;
import com.example.ibm_project_code.database.Listing;
import com.example.ibm_project_code.dto.ItemListingDetail;
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
//    List<Listing> findByStatus(Listing.ListingStatus status);
//
//    List<Listing> findBySellerId(Long sellerId);
//
//    List<Listing> findByItem(Item item);

//    private String imageUrl;
//    private String itemName;
//    private double price;
//    private String sellerName;
//    private String createDate;
//    private String rarity;
//
//    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(l.item.name, COUNT(l), MIN(l.price), l.item.category) " + "FROM Listing l WHERE l.status = 'ACTIVE' GROUP BY l.item.name")
//    Page<ListingSummary> findSummaryOfActiveListings(Pageable pageable);
//
//    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(l.item.name, COUNT(l), MIN(l.price), l.item.category) " + "FROM Listing l WHERE l.status = 'ACTIVE' AND (l.item.name LIKE %:search% OR l.item.category LIKE %:search%) GROUP BY l.item.name, l.item.category")
//    Page<ListingSummary> findBySearchTerm(@Param("search") String search, Pageable pageable);
//
//
//    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(l.item.name, COUNT(l), MIN(l.price), l.item.category) " + "FROM Listing l WHERE l.status = 'ACTIVE' AND l.item.category LIKE %:category% GROUP BY l.item.name")
//    Page<ListingSummary> findSummaryOfActiveListingsByCategory(@Param("category") String category, Pageable pageable);
//
//    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(l.item.name, COUNT(l), MIN(l.price), l.item.category) " + "FROM Listing l WHERE l.status = 'ACTIVE' AND l.item.name LIKE %:search% AND l.item.category LIKE %:category% GROUP BY l.item.name")
//    Page<ListingSummary> findBySearchTermAndCategory(@Param("search") String search, @Param("category") String category, Pageable pageable);
//
//    @Query("SELECT new com.example.ibm_project_code.dto.ItemListingDetail(l.item.imageUrl, l.item.name, l.item.description, l.price, u.username, l.createDate, l.item.rarity) " +
//            "FROM Listing l JOIN l.item i JOIN l.seller u WHERE i.name = :itemName")
//    List<ItemListingDetail> findDetailsByItemName(@Param("itemName") String itemName);


}
