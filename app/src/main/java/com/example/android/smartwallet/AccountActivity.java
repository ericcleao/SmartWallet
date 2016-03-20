package com.example.android.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    String name, type;
    double limit;
    EditText nameLabel, limitLabel;
    RadioGroup accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        nameLabel = (EditText) findViewById(R.id.account_name_label);
        limitLabel = (EditText) findViewById(R.id.account_limit_label);
        accountType = (RadioGroup) findViewById(R.id.account_type_radio_group);

        accountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                TextView textView = (TextView) findViewById(R.id.account_type_text_view);

                type = radioButton.getText().toString();
                textView.setText(type + " account");
                textView.setVisibility(View.VISIBLE);

                if (type.equals("Credit")) {
                    limitLabel.setVisibility(View.VISIBLE);
                }

                accountType.setVisibility(View.GONE);
            }
        });
    }

    public void createAccount(View view) {
        if (type != null && !nameLabel.getText().toString().equals("")) {
            name = nameLabel.getText().toString();
            if (type.equals("Credit") && !limitLabel.getText().toString().equals("")) {
                try {
                    limit = Double.parseDouble(limitLabel.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }
            } else if (type.equals("Credit")) {
                Snackbar.make(getCurrentFocus(), "Credit's account needs a limit.", Snackbar.LENGTH_LONG).show();
                return;
            }
        } else if (type == null) {
            Snackbar.make(getCurrentFocus(), "Select an type account please.", Snackbar.LENGTH_LONG).show();
            return;
        } else {
            Snackbar.make(getCurrentFocus(), "The account needs a name.", Snackbar.LENGTH_LONG).show();
            return;
        }

        Bundle account = new Bundle();

        account.putString("name", name);
        account.putString("type", type);
        if (type.equals("Credit")) {
            account.putDouble("limit", limit);
        }

        Intent intent = new Intent();
        intent.putExtra("account", account);

        setResult(3, intent);
        finish();
    }

    public void cancelAccount(View view) {
        setResult(0);
        finish();
    }
}
