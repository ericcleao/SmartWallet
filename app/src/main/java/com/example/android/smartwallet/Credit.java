package com.example.android.smartwallet;

import java.util.ArrayList;

/**
 * Created by Eric Cerqueira on 12/03/2016.
 */
public class Credit extends Account {
    double limit;
    ArrayList<Expense> expenses = new ArrayList<>();

    public Credit(String name, double limit) {
        super(name);
        this.limit = limit;
    }

    public Credit(String name, double balance, double limit) {
        super(name, balance);
        this.limit = limit;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        decreaseBalance(expense.getValue());
    }

    public double getLimit() {
        return limit;
    }
}
