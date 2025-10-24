package com.tradingsimulator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class StockProfile {
    private String country;
    private String exchange;
    private LocalDate ipo;
    @JsonProperty("weburl")
    private String url;
    private String logo;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockProfile that = (StockProfile) o;
        return Objects.equals(country, that.country) && Objects.equals(exchange, that.exchange) && Objects.equals(ipo, that.ipo) && Objects.equals(url, that.url) && Objects.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, exchange, ipo, url, logo);
    }
}
