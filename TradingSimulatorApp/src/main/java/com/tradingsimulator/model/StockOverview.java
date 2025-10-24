package com.tradingsimulator.model;

import java.util.Objects;

public class StockOverview {

   private String description;

   private long marketCap;

   private String address;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StockOverview() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOverview overview = (StockOverview) o;
        return marketCap == overview.marketCap && Objects.equals(description, overview.description) && Objects.equals(address, overview.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, marketCap, address);
    }
}
