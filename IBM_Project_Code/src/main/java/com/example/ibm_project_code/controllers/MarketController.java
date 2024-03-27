package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.Item;
import com.example.ibm_project_code.database.Transaction;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.database.UserItem;
import com.example.ibm_project_code.dto.ItemInfoDetail;
import com.example.ibm_project_code.dto.ItemListingDetail;
import com.example.ibm_project_code.dto.ListingSummary;
import com.example.ibm_project_code.exceptions.InsufficientFundsException;
import com.example.ibm_project_code.repositories.*;
import com.example.ibm_project_code.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class MarketController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private MarketService marketService;

    @ModelAttribute
    public void addUserToModel(Model model) {
        User user = userAuth();
        Long id = user != null ? user.getId() : null;
        model.addAttribute("userId", id);
        model.addAttribute("user", user);
    }

    @GetMapping("/market")
    public String marketplace() {
        return "market";
    }


    @GetMapping("/market/listings")
    public String showListings(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               @RequestParam(name = "search", required = false) String search,
                               @RequestHeader HttpHeaders headers) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListingSummary> listingsPage;

        if (search != null && !search.trim().isEmpty()) {
            // Use the same parameter for both name and category search
            listingsPage = transactionRepository.findBySearchTerm("%" + search.trim() + "%", pageable);
        } else {
            // No search term provided
            listingsPage = transactionRepository.findSummaryOfActiveTransactions(pageable);
        }

        model.addAttribute("listingsPage", listingsPage);
        model.addAttribute("search", search); // Keep the search term in the model

        if ("XMLHttpRequest".equals(headers.getFirst("X-Requested-With"))) {
            return "fragments/listingsTableFragment";
        } else {
            return "listings";
        }
    }


    @GetMapping("/market/item-listings")
    public String showItemSpecificListings(Model model, @RequestParam("itemName") String itemName) {
        List<ItemListingDetail> listings = transactionRepository.findDetailsByItemName(itemName);

        if (!listings.isEmpty()) {
            model.addAttribute("listings", listings); // Pass the detailed listings
        } else {
            model.addAttribute("errorMessage", "No listings found for item: " + itemName);
        }

        // Fetching item details using the service layer
        ItemInfoDetail itemDetails = marketService.getItemInfoDetail(itemName);
        model.addAttribute("itemDetails", itemDetails); // Add the itemDetail to the model

        // add userItems to model
        User user = userAuth();
        List<UserItem> userItems = userItemRepository.findByUser(user);
        model.addAttribute("userItems", userItems);

        System.out.println(itemDetails.getLowestSellOrder());
        System.out.println(itemDetails.getHighestBuyRequest());


        return "itemListings";
    }

    @PostMapping("/market/buy")
    public String processBuyOrder(Model model,
                                  @RequestParam("itemId") Long itemId,
                                  @RequestParam("price") BigDecimal price,
                                  @RequestParam("quantity") int quantity) {
        User user = userAuth();
        model.addAttribute("user", user);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

        try {
            Transaction buyOrder = placeBuyOrder(user, item, price, quantity);
            model.addAttribute("buyOrder", buyOrder);
        } catch (InsufficientFundsException e) {
            model.addAttribute("errorMessage", "Insufficient funds to complete the transaction.");
        }

        return "redirect:/market/listings";
    }

    @PostMapping("/market/sell")
    public String processSellOrder(Model model,
                                   @RequestParam("itemId") Long itemId,
                                   @RequestParam("price") BigDecimal price,
                                   @RequestParam("quantity") int quantity) {

        User user = userAuth();
        model.addAttribute("user", user);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));
        Transaction sellOrder = new Transaction();
        sellOrder.setOrderType(Transaction.OrderType.SELL);
        sellOrder.setSeller(user);
        sellOrder.setItem(item);
        sellOrder.setPrice(price);
        sellOrder.setQuantity(quantity);
        sellOrder.setStatus(Transaction.TransactionStatus.PENDING);
        transactionRepository.save(sellOrder);

        model.addAttribute("sellOrder", sellOrder);
        return "redirect:/market/listings";
    }

    public Transaction placeBuyOrder(User buyer, Item item, BigDecimal price, int quantity) throws InsufficientFundsException {
        // Check if the buyer has enough balance
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(quantity));
        if (buyer.getBalance().compareTo(totalCost) < 0) {
            throw new InsufficientFundsException(buyer.getBalance(), totalCost);
        }

        // Reserve or deduct buyer's balance
        buyer.setBalance(buyer.getBalance().subtract(totalCost));
        userRepo.save(buyer);

        // Create a new buy order transaction
        Transaction buyOrder = new Transaction();
        buyOrder.setOrderType(Transaction.OrderType.BUY);
        buyOrder.setBuyer(buyer);
        buyOrder.setItem(item);
        buyOrder.setPrice(price);
        buyOrder.setQuantity(quantity);
        buyOrder.setStatus(Transaction.TransactionStatus.PENDING);
        transactionRepository.save(buyOrder);

        // Match the buy order with existing sell orders
        // if not matched, the buy order will be pending
        matchWithSellOrders(buyOrder);

        return buyOrder;
    }

    private void matchWithSellOrders(Transaction buyOrder) {
        List<Transaction> matchingSellOrders = transactionRepository.findMatchingSellOrders(
                buyOrder.getItem(), buyOrder.getPrice(), Transaction.TransactionStatus.PENDING);

        for (Transaction sellOrder : matchingSellOrders) {
            if (sellOrder.getQuantity() >= buyOrder.getQuantity()) {
                // Execute the transaction partially or fully
                executeTransaction(buyOrder, sellOrder);

                // Update quantities or statuses as needed
                sellOrder.setQuantity(sellOrder.getQuantity() - buyOrder.getQuantity());
                transactionRepository.save(sellOrder);

                buyOrder.setStatus(Transaction.TransactionStatus.COMPLETED);
                transactionRepository.save(buyOrder);

                break;
            }
        }
    }

    private void executeTransaction(Transaction buyOrder, Transaction sellOrder) {
        // Get or create the UserItem record for the buyer
        UserItem buyerItem = userItemRepository.findByUserAndItem(buyOrder.getBuyer(), buyOrder.getItem())
                .orElseGet(() -> {
                    UserItem newItem = new UserItem();
                    newItem.setUser(buyOrder.getBuyer());
                    newItem.setItem(buyOrder.getItem());
                    return newItem;
                });
        // Update the quantity for the buyer
        buyerItem.setQuantity(buyerItem.getQuantity() + buyOrder.getQuantity());
        userItemRepository.save(buyerItem);

        // Get or decrease the UserItem record for the seller
        // include a check for if the order should just be partially fulfilled
        // also check if the buyer simply want to put a request out for a lower price, it doesnt have to be bought immediately
        UserItem sellerItem = userItemRepository.findByUserAndItem(sellOrder.getSeller(), sellOrder.getItem())
                .orElseThrow(() -> new IllegalStateException("Seller does not have the item to sell."));

        // Check if the seller has enough quantity to sell
        if (sellerItem.getQuantity() < sellOrder.getQuantity()) {
            throw new IllegalStateException("Seller does not have enough quantity to sell.");
        }
        // Update the quantity for the seller
        sellerItem.setQuantity(sellerItem.getQuantity() - sellOrder.getQuantity());
        userItemRepository.save(sellerItem);

        // Transfer the funds from the buyer to the seller
        BigDecimal totalCost = sellOrder.getPrice().multiply(BigDecimal.valueOf(sellOrder.getQuantity()));
        sellOrder.getSeller().setBalance(sellOrder.getSeller().getBalance().add(totalCost));
        buyOrder.getBuyer().setBalance(buyOrder.getBuyer().getBalance().subtract(totalCost));
        userRepo.save(sellOrder.getSeller());
        userRepo.save(buyOrder.getBuyer());

        // Mark both transactions as completed
        buyOrder.setStatus(Transaction.TransactionStatus.COMPLETED);
        sellOrder.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionRepository.save(buyOrder);
        transactionRepository.save(sellOrder);

        // Handle the case where the sell order is only partially fulfilled
        if (sellOrder.getQuantity() > buyOrder.getQuantity()) {
            Transaction newSellOrder = new Transaction();
            newSellOrder.setOrderType(Transaction.OrderType.SELL);
            newSellOrder.setSeller(sellOrder.getSeller());
            newSellOrder.setItem(sellOrder.getItem());
            newSellOrder.setPrice(sellOrder.getPrice());
            newSellOrder.setQuantity(sellOrder.getQuantity() - buyOrder.getQuantity());
            newSellOrder.setStatus(Transaction.TransactionStatus.PENDING);
            transactionRepository.save(newSellOrder);
        }
    }


    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepo.findByUsername(username).orElse(null);
    }
}

