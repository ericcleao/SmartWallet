package com.example.android.smartwallet;

/**
 * Created by Eric Cerqueira on 12/03/2016.
 */
abstract public class Account {
    String name;
    double balance;

    public Account(String name) {
        this.name = name;
        balance = 0;
    }

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public void increaseBalance(double value) {
        balance += value;
    }

    public void decreaseBalance(double value) {
        balance -= value;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }
}
