package com.tradingsimulator.controller;

import com.tradingsimulator.exception.DaoException;
import com.tradingsimulator.model.Trade;
import com.tradingsimulator.model.TradeDto;
import com.tradingsimulator.services.TradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/trades")
@RestController
public class TradeController {
    private TradeService tradeService;
    public TradeController (TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("")
    public List<Trade> listTrades(Principal principal){
        List<Trade> trades;
        try{
            trades = tradeService.getTrades(principal);
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return trades;
    }

    @GetMapping("/{id}")
    public Trade getTrade(@PathVariable int id, Principal principal){
        Trade trade;

        try{
            trade = tradeService.getTradeById(id, principal);
            if (trade == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trade not found");
            }
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return trade;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Trade addTrade(@Valid @RequestBody Trade newTrade, Principal principal){
        Trade trade = null;
        try{
            trade = tradeService.create(newTrade, principal);
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return trade;
    }

    @PostMapping("/buy")

    public Trade buy(@Valid @RequestBody TradeDto tradeDto, Principal principal) {
        Trade trade = tradeService.buyStock(tradeDto.getSymbol(), tradeDto.getQuantity(), principal);
        if (trade != null) {
            return trade;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error encountered."); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
    }
    @PostMapping("/sell")
    public Trade sell(@Valid @RequestBody TradeDto tradeDto,Principal principal){
        Trade trade = tradeService.sellStock(tradeDto.getSymbol(), tradeDto.getQuantity(), principal);
        if (trade != null){
            return trade;
        }else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error encountered."); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
    }


}
