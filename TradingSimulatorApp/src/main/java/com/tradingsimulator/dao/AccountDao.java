package com.tradingsimulator.dao;

import com.tradingsimulator.model.Account;

import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

    Account getAccountById(int id);
    Account getAccountByUserId(int userId);

    Account updateAccount(Account updateAccount);

    Account createAccount(Account newAccount);

    int deleteAccount(int id);
}
