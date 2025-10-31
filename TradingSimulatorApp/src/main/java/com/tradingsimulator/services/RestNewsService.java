package com.tradingsimulator.services;

import com.tradingsimulator.model.News;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestNewsService implements NewsService{
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${news.api.url}")
    private String API_URL;

    @Value("${news.api.key}")
    private String API_KEY;

    public News[] getNews(){
        News[] news = null;
        try{
            ResponseEntity<News[]> response = restTemplate.exchange(API_URL, HttpMethod.GET, makeAuthEntity(), News[].class);
            news = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) { //RestClientResponse handles all 400-599 status codes & ResourceAccessException handles Network I/O
            e.printStackTrace();
        }
        return news;
    }


    private HttpEntity<String> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Finnhub-Token", API_KEY);
        return new HttpEntity<>(headers);
    }



}
