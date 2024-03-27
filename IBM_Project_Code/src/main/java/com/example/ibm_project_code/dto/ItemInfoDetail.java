package com.example.ibm_project_code.dto;

import com.example.ibm_project_code.database.Item;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class ItemInfoDetail extends ItemListingDetail {
    private BigDecimal lowestSellOrder;
    private Integer sellOrderCount;
    private BigDecimal highestBuyRequest;
    private Integer buyRequestCount;

    public ItemInfoDetail(Long id, String imageUrl, String itemName, String description, BigDecimal price, String sellerName, LocalDate listedOn, Item.Rarity rarity, BigDecimal lowestSellOrder, Integer sellOrderCount, BigDecimal highestBuyRequest, Integer buyRequestCount) {
        super(
                id,
                imageUrl,
                itemName,
                description,
                price,
                sellerName,
                Timestamp.valueOf((listedOn != null ? listedOn : LocalDate.of(2000, 1, 1)).atStartOfDay()), // Set default to Jan 1, 2000
                rarity
        );

        this.lowestSellOrder = lowestSellOrder;
        this.sellOrderCount = sellOrderCount;
        this.highestBuyRequest = highestBuyRequest;
        this.buyRequestCount = buyRequestCount;
    }

}
