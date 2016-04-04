package com.example.android.smartwallet;

/**
 * Created by Eric Cerqueira on 04/04/2016.
 */
public class Hint {
    String title, text;

    public Hint(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
