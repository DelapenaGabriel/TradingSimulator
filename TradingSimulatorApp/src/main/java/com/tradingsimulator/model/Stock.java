package com.tradingsimulator.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Stock {

    private String country;
    private String exchange;

    private String name;

    private String symbol;
    private LocalDate ipo;
    private String url;
    private String logo;

    private BigDecimal price;
    private double percentChange;
    private double highPrice;
    private double lowPrice;
    private double openPrice;

    private double previousClose;



    public Stock(String name, String symbol, BigDecimal price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public Stock(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }


    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public LocalDate getIpo() {
        return ipo;
    }

    public void setIpo(LocalDate ipo) {
        this.ipo = ipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public Stock() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(name, stock.name) && Objects.equals(symbol, stock.symbol) && Objects.equals(price, stock.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol, price);
    }

    @Override
    public String toString() {
        return  "name = " + name + "\n" +
                "symbol = " + symbol + "\n" +
                "price = " + price;
    }
}
