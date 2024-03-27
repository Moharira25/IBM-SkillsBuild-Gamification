package com.example.ibm_project_code.dto;

import com.example.ibm_project_code.database.Item;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class ItemListingDetail {
    private Long id;
    private String imageUrl;
    private String itemName;
    private String description;
    private BigDecimal price;
    private String sellerName;
    private LocalDate listedOn;
    private Item.Rarity rarity; // Using Rarity enum directly

    // Constructor that accepts Rarity as an enum
    public ItemListingDetail(Long id, String imageUrl, String itemName, String description, BigDecimal price, String sellerName, Timestamp listedOnTimestamp, Item.Rarity rarity) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.sellerName = sellerName;
        this.listedOn = listedOnTimestamp.toLocalDateTime().toLocalDate();
        this.rarity = rarity; // Accepting Rarity enum directly
    }
}


