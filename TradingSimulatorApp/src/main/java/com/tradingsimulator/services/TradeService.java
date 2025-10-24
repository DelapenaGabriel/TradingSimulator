package com.tradingsimulator.services;

import com.tradingsimulator.model.Trade;

import java.security.Principal;
import java.util.List;

public interface TradeService {
    List<Trade> getTrades(Principal Principal);
    Trade create(Trade newTrade, Principal principal);
    Trade getTradeById(int id, Principal principal);
    Trade sellStock(String symbol, int quantity, Principal principal);

    Trade buyStock(String symbol, int quantity, Principal principal);

}
