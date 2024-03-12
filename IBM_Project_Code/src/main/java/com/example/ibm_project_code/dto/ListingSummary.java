package com.example.ibm_project_code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingSummary {
    private String itemName;
    private long quantity;
    private double minPrice;

    public ListingSummary(String itemName, long quantity, double minPrice) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.minPrice = minPrice;
    }

}
