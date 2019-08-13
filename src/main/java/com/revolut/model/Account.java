package com.revolut.model;

import java.util.UUID;

public class Account {

    private String id;
    private String userId;
    private double balance;

    public Account(String userId, double balance) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}