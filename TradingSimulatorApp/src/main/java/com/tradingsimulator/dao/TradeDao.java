package com.tradingsimulator.dao;

import com.tradingsimulator.model.Trade;

import java.util.List;

public interface TradeDao {
    List<Trade> getTradesByUsername(String username);
    Trade createTrade(Trade newTrade);

    Trade getTradeById(int id);
}
