package com.tradingsimulator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Trade {
    private int id;
    private int userId;
    @NotBlank(message = "Symbol must not be blank")
    private String symbol;
    @NotBlank(message = "Trade type must not be blank")
    private String tradeType;
    @Positive (message = "Quantity must be greater than 0")
    private int quantity;
    @Positive (message = "Price must be greater than 0")
    private BigDecimal price;

    //Default to current timestamp
    private LocalDateTime tradeDate;

    public Trade() {
    }

    public Trade(int userId, String symbol, String tradeType, int quantity, BigDecimal price, LocalDateTime tradeDate) {
        this.userId = userId;
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.tradeDate = tradeDate;
    }

    public Trade(int id, int userId, String symbol, String tradeType, int quantity, BigDecimal price, LocalDateTime tradeDate) {
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.tradeDate = tradeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return id == trade.id && userId == trade.userId && quantity == trade.quantity && Objects.equals(symbol, trade.symbol) && Objects.equals(tradeType, trade.tradeType) && Objects.equals(price, trade.price) && Objects.equals(tradeDate, trade.tradeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, symbol, tradeType, quantity, price, tradeDate);
    }


    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", userId=" + userId +
                ", symbol='" + symbol + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", tradeDate=" + tradeDate +
                '}';
    }
}
