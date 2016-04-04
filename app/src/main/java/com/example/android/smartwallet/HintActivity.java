package com.example.android.smartwallet;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HintActivity extends AppCompatActivity {
    LinearLayout hintBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        hintBox = (LinearLayout) findViewById(R.id.hint_box);

        ArrayList<Hint> hints = new ArrayList<>();
        hints.add(new Hint("Move bank accounts to take advantage of perks and earn more interest", "If you’re paying a monthly fee for your checking or savings account, " +
                "you would benefit from researching some of newest banking offers out there. " +
                "Not only do some banks offer sign-up bonuses simply for opening an account and " +
                "setting up direct deposit, but some offer attractive interest rates to new customers as well.\n\n" +
                "It’s true that interest rates are not what they once were, but it’s still worth a look. " +
                "Some of the best free checking accounts and best savings accounts can be found online. " +
                "Here’s a guide on how to make that switch."));
        hints.add(new Hint("Stop collecting, and start selling", "There was a time when people thought their collections would bring them riches. " +
                "Beanie Babies were a big fad at one time, as were Longaberger baskets. " +
                "Now you can find those items on resale sites like Craigslist and at garage sales for a " +
                "fraction of their initial cost, leaving many people who sunk thousands of dollars into their" +
                " “investments” wondering what happened.\n\n" +
                "If you want to avoid that situation, don’t collect items of questionable value. And if you want" +
                " to recoup some of the money you’ve already spent on collectible items, you can start selling " +
                "them now and use those funds for any number of worthy financial goals. Read our “Guide to Selling" +
                " Unwanted Items” for some simple strategies that can help you profit as much as possible."));
        hints.add(new Hint("Write a list before you go shopping – and stick to it.", "One of the easiest ways to save money is to only shop when you have a list. " +
                "Because when you’re without one, you typically end up making impulse buys and unplanned purchases" +
                " – all things that cost money.\n\n" +
                "Creating a list before you go to the grocery store is especially important. Not only can it help " +
                "you buy items that fit with your meal plan, but it can also help you avoid buying food you might waste." +
                " Always create a list and, more importantly, stick to it."));
        hints.add(new Hint("Avoid convenience foods and fast food.", "Instead of eating fast food or just nuking some prepackaged dinner when you get home, try making " +
                "some simple and healthy replacements that you can take with you. An hour’s worth of preparation one " +
                "weekend can leave you with a ton of cheap and easy dinner and snack options for the following week.\n\n" +
                "Also consider breaking out the ol’ crock pot for some inexpensive " +
                "meal options that not only save money, but time, too."));
        hints.add(new Hint("Buy used when you can.", "You can often find the exact item you want with a bit of clever shopping at used equipment stores, " +
                "used game stores, consignment shops, and so on. Just make these shops a part of your normal routine – " +
                "go there first when looking for potential items and you will save money.\n\n" +
                "Clothes, for example, often cost pennies on the dollar when bought used – even if they were only worn " +
                "once. By buying used most of the time, you can save a ton of cash."));

        for (int i = 0; i < hints.size(); ++i) {
            Hint hint = hints.get(i);
            addHint(i + 1, hint.getTitle(), hint.getText());
        }
    }

    private void addHint(int index, final String title, String text) {
        LinearLayout hint = (LinearLayout) getLayoutInflater().inflate(R.layout.hint, null);
        final TextView hintIndexValue = (TextView) hint.findViewById(R.id.hint_index);
        hintIndexValue.setText(String.valueOf(index) + ".");
        final TextView hintTitle = (TextView) hint.findViewById(R.id.hint_title);
        hintTitle.setText(title);
        hintTitle.post(new Runnable() {
            @Override
            public void run() {
                hintIndexValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16 * hintTitle.getLineCount());
            }
        });
        TextView hintText = (TextView) hint.findViewById(R.id.hint_text);
        hintText.setText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            hint.setId(View.generateViewId());
        }
        hintBox.addView(hint);
    }
}
