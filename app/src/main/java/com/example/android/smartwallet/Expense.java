package com.example.android.smartwallet;

/**
 * Created by Eric Cerqueira on 12/03/2016.
 */
public class Expense {
    String description;
    double value;
    int times;

    public Expense(String description, double value) {
        this.description = description;
        if (value < 0)
            this.value = (-1) * value;
        else
            this.value = value;

        this.times = 1;
    }

    public Expense(String description, double value, int times) {
        this.description = description;
        if (value < 0)
            this.value = (-1) * value;
        else
            this.value = value;

        this.times = times;
    }

    public double getValue() {
        return value;
    }
}
