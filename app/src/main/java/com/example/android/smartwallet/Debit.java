package com.example.android.smartwallet;

import java.util.ArrayList;

/**
 * Created by Eric Cerqueira on 12/03/2016.
 */
public class Debit extends Account {
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();

    public Debit(String name) {
        super(name);
    }

    public Debit(String name, double balance) {
        super(name, balance);
    }

    public void addIncome(Income income) {
        incomes.add(income);
        increaseBalance(income.getValue());
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        decreaseBalance(expense.getValue());
    }
}
