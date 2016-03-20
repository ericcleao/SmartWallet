package com.example.android.smartwallet;

/**
 * Created by Eric Cerqueira on 12/03/2016.
 */
public class Income {
    String description;
    double value;

    public Income(String description, double value) {
        this.description = description;
        if (value < 0)
            this.value = (-1) * value;
        else
            this.value = value;
    }

    public double getValue() {
        return value;
    }
}
