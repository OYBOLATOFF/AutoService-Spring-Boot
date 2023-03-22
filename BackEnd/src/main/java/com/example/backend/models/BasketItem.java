package com.example.backend.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class BasketItem implements Serializable {
    private String title;
    private Double priceWithoutDiscount;
    private Double priceWithDiscount;
    private int amount;
    private Double totalCost;

    public BasketItem(String title, Double priceWithoutDiscount, Double priceWithDiscount, int amount) {
        this.title = title;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.priceWithDiscount = priceWithDiscount;
        this.amount = amount;
        this.totalCost = Double.parseDouble(
                String.format("%.2f", ((priceWithDiscount == null)? priceWithoutDiscount: priceWithDiscount)*amount).replace(',', '.')
        );
    }
}
