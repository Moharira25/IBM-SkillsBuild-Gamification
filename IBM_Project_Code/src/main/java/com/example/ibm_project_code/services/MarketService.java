package com.example.ibm_project_code.services;

import com.example.ibm_project_code.database.Item;
import com.example.ibm_project_code.database.Transaction;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.dto.ItemInfoDetail;
import com.example.ibm_project_code.repositories.ItemRepository;
import com.example.ibm_project_code.repositories.ListingRepository;
import com.example.ibm_project_code.repositories.TransactionRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketService {
    // Autowire necessary repositories

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Business logic for placing a buy order

    public ItemInfoDetail getItemInfoDetail(String itemName) {
        List<Transaction> transactions = transactionRepository.findByItemName(itemName);

        // Assuming Transaction has a method to differentiate buy/sell
        List<Transaction> sellOrders = transactions.stream()
                .filter(t -> t.getOrderType().equals(Transaction.OrderType.SELL))
                .toList();

        List<Transaction> buyOrders = transactions.stream()
                .filter(t -> t.getOrderType().equals(Transaction.OrderType.BUY))
                .toList();

        BigDecimal lowestSellOrder = sellOrders.stream()
                .map(Transaction::getPrice) // Extract prices
                .min(BigDecimal::compareTo) // Natural ordering for BigDecimal
                .orElse(BigDecimal.ZERO);

        int sellOrderCount = sellOrders.size();

        BigDecimal highestBuyRequest = buyOrders.stream()
                .max(Comparator.comparing(Transaction::getPrice))
                .map(Transaction::getPrice)
                .orElse(BigDecimal.ZERO); // Defaulting to 0 if no buy orders

        int buyRequestCount = buyOrders.size();

        // Fetch additional item details required to populate the DTO
        // For example, fetching the item details from itemRepository
        Item item = itemRepository.findByName(itemName).orElseThrow(() -> new RuntimeException("Item not found"));

        // Assuming you have a method or a constructor in your DTO to set these values
        ItemInfoDetail itemInfoDetail = new ItemInfoDetail(
                item.getId(),
                item.getImageUrl(),
                item.getName(),
                item.getDescription(),
                null, // price isn't directly relevant here
                null, // sellerName isn't directly relevant here
                null, // listedOn isn't directly relevant here
                item.getRarity(),
                lowestSellOrder,
                sellOrderCount,
                highestBuyRequest,
                buyRequestCount
        );

        return itemInfoDetail;
    }
}

