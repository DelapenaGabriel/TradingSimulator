package com.tradingsimulator.services;

import com.tradingsimulator.dao.AccountDao;
import com.tradingsimulator.dao.UserDao;
import com.tradingsimulator.model.Account;
import com.tradingsimulator.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class RestAccountService implements AccountService{
    private AccountDao accountDao;
    private UserDao userDao;
    public RestAccountService(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public Account getAccountById(int id) {
        return accountDao.getAccountById(id);
    }

    @Override
    public Account getAccountByUser(Principal principal) {
        User user = getUser(principal);

        Account account = accountDao.getAccountByUserId(user.getId());

        if (account == null){
            return null;
        }

        if (user.getId() == account.getUserId()){
            return account;
        }else{
            throw new AccessDeniedException("Access denied"); //Access check, does not have permission
        }
    }

    @Override
    public List<Account> getAll() {
        return accountDao.getAllAccounts();
    }

    @Override
    public Account add(Account newAccount, Principal principal) {
        Account account = null;

        User user = getUser(principal);

        if (isAdmin(principal)){
            account = accountDao.createAccount(newAccount);
        }else{
            newAccount.setUserId(user.getId());
            account = accountDao.createAccount(newAccount);
        }
        return account;
    }

    @Override
    public int delete(int id, Principal principal) {
        User user = getUser(principal);
        int deletedRows = 0;

        if (user.getRole().equalsIgnoreCase("ROLE_ADMIN")){
            deletedRows = accountDao.deleteAccount(id);
        }else{
            throw new AccessDeniedException("Access denied"); //Access check, does not have permission
        }
        return deletedRows;
    }

    @Override
    public Account update(Account updateAccount, Principal principal) {
        Account account = null;

        Account existingAccount = accountDao.getAccountById(updateAccount.getId());
        if (existingAccount != null){
            User user = getUser(principal);

            if (existingAccount.getUserId() == user.getId()){
                updateAccount.setUserId(existingAccount.getUserId());
                account = accountDao.updateAccount(updateAccount);
            }
            else{
                throw new AccessDeniedException("Access Denied"); //Access check, does not have permission
            }

        }
        return account;

    }

    private User getUser(Principal principal){
        return userDao.getUserByUsername(principal.getName());
    }

    private boolean isAdmin(Principal principal){
        User user = getUser(principal);
        return user.getRole().equalsIgnoreCase("ROLE_ADMIN");
    }
}
