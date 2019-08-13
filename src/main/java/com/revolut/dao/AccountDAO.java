package com.revolut.dao;

import java.util.Collection;
import java.util.List;

import com.revolut.model.Account;
import com.revolut.model.User;

public interface AccountDAO {

    Account create(User user, double balance);
    Account get(String id);
    Account delete(String id);
    Collection<Account> list();
    List listByUser(String userId);

    void transfer(Account from, Account to, double amount);
    void deleteAll();
}