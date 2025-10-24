package com.tradingsimulator.dao;

import com.tradingsimulator.model.Trade;
import com.tradingsimulator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JdbcTradeDaoTests extends BaseDaoTests{

    private static final Trade TRADE_1 = new Trade(1, 1, "TSLA", "BUY" ,10, new BigDecimal("10.00"), LocalDateTime.parse("2007-12-03T10:15:30"));
    private static final Trade TRADE_2 = new Trade(2, 1, "TSLA", "SELL" ,10, new BigDecimal("30.00"), LocalDateTime.parse("2007-12-03T10:15:30"));
    private static final Trade TRADE_3 = new Trade(3, 2, "AAPL", "BUY" ,10, new BigDecimal("100.00"), LocalDateTime.parse("2007-12-03T10:15:30"));


    private static final User USER_1 = new User(1, "user1", "user1", "ROLE_USER");
    private JdbcTradeDao dao;

    @Before
    public void setup(){
        dao = new JdbcTradeDao(dataSource);
    }

    public void getTradesByUsername_with_valid_username_returns_trades(){
        List<Trade> trade = dao.getTradesByUsername(USER_1.getUsername());

        Assert.assertNotNull("getTradeByUsername return null list", trade);
        assertTradeMatch("getTradeByUsername returned incorrect trade", TRADE_1, trade.get(0));
        assertTradeMatch("getTradeByUsername returned incorrect trade", TRADE_2, trade.get(1));
    }

    @Test
    public void getTradesByUsername_with_invalid_username_returns_empty(){
        List<Trade> trade = dao.getTradesByUsername("iusernamei");

        Assert.assertEquals("getTradeByUsername return trades", 0, trade.size());
    }

    @Test
    public void getTradeById_with_valid_id_returns_trade(){
        Trade trade = dao.getTradeById(1);

        Assert.assertNotNull("getTradeById return null trade", trade);
        assertTradeMatch("getTradeById return incorrect trade", TRADE_1, trade);
    }

    @Test
    public void createTrade_creates_trade(){
        Trade newTrade = new Trade(1, "GME", "BUY", 15, new BigDecimal("12.50"), LocalDateTime.now());

        Trade trade = dao.createTrade(newTrade);
        Assert.assertNotNull("createTrade return null trade", trade);

        Trade actualTrade = dao.getTradeById(trade.getId());
        Assert.assertNotNull("getTradeById return null", actualTrade);

        newTrade.setId(actualTrade.getId());
        assertTradeMatch("Trades do not match", newTrade, actualTrade);
    }

    private void assertTradeMatch(String message, Trade expected, Trade actual){
        Assert.assertEquals(message, expected.getId(), actual.getId());
        Assert.assertEquals(message, expected.getUserId(), actual.getUserId());
        Assert.assertEquals(message, expected.getSymbol(), actual.getSymbol());
        Assert.assertEquals(message, expected.getTradeType(), actual.getTradeType());
        Assert.assertEquals(message, expected.getQuantity(), actual.getQuantity());
        Assert.assertEquals(message, expected.getPrice(), actual.getPrice());
    }



}

