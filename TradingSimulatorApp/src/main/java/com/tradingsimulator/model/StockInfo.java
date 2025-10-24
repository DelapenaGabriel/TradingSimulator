package com.tradingsimulator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class StockInfo {
    @JsonProperty("description")
    private String name;

    @JsonProperty("displaySymbol")
    private String symbol;

    private String figi;

    private String mic;

    private String type;

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

    public String getFigi() {
        return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockInfo stockInfo = (StockInfo) o;
        return Objects.equals(name, stockInfo.name) && Objects.equals(symbol, stockInfo.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol);
    }
}
