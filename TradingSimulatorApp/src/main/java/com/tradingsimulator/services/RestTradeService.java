package com.tradingsimulator.services;

import com.tradingsimulator.dao.TradeDao;
import com.tradingsimulator.dao.UserDao;
import com.tradingsimulator.model.Account;
import com.tradingsimulator.model.Stock;
import com.tradingsimulator.model.Trade;
import com.tradingsimulator.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestTradeService implements TradeService {

    private AccountService accountService;
    private UserDao userDao;
    private TradeDao tradeDao;
    private StockService stockService;

    public RestTradeService (TradeDao tradeDao, UserDao userDao, AccountService accountService, StockService stockService){
        this.tradeDao = tradeDao;
        this.userDao = userDao;
        this.accountService = accountService;
        this.stockService = stockService;
    }

    @Override
    public List<Trade> getTrades(Principal principal) {
        User user = getUser(principal);

        if (user != null) {
            return tradeDao.getTradesByUsername(user.getUsername());
        }else{
            throw new AccessDeniedException("Access denied"); //Access check, does not have permission
        }
    }

    @Override
    public Trade create(Trade newTrade, Principal principal) {
        Trade trade = null;
        User user = getUser(principal);

        newTrade.setUserId(user.getId());
        trade = tradeDao.createTrade(newTrade);

        return trade;

    }

    @Override
    public Trade getTradeById(int id, Principal principal) {
        Trade trade = tradeDao.getTradeById(id);
        User user = getUser(principal);

        if(trade.getUserId() == user.getId()){
            return trade;
        }
        else{
            throw new AccessDeniedException("Access denied"); //Access check, does not have permission
        }
    }

    @Transactional
    public Trade buyStock(String symbol, int quantity, Principal principal){
        Stock stock = stockService.getStockBySymbol(symbol);

        User user = userDao.getUserByUsername(principal.getName());

        String buy = "BUY";

        if (stock == null){
            throw new RuntimeException("Stock not found for this symbol");
        }
        BigDecimal shares = BigDecimal.valueOf(quantity);
        BigDecimal stockPrice = stock.getPrice();
        BigDecimal totalCost = stockPrice.multiply(shares);

        Account account = accountService.getAccountByUser(principal);

        if (account.getBalance().compareTo(totalCost) < 0){
            throw new RuntimeException("Insufficient Balance"); //Handles error when user tries buying a stock that cost more than account balance
        }

        account.setBalance(account.getBalance().subtract(totalCost));
        accountService.update(account, principal);

        Trade trade = new Trade();
        trade.setUserId(user.getId());
        trade.setSymbol(symbol);
        trade.setQuantity(quantity);
        trade.setPrice(stockPrice);
        trade.setTradeType(buy);
        trade.setTradeDate(LocalDateTime.now());

        return tradeDao.createTrade(trade);

    }

    @Transactional
    public Trade sellStock(String symbol, int quantity, Principal principal){
        User user = userDao.getUserByUsername(principal.getName());

        Stock stock = stockService.getStockBySymbol(symbol);

        if (stock == null){
            throw new RuntimeException("Stock not found for this symbol");
        }

        BigDecimal shares = BigDecimal.valueOf(quantity);
        BigDecimal stockPrice = stock.getPrice();
        BigDecimal totalCost = stockPrice.multiply(shares);

        Account account = accountService.getAccountByUser(principal);

        account.setBalance(account.getBalance().add(totalCost));
        accountService.update(account, principal);

        String sell = "SELL";

        Trade trade = new Trade();
        trade.setUserId(user.getId());
        trade.setSymbol(symbol);
        trade.setQuantity(quantity);
        trade.setPrice(stockPrice);
        trade.setTradeType(sell);
        trade.setTradeDate(LocalDateTime.now());

        return tradeDao.createTrade(trade);
    }

    private User getUser(Principal principal){
        return userDao.getUserByUsername(principal.getName());
    }

}
