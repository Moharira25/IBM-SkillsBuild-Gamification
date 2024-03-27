package com.example.ibm_project_code.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ListingSummary {
    private String itemName;
    private long quantity;
    private BigDecimal minPrice;
    private String category;

    public ListingSummary(String itemName, long quantity, BigDecimal minPrice, String category) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.minPrice = minPrice;
        this.category = category;
    }

}
