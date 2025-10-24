package com.tradingsimulator.services;

import com.tradingsimulator.model.Account;

import java.security.Principal;
import java.util.List;

public interface AccountService {
    Account getAccountById(int id);
    Account getAccountByUser(Principal principal);
    List<Account> getAll();
    Account add(Account newAccount, Principal principal);
    int delete(int id, Principal principal);

    Account update(Account updateAccount, Principal principal);
}
