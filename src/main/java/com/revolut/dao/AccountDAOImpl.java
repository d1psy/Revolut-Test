package com.revolut.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.revolut.model.Account;
import com.revolut.model.User;

public class AccountDAOImpl implements AccountDAO {

    private static SortedMap<String, Account> idMap = new TreeMap<>();
    private static Map<String, LinkedList<Account>> userMap = new HashMap<>();

    public Account create(User user, double balance) {
        Account account = new Account(user.getId(), balance);
        idMap.put(account.getId(), account);

        List<Account> userAccounts = userMap.computeIfAbsent(
                user.getId(), id -> new LinkedList<>()
        );
        userAccounts.add(account);

        return account;
    }

    public Account get(String id) {
        return idMap.get(id);
    }

    public Account delete(String id) {
        Account deleted = idMap.remove(id);
        if (deleted != null) {
            LinkedList<Account> userAccounts = userMap.get(deleted.getUserId());
            if (userAccounts != null) {
                userAccounts.remove(deleted);
            }
        }

        return deleted;
    }

    public Collection<Account> list() {
        return idMap.values();
    }

    public List listByUser(String userId) {
        List<Account> accounts = userMap.get(userId);
        if (accounts == null) {
            return Collections.EMPTY_LIST;
        }

        return accounts;
    }

    public void transfer(Account from, Account to, double amount) {
        if (from.getBalance() - amount < 0) {
            throw new RuntimeException("Not enough money on balance");
        }

        to.setBalance(to.getBalance() + amount);
        from.setBalance(from.getBalance() - amount);
    }

    public void deleteAll() {
        idMap.clear();
        userMap.clear();
    }
}
