package com.tradingsimulator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class TradeDto {
    @NotBlank
    private String symbol;
    @Positive
    private int quantity;

    public TradeDto(String symbol, int quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public TradeDto() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
