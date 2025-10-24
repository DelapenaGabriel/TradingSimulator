package com.tradingsimulator.dao;

import com.tradingsimulator.exception.DaoException;
import com.tradingsimulator.model.Trade;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcTradeDao implements TradeDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTradeDao (DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Trade> getTradesByUsername(String username) {
        List<Trade> trades = new ArrayList<>();

        String sql = "SELECT t.id, t.user_id, t.symbol, t.trade_type, t.quantity, t.price, t.trade_date " +
                "FROM trade t " +
                "JOIN users u ON t.user_id = u.id " +
                "WHERE u.username = ? " +
                "ORDER BY t.trade_date DESC;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            while (result.next()){
                trades.add(mapRowToTrade(result));
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trades;
    }

    @Override
    public Trade createTrade(Trade newTrade) {
        Trade trade = null;
        int newId;

        String sql = "INSERT INTO trade (user_id, symbol, trade_type, quantity, price, trade_date) " +
                "VALUES (?,?,?,?,?,?) RETURNING id;";

        try{
            newId = jdbcTemplate.queryForObject(sql, int.class, newTrade.getUserId(), newTrade.getSymbol(), newTrade.getTradeType(), newTrade.getQuantity(), newTrade.getPrice(), newTrade.getTradeDate());
            trade = getTradeById(newId);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trade;
    }

    @Override
    public Trade getTradeById(int id) {
        Trade trade = null;
        String sql = "SELECT * FROM trade " +
                "WHERE id = ?;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
            if (result.next()){
                trade = mapRowToTrade(result);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trade;
    }

    private Trade mapRowToTrade(SqlRowSet rowSet){
        Trade trade = new Trade();
        trade.setId(rowSet.getInt("id"));
        trade.setUserId(rowSet.getInt("user_id"));
        trade.setSymbol(rowSet.getString("symbol"));
        trade.setTradeType(rowSet.getString("trade_type"));
        trade.setQuantity(rowSet.getInt("quantity"));
        trade.setPrice(rowSet.getBigDecimal("price"));
        trade.setTradeDate(rowSet.getTimestamp("trade_date").toLocalDateTime());
        return trade;
    }
}
