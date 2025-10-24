package com.tradingsimulator.services;

import com.tradingsimulator.model.*;

public interface StockService {
    StockInfo[] getStocks();
    StockPrice getStockPrice(String symbol);
    Stock getStockBySymbol(String symbol);
    StockProfile getStockProfile(String symbol);

}
