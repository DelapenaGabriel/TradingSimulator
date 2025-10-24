package com.tradingsimulator.controller;

import com.tradingsimulator.exception.DaoException;
import com.tradingsimulator.model.Account;
import com.tradingsimulator.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/accounts")
@RestController
public class AccountController {
    private AccountService accountService;

    public AccountController (AccountService accountService){
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<Account> listAll(){
        List<Account> accounts;

        try{
            accounts = accountService.getAll();
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return accounts;
    }

    @GetMapping("")
    public Account getAccount(Principal principal){
        Account account;
        try{
            account = accountService.getAccountByUser(principal);
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return account;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public Account get(@PathVariable int id){
        Account account;
        try{
            account = accountService.getAccountById(id);
            if (account == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
            }
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return account;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Account create(@RequestBody Account newAccount, Principal principal){
        Account account;

        try{
            account = accountService.add(newAccount, principal);
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return account;
    }


    @PutMapping("/{id}")
    public Account update(@PathVariable int id, @RequestBody Account updateAccount, Principal principal){
        Account account;

        try{
            updateAccount.setId(id);
            account = accountService.update(updateAccount, principal);
            if (account == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
            }
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return account;

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable int id, Principal principal){
        try{
            int rowsDeleted = accountService.delete(id, principal);
            if (rowsDeleted == 0){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
            }
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
    }



}
