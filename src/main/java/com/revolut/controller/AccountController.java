package com.revolut.controller;

import com.revolut.dao.AccountDAO;
import com.revolut.dao.UserDAO;
import com.revolut.error.ErrorResponse;
import com.revolut.model.Account;
import com.revolut.model.Model;
import com.revolut.model.User;

import java.util.Collection;

public class AccountController {

    private static UserDAO userDAO = Model.userDAO;
    private static AccountDAO accountDAO = Model.accountDAO;

    public static Collection<Account> list() {
        return accountDAO.list();
    }

    public static Account create(String userId, double balance) {
        User user = userDAO.get(userId);
        if (user == null) {
            throw new ErrorResponse("Account does not exist", 404);
        }
        if (balance < 0) {
            throw new ErrorResponse("Balance can not be negative", 400);
        }

        return accountDAO.create(user, balance);
    }

    public static Account get(String id) {
        Account account = accountDAO.get(id);
        if (account == null) {
            throw new ErrorResponse("Account not found", 404);
        }
        return account;
    }

    public static void transfer(String fromId, String toId, double amount) {
        Account from = get(fromId);
        Account to = get(toId);
        if (amount < 0) {
            throw new ErrorResponse("Amount can not be negative", 400);
        }
        if (from.getId().equals(to.getId())) {
            throw new ErrorResponse("Can not transfer money from an account to itself",400);
        }

        try {
            accountDAO.transfer(from, to, amount);
        } catch (RuntimeException error) {
            throw new ErrorResponse(error.getMessage(), 400);
        }
    }

    public static void delete(String id) {
        Account deletedAccount = accountDAO.delete(id);
        if (deletedAccount == null) {
            throw new ErrorResponse("Account not found", 404);
        }
    }
}