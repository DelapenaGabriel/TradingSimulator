package com.tradingsimulator.dao;

import com.tradingsimulator.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests{
    private static final Account ACCOUNT_1 = new Account(1, 1, new BigDecimal("10000.00"));
    private static final Account ACCOUNT_2 = new Account(2,2,new BigDecimal("20000.00"));



    private JdbcAccountDao dao;

    @Before
    public void setup(){
        dao = new JdbcAccountDao(dataSource);
    }

    @Test
    public void getAllAccounts_returns_all_account(){
        List<Account> accounts = dao.getAllAccounts();
        Assert.assertNotNull("getAllAccounts return a null list of accounts", accounts);
        Assert.assertEquals("getAllAccounts returned a list with incorrect number of accounts", 2, accounts.size());
        assertAccountMatch("getAllAccount return a list of incorrect account", ACCOUNT_1, accounts.get(0));
        assertAccountMatch("getAllAccount return a list of incorrect account", ACCOUNT_2, accounts.get(1));

    }

    @Test
    public void getAccountById_with_valid_id_returns_correct_account(){
        Account account = dao.getAccountById(1);

        Assert.assertNotNull("getAccountById returned null", account);
        Assert.assertEquals("getAccountById with valid id did not return correct account", ACCOUNT_1, account);
    }

    @Test
    public void getAccountById_with_invalid_id_return_null_account(){
        Account account = dao.getAccountById(10);

        Assert.assertNull("getAccountById with invalid id did not return null user", account);
    }

    @Test
    public void getAccountByUserId_with_valid_userId_returns_correct_account(){
        Account account = dao.getAccountByUserId(2);

        Assert.assertNotNull("getAccountByUserId returned null", account);
        assertAccountMatch("getAccountByUserId with valid id return incorrect account", ACCOUNT_2, account);
    }

    @Test
    public void getAccountByUserId_with_invalid_userId_returns_null(){
        Account account = dao.getAccountByUserId(0);

        Assert.assertNull("getAccountByUserId with invalid id returned not null", account);
    }

    @Test
    public void createAccount_creates_an_account(){
        Account newAccount = new Account(3,new BigDecimal("10000.00"));

        Account account = dao.createAccount(newAccount);
        Assert.assertNotNull("createAccount return null account", account);

        Account actualAccount = dao.getAccountById(account.getId());
        Assert.assertNotNull("getAccountById return null account", actualAccount);

        newAccount.setId(actualAccount.getId());
        assertAccountMatch("Accounts do not match", newAccount, actualAccount);
    }

    @Test
    public void updateAccount_updates_account(){
        Account accountToUpdate = dao.getAccountById(ACCOUNT_1.getId());
        accountToUpdate.setUserId(1);
        accountToUpdate.setBalance(new BigDecimal("12341234.00"));

        Account updateAccount = dao.updateAccount(accountToUpdate);
        Account retrieveAccount = dao.getAccountById(ACCOUNT_1.getId());

        assertAccountMatch("updateAccount returned incorrect account data", updateAccount, retrieveAccount);
    }

    @Test
    public void deleteAccount_deletes_account(){
        int rowsAffected = dao.deleteAccount(2);

        Assert.assertEquals("Account was not deleted", 1, rowsAffected);
        Account retrieveAccount = dao.getAccountById(2);
        Assert.assertNull("Account can still be retrieved", retrieveAccount);
    }

    private void assertAccountMatch(String message, Account expected, Account actual){
        Assert.assertEquals(message, expected.getId(), actual.getId());
        Assert.assertEquals(message, expected.getUserId(), actual.getUserId());
        Assert.assertEquals(message, expected.getBalance(), actual.getBalance());
    }

}
