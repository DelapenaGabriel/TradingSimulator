package com.tradingsimulator.services;

import com.tradingsimulator.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestStockService implements StockService{

    private final String API_BASE_URL = "https://finnhub.io/api/v1";

    private final String API_KEY = "cq4se7hr01qhh2h6gk4gcq4se7hr01qhh2h6gk50";

    private RestTemplate restTemplate = new RestTemplate();


    public StockInfo[] getStocks(){
        StockInfo[] stocks;

        try{
            ResponseEntity<StockInfo[]> response = restTemplate.exchange(API_BASE_URL + "/stock/symbol?exchange=US", HttpMethod.GET, makeAuthEntity(), StockInfo[].class);
            stocks = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) { //RestClientResponse handles all 400-599 status codes & ResourceAccessException handles Network I/O
            throw new RuntimeException("No available stocks found");
        }
        return stocks;

    }

    public StockProfile getStockProfile(String symbol){
        StockProfile stock;

        try{
            ResponseEntity<StockProfile> response = restTemplate.exchange(API_BASE_URL + "/stock/profile2?symbol=" + symbol, HttpMethod.GET, makeAuthEntity(), StockProfile.class);
            stock = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) { //RestClientResponse handles all 400-599 status codes & ResourceAccessException handles Network I/O
            throw new RuntimeException("Stock not found");
        }
        return stock;
    }


    public StockPrice getStockPrice(String symbol){
        StockPrice stock;

        try{
            ResponseEntity<StockPrice> response = restTemplate.exchange(API_BASE_URL + "/quote?symbol=" + symbol, HttpMethod.GET, makeAuthEntity(), StockPrice.class);
            stock = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) { //RestClientResponse handles all 400-599 status codes & ResourceAccessException handles Network I/O
            throw new RuntimeException("Stock not found");
        }
        return stock;
    }

    public Stock getStockBySymbol(String symbol){
        StockInfo[] stocks = getStocks();
        Stock s = new Stock();
        StockProfile profile = getStockProfile(symbol);
        StockPrice price = getStockPrice(symbol);

        for (StockInfo stock : stocks){
            if (stock.getSymbol().equalsIgnoreCase(symbol)){
                s.setName(stock.getName());
                s.setSymbol(stock.getSymbol());
                s.setPrice(price.getPrice());
                s.setCountry(profile.getCountry());
                s.setExchange(profile.getExchange());
                s.setIpo(profile.getIpo());
                s.setUrl(profile.getUrl());
                s.setLogo(profile.getLogo());
                s.setPercentChange(price.getPercentChange());
                s.setHighPrice(price.getHighestPrice());
                s.setLowPrice(price.getLowestPrice());
                s.setOpenPrice(price.getOpenPrice());
                s.setPreviousClose(price.getPreviousClose());
            }
        }
        return s;
    }

    private HttpEntity<String> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Finnhub-Token", API_KEY);
        return new HttpEntity<>(headers);
    }



}
