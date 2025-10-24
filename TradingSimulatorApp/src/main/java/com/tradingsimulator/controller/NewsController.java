package com.tradingsimulator.controller;

import com.tradingsimulator.model.News;
import com.tradingsimulator.services.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/news")
@RestController
public class NewsController {
    private NewsService newsService;
    public NewsController (NewsService newsService){
        this.newsService = newsService;
    }

    @GetMapping("")
    public News[] list(){
        try {
            News[] news = newsService.getNews();
            return news;
        }catch (RestClientResponseException | ResourceAccessException e) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
    }

}
