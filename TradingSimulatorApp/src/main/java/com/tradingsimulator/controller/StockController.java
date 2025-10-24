package com.tradingsimulator.controller;

import com.tradingsimulator.model.Stock;
import com.tradingsimulator.model.StockInfo;
import com.tradingsimulator.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/stocks")
@RestController
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService){

        this.stockService = stockService;
    }

    @GetMapping("")
    public StockInfo[] list(){
        StockInfo[] stocks = stockService.getStocks();
        if (stocks != null && stocks.length > 0){
            return stocks;
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error encountered."); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
    }

    @GetMapping("/{symbol}")
    public Stock get(@PathVariable String symbol){
        Stock stock = stockService.getStockBySymbol(symbol);
        if (stock == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Symbol not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
        }else{
            return stock;
        }
    }

}
