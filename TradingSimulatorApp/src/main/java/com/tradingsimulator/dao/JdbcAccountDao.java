package com.tradingsimulator.dao;

import com.tradingsimulator.exception.DaoException;
import com.tradingsimulator.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT id, user_id, balance " +
                "FROM account;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
            while(result.next()){
                accounts.add(mapRowToAccount(result));
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }

    @Override
    public Account getAccountById(int id) {
        Account account = null;

        String sql = "SELECT id, user_id, balance " +
                "FROM account " +
                "WHERE id = ?;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
            if (result.next()){
                account = mapRowToAccount(result);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;

        String sql = "SELECT id, user_id, balance " +
                "FROM account " +
                "WHERE user_id = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            if (result.next()){
                account = mapRowToAccount(result);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public Account updateAccount(Account updateAccount) {
        Account account = null;

        String sql = "UPDATE account " +
                "SET balance = ? WHERE id = ?;";

        try{
            int rowsAffected = jdbcTemplate.update(sql, updateAccount.getBalance(), updateAccount.getId());
            if (rowsAffected == 0){
                throw new DaoException("Zero rows affected, expected at least one");
            }
            account = getAccountById(updateAccount.getId());
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return account;
    }

    @Override
    public Account createAccount(Account newAccount) {
        Account account = null;
        int newId;

        String sql = "INSERT INTO account (user_id, balance) " +
                "VALUES (?,?) RETURNING id;";


        try{
            newId = jdbcTemplate.queryForObject(sql, int.class, newAccount.getUserId(), newAccount.getBalance());
            account = getAccountById(newId);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return account;
    }

    @Override
    public int deleteAccount(int id) {
        String sql = "DELETE FROM account WHERE id = ?;";
        int rowsDeleted = 0;
        try{
            rowsDeleted = jdbcTemplate.update(sql, id);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return rowsDeleted;
    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setId(rowSet.getInt("id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
