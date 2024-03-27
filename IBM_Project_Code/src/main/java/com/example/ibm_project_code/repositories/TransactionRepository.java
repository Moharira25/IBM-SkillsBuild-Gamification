package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Item;
import com.example.ibm_project_code.database.Transaction;
import com.example.ibm_project_code.dto.ItemListingDetail;
import com.example.ibm_project_code.dto.ListingSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBuyerId(Long buyerId);

    List<Transaction> findBySellerId(Long sellerId);

    @Query("SELECT t FROM Transaction t WHERE t.item = :item AND t.price <= :price AND t.status = :status")
    List<Transaction> findMatchingSellOrders(@Param("item") Item item,
                                             @Param("price") BigDecimal price,
                                             @Param("status") Transaction.TransactionStatus status);

    // Find all sell orders for an item with a price less than or equal to the given price
    @Query("SELECT t FROM Transaction t WHERE t.item = :item AND t.price <= :price AND t.status = 'PENDING' AND t.orderType = 'SELL'")
    List<Transaction> findSellOrdersLessThanOrEqualToPrice(@Param("item") Item item, @Param("price") BigDecimal price);

    // Find the lowest sell order price for an item
    @Query("SELECT MIN(t.price) FROM Transaction t WHERE t.item = :item AND t.status = 'PENDING' AND t.orderType = 'SELL'")
    BigDecimal findLowestSellOrderPrice(@Param("item") Item item);

    // Find the highest buy request for an item
    @Query("SELECT MAX(t.price) FROM Transaction t WHERE t.item = :item AND t.status = 'PENDING' AND t.orderType = 'BUY'")
    BigDecimal findHighestBuyRequestPrice(@Param("item") Item item);

    // Optionally, find transactions for a specific item
    @Query("SELECT t FROM Transaction t WHERE t.item.name = :itemName")
    List<Transaction> findByItemName(@Param("itemName") String itemName);

    // Find the count of sell orders for an item
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.item = :item AND t.status = 'PENDING' AND t.orderType = 'SELL'")
    Long countSellOrdersForItem(@Param("item") Item item);

    // Find the count of buy requests for an item
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.item = :item AND t.status = 'PENDING' AND t.orderType = 'BUY'")
    Long countBuyRequestsForItem(@Param("item") Item item);

    // Aggregate transactions to get a summary, similar to active listings summary
    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(t.item.name, COUNT(t), MIN(t.price), t.item.category) " +
            "FROM Transaction t WHERE t.status = 'PENDING' AND t.orderType = 'SELL' GROUP BY t.item.name")
    Page<ListingSummary> findSummaryOfActiveTransactions(Pageable pageable);

    // Filter transactions by search term for item name or category
    @Query("SELECT new com.example.ibm_project_code.dto.ListingSummary(t.item.name, COUNT(t), MIN(t.price), t.item.category) " +
            "FROM Transaction t WHERE t.status = 'PENDING' AND t.orderType = 'SELL' AND " +
            "(t.item.name LIKE %:search% OR t.item.category LIKE %:search%) GROUP BY t.item.name, t.item.category")
    Page<ListingSummary> findBySearchTerm(@Param("search") String search, Pageable pageable);

    // Detail view of a specific item by name, showing the details from transactions
    @Query("SELECT new com.example.ibm_project_code.dto.ItemListingDetail(t.item.id, t.item.imageUrl, t.item.name, t.item.description, t.price, t.seller.username, t.transactionDate, t.item.rarity) " +
            "FROM Transaction t JOIN t.item i JOIN t.seller u WHERE i.name = :itemName AND t.status = 'PENDING' AND t.orderType = 'SELL'")
    List<ItemListingDetail> findDetailsByItemName(@Param("itemName") String itemName);

}

